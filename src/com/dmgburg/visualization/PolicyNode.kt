package com.dmgburg.visualization

import java.util.ArrayList

class PolicyNode {
    var children: MutableList<PolicyNode> = ArrayList()
    var decision: Desicion? = null
    var description: String
    var id: String? = null

    constructor() {
        description = ""
    }

    constructor(description: String, decision: Desicion) : this(description) {
        this.decision = decision
    }

    fun addChild(child: PolicyNode): PolicyNode {
        children.add(child)
        return this
    }

    constructor(description: String) {
        this.description = description
    }

    override fun toString(): String {
        return "PolicyNode{" +
                "children=" + children +
                ", decision=" + decision +
                ", description='" + description + '\'' +
                ", id='" + id + '\'' +
                '}'
    }
}
