/*
 * ThreadPoolManger
 *
 * @author ZhaoPeng
 * @date 18-7-20
 */
package com.zp.threadPool;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public class ThreadPoolManger extends Thread implements ThreadPool {
    private final int MAX_WORKER_NUMBERS;
    private final int MIN_WORKER_NUMBERS;
    private int currentWorkers;
    private final LinkedBlockingQueue<Task> tasks;      //待分配线程处理的任务
    private final LinkedList<Worker> workers;       //分配出正在处理任务的工作线程

    public ThreadPoolManger(int max, int min){
        this.MAX_WORKER_NUMBERS = max;
        this.MIN_WORKER_NUMBERS = min;
        this.tasks = new LinkedBlockingQueue<>();
        this.workers = new LinkedList<>();
        for (int i= 0; i < MIN_WORKER_NUMBERS; i++){
            workers.add(new Worker(i));
            currentWorkers++;
        }
    }

    /**
     * 将新建的任务加入线程池的任务队列，等待线程池分配线程执行
     * @param task 新任务
     *             任务可能是发送、接受、心跳包等等等
     */
    @Override
    public void execute(Task task) {
        if(task != null){
            try {
                tasks.put(task);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void shutdown() {
        Worker worker;
        int n = workers.size();
        for (int i = 0; i < n; i++){
            worker = workers.removeFirst();
            worker.shutDown();
        }
    }

    @Override
    public int getTaskSize() {
        synchronized (tasks){
            return tasks.size();
        }
    }

    /**
     * 线程池管理线程
     * 根据上界和下界
     * 超出底线则将空闲线程移除工作队列
     * 大于当前容量小于最大容量则创建新线程加入工作队列
     */
    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (workers){
                if(tasks.size() < workers.size() && workers.size() > MIN_WORKER_NUMBERS){   //超出底线则将空闲线程移除工作队列
                    for (int i = 0; i < workers.size(); i++){
                        if(!workers.get(i).isRunning()){
                            workers.remove(i);
                            break;
                        }
                    }
                }
                if(tasks.size() > 2*workers.size() && workers.size() < MAX_WORKER_NUMBERS){     //大于当前容量小于最大容量则创建新线程加入工作队列
                    workers.addLast(new Worker(currentWorkers));
                    currentWorkers++;
                }
                //Logger.getGlobal().info(""+workers.size());
            }
        }
    }

    /**
     * 内部类处理线程的工作线程
     */
    class Worker extends Thread{
        private boolean isRunning;
        private int index;

        public Worker(int index){
            this.index = index;
            this.isRunning = false;
            start();
        }

        public boolean isRunning() {
            return isRunning;
        }

        @Override
        public void run() {
            while (true){
                Task task = null;
                try {
                    task = tasks.take();        //take：取出并移除blockingQueue元素，若队列为空则阻塞
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
                this.isRunning = true;  //运行时置true，运行结束置false
                task.run();         //执行task子类对应的run函数功能
                this.isRunning = false; //  执行结束恢复false状态
            }
        }

        public void shutDown(){
            interrupt();
        }
    }
}
