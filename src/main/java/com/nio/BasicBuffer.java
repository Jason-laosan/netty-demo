package com.nio;

import java.nio.IntBuffer;

/**
 * @author: laosan
 * Date: 2021/6/18
 * Time: 10:34 PM
 * Describe:
 */
public class BasicBuffer {
    public static void main(String[] args) {
        IntBuffer intBuffer = IntBuffer.allocate(5);
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i * 2);
        }

        //buffer转换， 读些切换
        intBuffer.flip();
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }


    }
}
