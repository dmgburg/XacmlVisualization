package com.dmgburg.visualization

import com.fasterxml.jackson.databind.ObjectMapper
import org.joda.time.DateTime
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@CrossOrigin(origins = arrayOf("*"))
@EnableAutoConfiguration
class JsonController {

    @RequestMapping("/decisions")
    @ResponseBody
    internal fun home(@RequestParam("fromDate") fromDate: String, @RequestParam("toDate") toDate: String): String {
        val decisionsMap = ClickhouseClient.getDecisions("myUser", "Support",
                DateTime.parse(fromDate),
                DateTime.parse(toDate)
        ).map { buildDecisionsMap(it.rootPolicy, it.desicions) }
        return ObjectMapper().writeValueAsString(decisionsMap)
    }

    private fun buildDecisionsMap(rootPolicy: String, desicions: Map<String, Desicion>) : PolicyNode {
        return applyDesicions(Parser.parse(ClickhouseClient.getPolicy(rootPolicy, "1").byteInputStream()), desicions)
    }

    private fun applyDesicions(root: PolicyNode, desicions: Map<String, Desicion>) : PolicyNode {
        var child = root
        root.decision = desicions.entries.reversed().first().value
        val iterator = desicions.entries.reversed().iterator()
        var backtrack = false;
        var current: Map.Entry<String, Desicion>? = iterator.next() // first decision is applied
        while (iterator.hasNext()) {
            if (!backtrack) {
                current = iterator.next()
            }
            backtrack = false
            val policy = child.children.find {
                it.id == current!!.key
            }
            if (policy == null) {
                // backtrack upwards
                backtrack = true
                child = child.parent ?: throw IllegalStateException("Backtracked up to root")
            } else {
                policy.decision = current!!.value
                child = policy

            }
        }
        return root
    }

    @RequestMapping("/html")
    internal fun html(): String {
        return "index.html"
    }
}