package com.atguigu.gulimall.search.thread;

import java.time.Year;
import java.util.concurrent.*;

/**
 * @author weepppp 2022/7/14 22:02
 **/
public class ThreadTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

        CompletableFuture<Integer> future01 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务1启动");
            int i = 10 / 2;
            System.out.println("任务1结束");
            return i;
        }, fixedThreadPool);

        CompletableFuture<Integer> future02 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务2启动");
            int i = 10 / 5;
            System.out.println("任务2结束");
            return i;
        }, fixedThreadPool);

        CompletableFuture<Integer> future = future01.thenCombineAsync(future02, (a, b) -> {
            return a + b;
        }, fixedThreadPool);

        System.out.println(future.get());




    }
}
