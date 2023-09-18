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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import static java.lang.Thread.sleep;

public class ClientSocketDemo {
    public static void main(String[] args) {
        try {
            // 客户端链接服务器的，ip+port
            Socket s = new Socket("127.0.0.1",8888);
            
            //构建IO
            InputStream is = s.getInputStream();
            OutputStream os = s.getOutputStream();
            
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            //向服务器端发送一条消息
            bw.write("测试客户端和服务器通信，服务器接收到消息返回到客户端\n");
            bw.flush();
            
            //读取服务器返回的消息
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String mess = br.readLine();
            System.out.println("服务器："+mess);
            
            try {
                sleep(5);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
