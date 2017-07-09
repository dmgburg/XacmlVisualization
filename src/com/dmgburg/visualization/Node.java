package com.dmgburg.visualization;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Denis on 04.07.2017.
 */
public class Node {
    public List<Node> children = new ArrayList<Node>();
    public Desicion decision;
    public String description;

    public Node(){
        description = "";
    }

    public Node(String description, Desicion decision){
        this(description);
        this.decision = decision;
    }

    public Node addChild(Node child){
        children.add(child);
        return this;
    }

    public Node(String description) {
        this.description = description;
    }
}
