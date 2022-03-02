package com.stitch.service.cms;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisStringCommands;

import java.util.concurrent.ExecutionException;

public class LettuceSimpleUse  {
    private void testLettuce() throws ExecutionException, InterruptedException {
        //构建RedisClient对象，RedisClient包含了Redis的基本配置信息，可以基于RedisClient创建RedisConnection
        RedisClient client = RedisClient.create("redis://192.168.1.128");

        //创建一个线程安全的StatefulRedisConnection，可以多线程并发对该connection操作,底层只有一个物理连接.
        StatefulRedisConnection<String, String> connection = client.connect();

        //获取SyncCommand。Lettuce支持SyncCommand、AsyncCommands、ActiveCommand三种command
        RedisStringCommands<String, String> sync = connection.sync();
        String value = sync.get("key");
        System.out.println("get redis value with lettuce sync command, value is :" + value);

        //获取SyncCommand。Lettuce支持SyncCommand、AsyncCommands、ActiveCommand三种command
        RedisAsyncCommands<String, String> async = connection.async();
        RedisFuture<String> getFuture = async.get("key");
        value = getFuture.get();
        System.out.println("get redis value with lettuce sync command, value is :" + value);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new LettuceSimpleUse().testLettuce();
    }
}