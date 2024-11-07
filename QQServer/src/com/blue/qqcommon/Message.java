package com.blue.qqcommon;

import java.io.Serializable;

/**
 * User: 王帅
 * Date&Time: 2021/11/7 17:27
 * 表示发送的信息
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;//增强兼容性
    private String sender;//发送方
    private String getter;//接收方
    private String content;//内容
    private String sendTime;//发送时间
    private String mesType;//消息类型（可以在接口中定义已知的消息类型）
    //消息类的扩展——和文件相关的成员变量
    private byte[] fileBytes;//文件数据
    private int fileLen = 0;//数组长度
    private String dest;//将文件传输到哪里
    private String src;//源文件路径

    public Message(String sender, String getter, String content, String sendTime, String mesType) {
        this.sender = sender;
        this.getter = getter;
        this.content = content;
        this.sendTime = sendTime;
        this.mesType = mesType;
    }
    public Message(){}
    public static long getSerialVersionID() {
        return serialVersionUID;
    }


    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public int getFileLen() {
        return fileLen;
    }

    public void setFileLen(int fileLen) {
        this.fileLen = fileLen;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMesType() {
        return mesType;
    }

    public void setMesType(String mesType) {
        this.mesType = mesType;
    }
}
