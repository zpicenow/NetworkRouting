/*
 * TimedUpdatePackage
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.protocol.data;

import com.zp.netView.RouteTable;

import java.io.Serializable;
import java.util.LinkedList;

public class TimedUpdatePackage extends Package implements Serializable {
    private LinkedList<RouteTable> routeTables;

    public TimedUpdatePackage(LinkedList<RouteTable> routeTables){
        this.routeTables = routeTables;
    }

    public LinkedList<RouteTable> getRouteTables() {
        return routeTables;
    }
}
