package com.blue.qqclient.service;

import com.blue.qqcommon.Message;
import com.blue.qqcommon.MessageType;
import com.blue.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * User: 王帅
 * Date&Time: 2021/11/7 18:00
 * 该类完成用户登录验证和用户注册等功能
 */
public class UserClientService {
    //其他地方可能用到User对象，所以设定为属性
    private User u = new User();
    //创建一个Socket对象
    private Socket socket;
    //根据userId 和 pwd 到服务端验证该用户是否合法
    public boolean checkUser(String userId,String pwd){
        boolean b = false;
        //创建User对象
        u.setUserId(userId);
        u.setPasswd(pwd);
        try {
            //连接到服务器端，发送User对象
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
            //得到ObjectOutputStream对象，发送User对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u);//发送User对象
            //读取从服务器端回送的Message对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message ms = (Message) ois.readObject();

            if (ms.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)){//登录成功
                //启动一个线程，让线程持有一个Socket对象，保持与服务器端的通信
                //创建一个线程类ClientConnectServerThread
                ClientConnectServerThread ccst = new ClientConnectServerThread(socket);
                ccst.start();//启动客户端线程
                //为了后面客户端的扩展，我们将线程放入到集合中管理
                ManageClientConnectServerThread.addClientConnectServerThread(userId,ccst);
                b = true;

            }else {
                //如果登录失败，不能启动和服务器通信的线程，关闭Socket
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }
    //向服务器端请求在线用户列表
    public void onlineFriendList(){
        //发送一个Message对象，类型MESSAGE_GET_ONLINE_FRIEND
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(u.getUserId());
        //发送给服务器
        //得到当前线程的 Socket 对应的 ObjectOutPutStream 对象
        try {
            ObjectOutputStream oos = new ObjectOutputStream
                    (ManageClientConnectServerThread.getClientConnectServerThread(u.getUserId()).getSocket().getOutputStream());//拿到了当前线程->拿到Socket->拿到对象输出流
            oos.writeObject(message);//发送Message对象，向服务器端请求在线用户列表
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //编写方法，退出客户端，并给服务器端发送退出系统的Message对象
    public void logout(){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIST);//设置消息类型
        message.setSender(u.getUserId());//一定要指定是哪个客户端要退出系统
        //发送Message
        try {
            //ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            //通过userId获取Socket对象(多个socket)
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(u.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);
            System.out.println(u.getUserId()+" 退出系统");
            System.exit(0);//结束进程
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
