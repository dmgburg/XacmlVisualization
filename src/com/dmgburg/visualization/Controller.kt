package com.dmgburg.visualization

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.LinkedHashMap

import java.nio.charset.StandardCharsets.UTF_8

@Controller
@EnableAutoConfiguration
class JsonController {

    @RequestMapping("/")
    @ResponseBody
    internal fun home(): String {
        val root = PolicyNode("Policy resolver root")
        Parser().parse(root, File("E:\\dev\\alfa-visualizations\\resources\\PolicyGpms.xml"))
        Parser().parse(root, File("E:\\dev\\alfa-visualizations\\resources\\PolicyArma.xml"))
        val desicions = LinkedHashMap<String, Desicion>(
                mapOf(
                        "Arma-PolicySet1" to Desicion.ALLOW,
                        "Arma-PolicySet2" to Desicion.ALLOW,
                        "Arma-Proposal-Rules2" to Desicion.ALLOW
                )
        )
        applyDesicions(root, desicions)
        val mapper = ObjectMapper()

        return mapper.writeValueAsString(root)
    }

    private fun applyDesicions(root: PolicyNode, desicions: LinkedHashMap<String, Desicion>) {
        var child = root
        root.decision = desicions.entries.first().value
        for (current in desicions){
            val policy = child.children.find {
                it.id == current.key
            }
            if (policy == null){
                throw IllegalArgumentException("Policy not found: $current, from $child" )
            }
            policy.decision = current.value
            child = policy
        }
    }

    @RequestMapping("/html")
    @ResponseBody
    internal fun html(): String {
        val encoded = Files.readAllBytes(Paths.get("E:\\dev\\alfa-visualizations\\resources\\index.html"))
        return String(encoded, UTF_8)
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(JsonController::class.java, *args)
}