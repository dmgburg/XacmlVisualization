package com.dmgburg.visualization

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.ArrayList

class PolicyNode {
    @JsonIgnore
    var parent: PolicyNode? = null
    var children: MutableList<PolicyNode> = ArrayList()
    var decision: Desicion? = null
    var description: String
    var id: String? = null

    constructor(parent: PolicyNode?) {
        this.parent = parent
        description = ""
    }

    constructor(parent: PolicyNode?, description: String) {
        this.description = description
    }

    constructor(parent: PolicyNode,description: String, decision: Desicion) : this(parent, description) {
        this.decision = decision
    }

    fun addChild(child: PolicyNode): PolicyNode {
        children.add(child)
        return this
    }

    override fun toString(): String {
        return "PolicyNode{id='" + id + '\'' +'}'
    }
}
