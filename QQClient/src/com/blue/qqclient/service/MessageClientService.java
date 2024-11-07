package com.blue.qqclient.service;

import com.blue.qqcommon.Message;
import com.blue.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

/**
 * User: 王帅
 * Date&Time: 2021/11/8 21:45
 * 该类的提供和消息相关的服务方法
 */
public class MessageClientService {
    /**
     * 私聊方法
     * @param content 发送的消息
     * @param senderId 发送者id
     * @param getterId 接收者id
     */
    public void sendMessageToOne(String content,String senderId,String getterId){
        //构建Message对象
        Message message = new Message();
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setContent(content);
        message.setSendTime(new Date().toString());//发送时间设置到Message对象中
        message.setMesType(MessageType.MESSAGE_COMM_MES);//普通聊天消息
        System.out.println(senderId+" 对 "+getterId+" 说： "+content);
        //发送给服务器端
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param content 消息内容
     * @param senderId 发送者
     */
    public void sendMessageToAll(String content,String senderId){
        //构建Message对象
        Message message = new Message();
        message.setSender(senderId);
        message.setContent(content);
        message.setSendTime(new Date().toString());//发送时间设置到Message对象中
        message.setMesType(MessageType.MESSAGE_TOALL_MES);//群发消息
        System.out.print(senderId+" 对大家说： "+content);
        //发送给服务器端
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
