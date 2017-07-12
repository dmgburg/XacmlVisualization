package com.dmgburg.visualization;

import java.util.ArrayList;
import java.util.List;

public class PolicyNode {
    public List<PolicyNode> children = new ArrayList<PolicyNode>();
    public Desicion decision;
    public String description;
    public String id;

    public PolicyNode(){
        description = "";
    }

    public PolicyNode(String description, Desicion decision){
        this(description);
        this.decision = decision;
    }

    public PolicyNode addChild(PolicyNode child){
        children.add(child);
        return this;
    }

    public PolicyNode(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "PolicyNode{" +
                "children=" + children +
                ", decision=" + decision +
                ", description='" + description + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
