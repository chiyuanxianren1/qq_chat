package com.blue.qqserver.service;

import java.util.HashMap;
import java.util.Iterator;

/**
 * User: 王帅
 * Date&Time: 2021/11/7 21:16
 * 该类用于管理金和客户端通信的线程
 */
public class ManageClientThreads {
    private static HashMap<String,ServerConnectClientThread> hm = new HashMap<>();

    //得到管理线程的集合的方法
    public static HashMap<String, ServerConnectClientThread> getHm() {
        return hm;
    }

    //添加线程对象到hm集合
    public static void addClientThread(String userId,ServerConnectClientThread serverConnectClientThread){
        hm.put(userId,serverConnectClientThread);
    }

    //根据userId返回一个线程
    public static ServerConnectClientThread getServerConnectClientThread(String userId){
        return hm.get(userId);
    }

    //增加一个方法，从集合中移除某个线程对象
    public static  void removeServerConnectClientThread (String userId){
        hm.remove(userId);
    }

    //这里编写方法，返回在线用户列表
    public static String getOnlineUser(){
        //集合遍历，遍历HashMap的key
        Iterator<String> iterator = hm.keySet().iterator();//拿到一个迭代器
        String onlineUserList = "";
        while (iterator.hasNext()){//只要集合元素还存在下一个
            onlineUserList += iterator.next().toString() + " ";//拼接成字符串进行返回
        }
        return onlineUserList;
    }

}
