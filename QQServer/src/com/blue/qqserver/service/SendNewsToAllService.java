package com.blue.qqserver.service;

import com.blue.qqcommon.Message;
import com.blue.qqcommon.MessageType;
import com.blue.utils.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

/**
 * User: 王帅
 * Date&Time: 2021/11/9 20:32
 * 实现向所有在线用户推送新闻消息
 */
public class SendNewsToAllService implements Runnable {

    @Override
    public void run() {
        //为了可以推送多次新闻，使用while循环，使线程一直运行
        while (true){
            System.out.print("请输入服务器要推送的新闻/消息[输入exit退出推送服务线程]：");
            String news = Utility.readString(100);
            if ("exit".equals(news)){
                break;
            }
            //构建一个群发消息的类型
            Message message = new Message();
            message.setSender("服务器");
            message.setContent(news);
            message.setSendTime(new Date().toString());
            message.setMesType(MessageType.MESSAGE_TOALL_MES);
            System.out.println("服务器推送消息给所有在线用户："+news);
            //遍历当前所有的通信线程，得到socket，并发送Message对象
            HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();
            Iterator<String> iterator = hm.keySet().iterator();//迭代器遍历
            while (iterator.hasNext()){
                String onLineUserId = iterator.next().toString();
                ServerConnectClientThread serverConnectClientThread = hm.get(onLineUserId);//根据在线用户获取对应的线程
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
        }

}
