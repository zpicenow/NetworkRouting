/*
 * BroadcastPackage
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.protocol.data;

import com.zp.node.NeighNode;

import java.io.Serializable;
import java.util.LinkedList;

public class BroadcastPackage extends Package implements Serializable {
    private String id;
    private int port;
    private LinkedList<NeighNode> neighNodes;

    public BroadcastPackage(String id, int port, LinkedList<NeighNode> neighNodes){
        this.id = id;
        this.port = port;
        this.neighNodes = neighNodes;
    }

    public String getId() {
        return id;
    }

    public int getPort() {
        return port;
    }

    public LinkedList<NeighNode> getNeighNodes() {
        return neighNodes;
    }
}
