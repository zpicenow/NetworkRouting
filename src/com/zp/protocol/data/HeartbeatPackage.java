/*
 * HeartbeatPackage
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.protocol.data;

import java.io.Serializable;

public class HeartbeatPackage extends Package implements Serializable {
    private String id;

    public HeartbeatPackage(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
