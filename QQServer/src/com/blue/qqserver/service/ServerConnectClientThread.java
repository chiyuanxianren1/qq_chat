package com.blue.qqserver.service;
import com.blue.qqcommon.Message;
import com.blue.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

/**
 * User: 王帅
 * Date&Time: 2021/11/7 21:05
 * 该类对应的对象和某个客户端保持通信
 */
public class ServerConnectClientThread extends Thread {
    //必须有一个Socket
    private Socket socket;
    private String userId;//连接到服务器端的客户端的唯一id

    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }
    //获取socket
    public Socket getSocket() {
        return socket;
    }
    @Override
    public void run() {//线程处于一个运行状态，可以发送或接受客户端的消息
        while (true){
            try {
                System.out.println("服务器端和客户端 "+userId+" 保持通信，读取数据中...");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();//接受消息内容
                //根据Message的类型，做相应的业务处理
                if (message.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)){
                    //客户端请求在线用户列表
                    /**
                     * 返回形式：100 王帅 齐天大圣 刘德华
                     */
                    System.out.println(message.getSender() + " 请求在线用户列表");
                    String onlineUser = ManageClientThreads.getOnlineUser();
                    //返回Message对象给客户端
                    //构建一个Message对象，返回给客户端
                    Message message2 = new Message();
                    message2.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message2.setContent(onlineUser);
                    message2.setGetter(message.getSender());
                    //返回给客户端
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message2);
                }else if (message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIST)){//如果发送的消息类型是客户端退出系统
                    //关闭socket
                    System.out.println(message.getSender()+" 退出系统");
                    //将这个客户端对应的线程从集合中删除
                    ManageClientThreads.removeServerConnectClientThread(message.getSender());
                    //关闭socket连接
                    socket.close();
                    //退出线程
                    break;
                }else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)){
                    //根据Message获取getterId，然后再获取对应的线程
                    ServerConnectClientThread serverConnectClientThread = ManageClientThreads.getServerConnectClientThread(message.getGetter());
                    //得到对应的getter的Socket的对象输出流，将Message对象转发给指定的客户端
                    ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                    oos.writeObject(message);//发送message，如果客户不在线，可以保存到数据库，就可以实现离线留言
                }else if (message.getMesType().equals(MessageType.MESSAGE_TOALL_MES)){
                    //遍历除发送者之外的所有用户的线程，将Message进行转发即可
                    //得到HashMap
                    HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();
                    //开始遍历
                    Iterator<String> iterator = hm.keySet().iterator();
                    while (iterator.hasNext()){
                        //取出在线用户的id
                        String onLineUserId = iterator.next().toString();
                        if (!onLineUserId.equals(message.getSender())){//排除发送者
                            //进行转发
                            ObjectOutputStream oos = new ObjectOutputStream(hm.get(onLineUserId).getSocket().getOutputStream());
                            oos.writeObject(message);
                        }
                    }
                }
                else if (message.getMesType().equals(MessageType.MESSAGE_FILE_MES)){
                    //根据getterId获取到对应的线程，将Message对象进行转发
                    ServerConnectClientThread serverConnectClientThread = ManageClientThreads.getServerConnectClientThread(message.getGetter());
                    //创建文件输出流
                    ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                    oos.writeObject(message);
                }
                else {//不是拉取在线用户列表
                    System.out.println("其他类型的Message，暂时不处理...");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
