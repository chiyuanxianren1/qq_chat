# Java Socket聊天室

#### 介绍
（个人学习用）一个用Java多线程，集合，Socket编程，面向对象等内容编写的简易聊天室工程，后续会进行一些内容的补充、改进和完善。

#### 软件架构
软件架构说明
QQClient    ---    客户端程序
    qqclient    ---    客户端主要程序
        service    ---    客户端与服务器端实现通信的类
            ClientConnectServerThread.java    ---    客户端与服务器端通信的线程（线程持有一个Socket对象，保持两者之间的通信）
            FileClientServer.java    ---    完成客户端与服务器端之间的文件传输的类
            ManageClientConnectServerThread.java    ---    管理持有Socket对象的线程的类（主要通过集合进行线程的管理）
            MessageClientService.java    ---    完成客户端与服务器端之间的文字类消息的通信的类
            UserClientService.java    ---    完成用户登录验证和用户注册等功能的类
        utils    ---    用到的工具类
        view    ---    客户端界面程序
    qqcommon    ---    客户端关于消息的程序
QQServer    ---    服务器端程序



