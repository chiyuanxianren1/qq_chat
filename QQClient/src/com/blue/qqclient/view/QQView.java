package com.blue.qqclient.view;

import com.blue.qqclient.service.FileClientService;
import com.blue.qqclient.service.MessageClientService;
import com.blue.qqclient.service.UserClientService;
import com.blue.qqclient.utils.Utility;

import java.io.FileInputStream;

/**
 * User: 王帅
 * Date&Time: 2021/11/7 17:34
 * 客户端的菜单界面
 */
public class QQView {
    private boolean loop = true;//控制是否显示菜单
    private String key = "";//接收用户的键盘输入
    private UserClientService userClientService = new UserClientService();//用来登录服务器/注册用户
    private MessageClientService messageClientService = new MessageClientService();//私聊消息的发送
    private FileClientService fileClientService = new FileClientService();//用于传输文件

    public static void main(String[] args) {
        new QQView().mainMenu();
        System.out.println("系统退出！");
    }

    private void mainMenu(){
        while (loop){
            System.out.println("**********欢迎登录网络通信系统**********");
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 9 退出系统");
            System.out.print("请输入你的选择：");
            //根据用户的输入，来处理不同的逻辑
            key = Utility.readString(1);
            switch (key){
                case "1":
                    System.out.print("请输入用户名：");
                    String userId = Utility.readString(50);
                    System.out.print("请输入密码：");
                    String pwd = Utility.readString(50);
                    //到服务器端去验证该用户是否合法
                    //使用UserClientService类（用户登录、注册）
                    if (userClientService.checkUser(userId,pwd)){
                        System.out.println("**********欢迎 "+userId+" **********");
                        //进入到二级菜单
                        while (loop){
                            System.out.println("\n**********网络通信系统二级菜单（用户 "+userId+"）**********");
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私聊消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 9 退出系统");
                            System.out.print("请输入你的选择：");
                            key = Utility.readString(1);
                            switch (key){
                                case "1":
                                    //System.out.println("显示在线用户列表");
                                    //写一个方法来获取在线用户列表
                                    userClientService.onlineFriendList();
                                    break;
                                case "2":
                                    System.out.print("请输入要群发的消息内容：");
                                    String s = Utility.readString(100);
                                    //调用一个方法，将消息封装成Message对象，发送给服务器端
                                    messageClientService.sendMessageToAll(s,userId);
                                    break;
                                case "3":
                                    System.out.print("请输入想聊天的用户名（在线）：");
                                    String getterId = Utility.readString(50);
                                    System.out.print("请输入要发送的消息内容：");
                                    String content = Utility.readString(100);
                                    //编写一个方法，将消息发送给服务器端
                                    messageClientService.sendMessageToOne(content,userId,getterId);
                                    System.out.println("私聊消息");
                                    break;
                                case "4":
                                    System.out.print("请输入你想发送文件的用户（在线）：");
                                    getterId = Utility.readString(50);
                                    System.out.print("请输入你要发送文件的路径（形式 D:\\xx.jpg）：");
                                    String src = Utility.readString(100);
                                    System.out.print("请输入把文件发送到对方的路径（形式 D:\\xx.jpg）：");
                                    String dest = Utility.readString(100);
                                    fileClientService.sendFileToOne(src,dest,userId,getterId);
                                    break;
                                case "9":
                                    //调用一个方法，给服务器端发送一个退出系统的Message
                                    userClientService.logout();
                                    loop = false;
                                    break;
                            }
                        }
                    }else {
                        System.out.println("**********登录失败**********");
                    }
                    break;
                case "9":
                    loop = false;
                    System.out.println("退出系统");
                    break;
            }
        }
    }
}
