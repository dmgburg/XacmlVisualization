package com.dmgburg.visualization

import com.dmgburg.visualization.ClickhouseClient.Companion.desision
import com.dmgburg.visualization.ClickhouseClient.Companion.init
import com.dmgburg.visualization.ClickhouseClient.Companion.policy
import org.apache.commons.io.IOUtils
import org.joda.time.DateTime
import org.joda.time.LocalDate
import ru.yandex.clickhouse.ClickHouseArray
import ru.yandex.clickhouse.ClickHouseDataSource
import java.sql.Date
import java.sql.Types

class ClickhouseClient {

    companion object {
        val url = "jdbc:clickhouse://192.168.99.100:8123"
        val driver = ClickHouseDataSource(url)

        fun uploadPolicy(name: String, version: String, text: String) {
            val statement = driver.connection.prepareStatement("insert into policies values (?,?,?)")
            statement.setString(1, name)
            statement.setString(2, version)
            statement.setString(3, text)
            statement.execute()
        }

        fun uploadDesicions(rootPolicy: String,
                            version: String,
                            dateTime: DateTime,
                            policies: List<String>,
                            decisions: List<Desicion>, attributes: Map<String, String>) {
            val attributeNames = attributes.map { it -> it.key }
            val attributeValues = attributes.map { it -> it.value }
            val statement = driver.connection.prepareStatement(
                    "insert into decisions (policy , version, timestamp, date, policies, decisions, " +
                            "${attributeNames.joinToString(", ")})" +
                            " values (?,?,?,?,?,?,${(0..attributeNames.size - 1).map { "?" }.joinToString(", ")})")
            statement.setString(1, rootPolicy)
            statement.setString(2, version)
            statement.setLong(3, dateTime.millis)
            statement.setDate(4, Date.valueOf(LocalDate(dateTime).toString()))
            statement.setArray(5, ClickHouseArray(Types.VARCHAR, policies.toTypedArray()))
            statement.setArray(6, ClickHouseArray(Types.VARCHAR, decisions.map { it.name }.toTypedArray()))
            attributeValues.forEachIndexed { i, value -> statement.setString(7 + i, value) }
            statement.execute()
        }

        fun getPolicy(name: String, version: String): String {
            val statement = driver.connection.prepareStatement("select * from policies where name = ? and version = ?")
            statement.setString(1, name)
            statement.setString(2, version)
            val resultSet = statement.executeQuery()
            resultSet.next()
            return resultSet.getString(3)
        }

        fun createDecisionsTable() {
            driver.connection.prepareStatement("create table decisions " +
                    "(policy String, version String, timestamp Int64, date Date, policies Array(String), " +
                    "decisions Array(Enum8('ALLOW' = 1, 'DENY' = 2, 'NOT_APPLICABLE' = 3, 'SYSEM_ERROR' = 4)), " +
                    "userId String, role String)" +
                    "Engine = MergeTree (date, timestamp, 8192)").execute()
        }

        fun createPoliciesTable() {
            driver.connection.prepareStatement("create table policies " +
                    "(name String, version String, text String) Engine = TinyLog").execute()
        }

        inline fun tryTo(closure: () -> Unit) {
            try {
                closure.invoke()
            } catch (e: Exception) {
                println("Exception: ${e.message}")
            }
        }

        fun cleanup() {
            tryTo { driver.connection.prepareStatement("drop table policies").execute() }
            tryTo { driver.connection.prepareStatement("drop table decisions").execute() }
        }

        fun policy(name: String, version: String) {
            uploadPolicy(name, version, IOUtils.toString(ClickhouseClient::class.java.getResourceAsStream("/$name.xml")))
        }

        fun init() {
            cleanup()
            createDecisionsTable()
            createPoliciesTable()
        }

        fun desision(dateTime: DateTime, rootPolicy: String, decisionsMap: Map<String, Desicion>, attributes: Map<String, String>) {
            val decisions = arrayListOf<Desicion>()
            val policies = arrayListOf<String>()
            decisionsMap.forEach({ policy, decision ->
                policies.add(policy)
                decisions.add(decision)
            })
            uploadDesicions(rootPolicy, "1", dateTime, policies, decisions, attributes)
        }

        fun getDecisions(userId: String, role: String, fromDate: DateTime, toDate: DateTime): List<DecisionTree> {
            val statement = driver.connection.prepareStatement("select * from decisions where userId=? and role=? and timestamp > ? and timestamp < ?")
            statement.setString(1, userId)
            statement.setString(2, role)
            statement.setLong(3, fromDate.millis)
            statement.setLong(4, toDate.millis)

            val resultSet = statement.executeQuery()
            val decisionTrees = mutableListOf<DecisionTree>()
            while (resultSet.next()) {
                val decisionTree = mutableMapOf<String, Desicion>()
                val policies = resultSet.getArray(5).array as Array<String>
                val desicions = resultSet.getArray(6).array as Array<String>
                (0..policies.size - 1).forEach { i ->
                    decisionTree.put(policies[i], Desicion.valueOf(desicions[i]))
                }
                decisionTrees.add(DecisionTree(resultSet.getString(1), decisionTree))
            }
            return decisionTrees
        }
    }
}

fun main(args: Array<String>) {
    init()
    policy("PolicyArma", "1")
    policy("PolicyGpms", "1")
    desision(DateTime.parse("2017-06-30T02:20+02:00"),
            "PolicyArma",
            mapOf(
                    "Arma-Proposal-Rules" to Desicion.NOT_APPLICABLE,
                    "Arma-Proposal-Rules2" to Desicion.ALLOW,
                    "Arma-PolicySet2" to Desicion.ALLOW,
                    "Arma-PolicySet1" to Desicion.ALLOW
            ), mapOf(
            "userId" to "myUser",
            "role" to "Support"
    ))
    desision(DateTime.parse("2017-06-30T03:20+02:00"),
            "PolicyGpms",
            mapOf(
                    "Gpms-Proposal-Rules" to Desicion.NOT_APPLICABLE,
                    "Gpms-Proposal-Rules2" to Desicion.ALLOW,
                    "Gpms-PolicySet2" to Desicion.DENY,
                    "Gpms-PolicySet1" to Desicion.ALLOW
            ), mapOf(
            "userId" to "myUser",
            "role" to "Support"
    ))
    desision(DateTime.parse("2017-06-30T04:20+02:00"),
            "PolicyArma",
            mapOf(
                    "Arma-Proposal-Rules" to Desicion.ALLOW,
                    "Arma-PolicySet1" to Desicion.ALLOW
            ), mapOf(
            "userId" to "myUser",
            "role" to "Support"
    ))
    desision(DateTime.parse("2017-06-30T05:20+02:00"),
            "PolicyArma",
            mapOf(
                    "Arma-Proposal-Rules" to Desicion.NOT_APPLICABLE,
                    "Arma-Proposal-Rules2" to Desicion.DENY,
                    "Arma-PolicySet2" to Desicion.DENY,
                    "Arma-PolicySet1" to Desicion.DENY
            ), mapOf(
            "userId" to "myUser",
            "role" to "Support"
    ))
}