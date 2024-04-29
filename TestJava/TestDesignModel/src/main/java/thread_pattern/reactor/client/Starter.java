/**
 * sharemer.com Inc.
 * Copyright (c) 2009-2019 All Rights Reserved.
 */
package thread_pattern.reactor.client;

/**
 * @author sunqinwen
 * @version \: Starter.java,v 0.1 2019-03-29 15:50
 */
public class Starter {

    public static void main(String[] args) {
        new Thread(new NIOClient("127.0.0.1", 2333)).start();
        new Thread(new NIOClient("127.0.0.1", 2333)).start();
    }
}
