package com.blue.qqclient.service;

import com.blue.qqcommon.Message;
import com.blue.qqcommon.MessageType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * User: 王帅
 * Date&Time: 2021/11/9 17:54
 * 该类完成文件的传输服务
 */
public class FileClientService {
    /**
     *
     * @param src 源文件路径
     * @param dest 文件传输目的路径
     * @param senderId 发送者id
     * @param getterId 接收者id
     */
    public void sendFileToOne(String src,String dest,String senderId,String getterId){
        //读取src文件（该文件必须存在）--> 封装到message中
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_FILE_MES);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setSrc(src);
        message.setDest(dest);

        //将文件读取
        FileInputStream fileInputStream = null;//创建文件输入流
        byte[] fileBytes = new byte[(int) new File(src).length()];//创建字节数组

        try {
            fileInputStream = new FileInputStream(src);//文件输入流
            fileInputStream.read(fileBytes);//将文件读入内存的字节数组
            //把字节数组设置到Message对象中去
            message.setFileBytes(fileBytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭流
            if (fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //提示信息
        System.out.println("\n"+senderId +" 给 "+getterId+" 发送了 "+src+" 到对方的电脑目录："+ dest);
        //发送
        //拿到对象输出流
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
