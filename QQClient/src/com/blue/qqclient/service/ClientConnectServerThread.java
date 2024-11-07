package com.blue.qqclient.service;

import com.blue.qqcommon.Message;
import com.blue.qqcommon.MessageType;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * User: 王帅
 * Date&Time: 2021/11/7 18:20
 * 线程类，用来持有一个Socket对象，保持和服务器端的通信
 */
public class ClientConnectServerThread extends Thread {
    //该线程需要持有一个Socket
    private Socket socket;

    //构造器可以得到一个Socket
    public ClientConnectServerThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        //在后台与服务器进行通信，用一个while循环来控制
        while (true){
            //客户端线程等待读取从服务器端回送的消息
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();//如果服务器端没有发送消息，程序会阻塞在这里
                //判断Message的类型，然后做相应的业务处理
                //如果读取到的是服务器端返回的在线用户列表，
                if (message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)){
                    //取出在线用户列表信息，并显示
                    //规定：如果服务器端回复在线用户列表形式用空格隔开
                    String[] onlineUsers = message.getContent().split(" ");
                    System.out.println("\n**********当前在线用户列表如下**********");
                    for (int i = 0;i < onlineUsers.length;i++){
                        System.out.println("用户："+onlineUsers[i]);
                    }
                }else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)){//如果接收到的是一个聊天消息
                    //把从服务器端转发的消息显示到控制台
                    System.out.println("\n"+message.getSender()+" 对 "+message.getGetter()+" 说 "+message.getContent());
                }
                else if (message.getMesType().equals(MessageType.MESSAGE_TOALL_MES)){
                    //如果接收到的是群发消息，则显示到控制台
                    System.out.println("\n"+message.getSender()+" 对所有人说："+message.getContent());
                }else if (message.getMesType().equals(MessageType.MESSAGE_FILE_MES)){//如果是含有文件的消息
                    System.out.println("\n"+message.getSender()+" 给 "+message.getSender()+" 发送了文件到："+message.getDest());
                    //将内存中的文件字节数组取出到次哦啊中，通过文件输出流（写出到磁盘）
                    FileOutputStream fileOutputStream = new FileOutputStream(message.getDest());
                    fileOutputStream.write(message.getFileBytes());
                    fileOutputStream.close();
                    System.out.println("保存文件成功！");
                }
                else {
                    System.out.println("是其他类型的Message，暂时不处理...");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //为了更方便的得到这个Socket对象，创建相应的方法
    public Socket getSocket() {
        return socket;
    }
}
