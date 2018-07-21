/*
 * ThreadPool
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.threadPool;

public interface ThreadPool {
    void execute(Task task);
    void shutdown();
    int getTaskSize();
}
