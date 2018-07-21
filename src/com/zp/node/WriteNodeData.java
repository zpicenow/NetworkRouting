/*
 * WriteNodeData
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.node;

import com.zp.protocol.data.BroadcastPackage;
import com.zp.protocol.data.HeartbeatPackage;
import com.zp.protocol.data.TimedUpdatePackage;

/**
 * 对节点信息修改接口
 */
public interface WriteNodeData extends ReadNodeData {
    boolean processBroadcast(BroadcastPackage broadcastPackage);
    void setHeartbeatTime(HeartbeatPackage heartbeatData);
    void processTimedUpdate(TimedUpdatePackage timedUpdateData);
    void deleteLinkNode(NeighNode neighNode);
}
