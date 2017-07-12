package com.dmgburg.visualization

import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class Parser {
    fun parse(root: PolicyNode, inputFile: File) : PolicyNode {
        val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputFile)
        doc.documentElement.normalize()
        val policy = PolicyNode()
        root.addChild(policy)
        parse(doc.documentElement, policy)
        return root
    }

    fun parse(element: Node, parentNode: PolicyNode) {
        val attributes = element.attributes
        parentNode.id = attributes.getNamedItem("PolicySetId")?.nodeValue ?: attributes.getNamedItem("PolicyId")?.nodeValue
        for (nNode: Node in element.childNodes) {
            when (nNode.nodeName) {
                "Description" -> parentNode.description = getText(nNode)
                "Policy", "PolicySet" -> {
                    val policyNode = PolicyNode()
                    parse(nNode, policyNode)
                    parentNode.addChild(policyNode)
                }
                else -> nNode.nodeName
            }
        }
    }

    fun getText(nNode: Node): String {
        val sb = StringBuilder()
        val list = nNode.childNodes
        for (i in 0..list.length - 1) {
            sb.append(list.item(i).nodeValue)
        }
        return sb.toString()
    }

    operator fun NodeList.iterator() : Iterator<Node>{
        return object : Iterator<Node> {
            var index = 0;
            override fun hasNext(): Boolean {
                return index < this@iterator.length;
            }

            override fun next(): Node {
                return this@iterator.item(index++)
            }
        }
    }
}