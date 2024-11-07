package com.blue.qqframe;

import com.blue.qqserver.service.QQServer;

/**
 * User: 王帅
 * Date&Time: 2021/11/7 21:32
 * 该类创建一个QQServer对象，启动后台服务
 */
public class QQFrame {
    public static void main(String[] args) {
        new QQServer();
    }
}
