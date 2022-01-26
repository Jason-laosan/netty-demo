package com;

import io.netty.util.NettyRuntime;

/**
 * @author: laosan
 * Date: 2021/6/22
 * Time: 9:50 AM
 * Describe:
 */
public class CpuTest {
    public static void main(String[] args) {
        System.out.println(NettyRuntime.availableProcessors());
        System.out.println(Runtime.getRuntime().availableProcessors());
    }
}
