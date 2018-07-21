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
    private final LinkedBlockingQueue<Task> tasks;
    private final LinkedList<Worker> workers;

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

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (workers){
                if(tasks.size() < workers.size() && workers.size() > MIN_WORKER_NUMBERS){
                    for (int i = 0; i < workers.size(); i++){
                        if(!workers.get(i).isRunning()){
                            workers.remove(i);
                            break;
                        }
                    }
                }
                if(tasks.size() > 2*workers.size() && workers.size() < MAX_WORKER_NUMBERS){
                    workers.addLast(new Worker(currentWorkers));
                    currentWorkers++;
                }
                //Logger.getGlobal().info(""+workers.size());
            }
        }
    }

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
                    task = tasks.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
                this.isRunning = true;
                task.run();
                this.isRunning = false;
            }
        }

        public void shutDown(){
            interrupt();
        }
    }
}
