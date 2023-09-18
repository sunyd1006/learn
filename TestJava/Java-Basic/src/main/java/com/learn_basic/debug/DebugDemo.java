package com.learn_basic.debug;

import org.apache.logging.log4j.LogManager;
        import org.apache.logging.log4j.Logger;


/**
 * 测试Debug 打印的错误
 * ERROR StatusLogger Log4j2 could not find a logging implementation. Please add log4j-core to the classpath. Using SimpleLogger to log to the console...
 * ERROR DebugDemo ExceptionTest Exception 先打印的错误:
 *  java.lang.ArithmeticException: / by zero                    // 最后找到哪里出错 3
 * 	at com.debug.DebugDemo.test(DebugDemo.java:11)         // 再看这里 2
 * 	at com.debug.DebugDemo.main(DebugDemo.java:21)         // 先看这里 1
 *
 * ERROR DebugDemo ExceptionTest Exception: 后打印的错误
 *  java.lang.ArithmeticException: / by zerow
 * 	at com.debug.DebugDemo.test(DebugDemo.java:11)
 * 	at com.debug.DebugDemo.main(DebugDemo.java:21)
 */
public class DebugDemo {
    private static final Logger logger= LogManager.getLogger();
    public void  test() {
        try {
            int i=1/0;

        }catch(Exception e){
            logger.error("ExceptionTest Exception 先打印的错误:",e);
            throw e;
        }
    }
    public static void main(String[] args) {
        try {
            DebugDemo test= new DebugDemo();
            test.test();
        }catch (Exception e){
            logger.error("ExceptionTest Exception: 后打印的错误",e);
        }

    }

}
