package com.easyrest.netty.conf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;

import java.util.HashMap;
import java.util.Map;

import static io.netty.channel.ChannelOption.*;

/**
 * The connect option setting.
 * Created by liuhongyu.louie on 2016/9/17.
 */
public class ChannelOptionBuilder {

    private boolean tcpNoDelay= true;
    private boolean soKeepAlive = true;
    private boolean soReuseAddr = true;
    private int soBackLog = 1024;
    private int soLinger = -1; //Default closed
    private int soRcvBuf = 262140; //Java Default
    private int connectTimeOutMillis = 10000; // Netty default
    private ByteBufAllocator allocator = new PooledByteBufAllocator(true);
    private Map<ChannelOption, Object> options = new HashMap<>();
    private Map<ChannelOption, Object> childOptions = new HashMap<>();

    public <T> ChannelOptionBuilder addOption(ChannelOption<T> option, T value, OptionType type){
        switch (type){
            case Parent:options.put(option, value);
                break;
            case Child:childOptions.put(option, value);
                break;
            default:
                options.put(option, value);
                childOptions.put(option, value);
        }
        return this;
    }

    public ChannelOptionBuilder buildWithDefaultOptions(){
        options.put(SO_KEEPALIVE, soKeepAlive);
        options.put(SO_BACKLOG, soBackLog);
        options.put(TCP_NODELAY, tcpNoDelay);
        options.put(SO_REUSEADDR, soReuseAddr);
        options.put(SO_LINGER, soLinger);
        options.put(CONNECT_TIMEOUT_MILLIS, connectTimeOutMillis);
        options.put(SO_RCVBUF, soRcvBuf);

        //For child
        childOptions.put(ALLOCATOR, allocator);
        childOptions.put(SO_RCVBUF, soRcvBuf);
        childOptions.put(SO_REUSEADDR, soReuseAddr);
        return this;
    }

    public void build(ServerBootstrap serverBootstrap){
        options.forEach(serverBootstrap::option);
        childOptions.forEach(serverBootstrap::childOption);
    }

    public enum OptionType{
        Parent,
        Child,
        Both
    }

}