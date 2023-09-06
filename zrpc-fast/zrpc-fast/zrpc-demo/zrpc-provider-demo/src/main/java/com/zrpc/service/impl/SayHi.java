package com.zrpc.service.impl;

import com.zrpc.api.SayHello;

public class SayHi implements SayHello {
    @Override
    public String sayHello(String msg) {
        return "hello,client--->" + msg;
    }
}
