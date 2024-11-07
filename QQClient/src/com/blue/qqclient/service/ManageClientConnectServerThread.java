package com.blue.qqclient.service;

import java.util.HashMap;

/**
 * User: 王帅
 * Date&Time: 2021/11/7 18:35
 * 管理线程连接到服务器端的线程的类
 */
public class ManageClientConnectServerThread {
    //把多个线程放入到集合中进行管理
    private static HashMap<String,ClientConnectServerThread> hm = new HashMap<>();

    //将某个线程加入到集合
    public static void   addClientConnectServerThread(String userId,ClientConnectServerThread clientConnectServerThread){
        hm.put(userId,clientConnectServerThread);
    }
    //通过userId可以得到对应的线程
    public static  ClientConnectServerThread getClientConnectServerThread(String userId){
        return hm.get(userId);
    }
}
