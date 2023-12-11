package com.learn_basic.network;


/**
 * 教程：http://c.biancheng.net/view/2123.html
 * 通过 socket() 函数来创建一个网络连接，或者说打开一个网络文件，socket() 的返回值就是文件描述符。
 *  linux 一切都是文件，故而Sokect 也是一个文件
 *  windows 区分了Sokect和文件，本质上也只是把sokect拿出来单独处理而已。
 * 有了文件描述符，我们就可以使用普通的文件操作函数来传输数据了，例如：
 *      用 read() 读取从远程计算机传来的数据；
 *      用 write() 向远程计算机写入数据。
 * socket主要分两种，
 * 1种 streamSocket 底层是tcp, 速率稳定，传输内容有序到达，没有遗漏
 * 1种 dagramSocket，底层UDP，速率快，不按需到达，可能有丢失
 *
 * ip 定位到局域网
 * mac地址，定位到计算机
 * port地址，定位到计算机上的应用程序进程
 *
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class ServerSocketDemo {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(8888);
            System.out.println("启动服务器....");
            Socket s = ss.accept();
            System.out.println("客户端:"+s.getInetAddress().getLocalHost()+"已连接到服务器");
            
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            //读取客户端发送来的消息
            String mess = br.readLine();
            System.out.println("客户端："+mess);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            bw.write(mess+"\n");
            bw.flush();
    
            try {
                sleep(5);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

