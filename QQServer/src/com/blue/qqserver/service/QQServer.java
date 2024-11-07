package com.blue.qqserver.service;

import com.blue.qqcommon.Message;
import com.blue.qqcommon.MessageType;
import com.blue.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: 王帅
 * Date&Time: 2021/11/7 20:49
 * 服务器端，监听9999端口，等待客户端的连接，并保持通信
 */
public class QQServer {
    private ServerSocket ss = null;
    //创建一个集合，存放多个用户，如果是这些用户登录，就认为是合法的
    //在这里也可以使用 ConcurrentHashMap，可以处理并发的集合，没有线程安全问题
    private static ConcurrentHashMap<String,User> validUsers = new ConcurrentHashMap<>();
    //HashMap 没有处理线程安全，因此在多线程情况下是不安全的
    //ConcurrentHashMap 处理的线程安全，即线程同步处理，在多线程情况下是安全的
//    private static HashMap<String,User> validUsers = new HashMap<>();
    static {//静态代码块初始化用户//加载QQServer的时候会初始化一次
        //如果用户传输的userId和password是这里面任意一个，就允许登录
        validUsers.put("100",new User("100","123456"));
        validUsers.put("200",new User("200","123456"));
        validUsers.put("300",new User("300","123456"));
        validUsers.put("王帅",new User("王帅","123456"));
        validUsers.put("刘德华",new User("刘德华","123456"));
        validUsers.put("齐天大圣",new User("齐天大圣","123456"));
    }
    //验证用户是否有效的方法
    private boolean checkUser(String userId,String passwd){
        User user = validUsers.get(userId);
        if (user == null){//用户不存在
            return false;
        }
        if (!user.getPasswd().equals(passwd)){//用户名正确，密码错误
            return false;
        }
        return true;
    }
    public QQServer(){
        //端口可以写在配置文件
        try {
            System.out.println("服务器端在9999端口监听...");
            //启动推送服务的线程
            new Thread(new SendNewsToAllService()).start();
            ss = new ServerSocket(9999);



            //循环监听
            while (true){//当和某个客户端建立连接之后，会继续监听，因此使用while循环
                Socket socket = ss.accept();//如果没有客户端，就会阻塞在这里
                //通过Socket对象来接收客户端发送过来的对象
                //得到Socket关联的对象输入流
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //得到Socket关联的对象输出流
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                User u = (User) ois.readObject();//读取客户端第一次发送的User对象
                //创建一个Message对象，准备回复客户端
                Message message = new Message();
                if (checkUser(u.getUserId(),u.getPasswd())){//登录成功
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    //将Message对象回复给客户端
                    oos.writeObject(message);
                    //创建一个线程，，该线程需要持有一个Socket对象，和客户端保持通信
                    ServerConnectClientThread scct = new ServerConnectClientThread(socket,u.getUserId());
                    //启动线程
                    scct.start();
                    //把线程对象放入到集合中进行管理
                    ManageClientThreads.addClientThread(u.getUserId(),scct);
                }else {//登录失败
                    System.out.println("用户 id="+u.getUserId()+"pwd="+u.getPasswd()+"验证失败！");
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    //关闭Socket对象
                    socket.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //如果服务器端退出了while循环，说明服务器端不再监听，因此需要关闭资源
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
