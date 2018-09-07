package com.pirate.esredisdemo.proxy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersSource;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * 代理服务器,开启Service会启动
 * @author fyn
 * @version 1.0 2018/09/04
 */
//@Service
public class ProxyServer {

    private List<HttpProxyServer> proxyServerList = new ArrayList<>();

    private HttpFiltersSource filtersSource = new HttpFiltersSource() {
        @Override
        public HttpFilters filterRequest(HttpRequest httpRequest, ChannelHandlerContext context) {
            return new CustomHttpFilter(httpRequest, context);
        }

        @Override
        public int getMaximumRequestBufferSizeInBytes() {
            return 1024 * 1024;
        }

        @Override
        public int getMaximumResponseBufferSizeInBytes() {
            return 10 * 1024 * 1024;
        }
    };

    @PostConstruct
    public void startUpProxyServer() {
        //todo:设置证书

        int port = 8888;
        //启动代理服务器
        HttpProxyServer server = DefaultHttpProxyServer.bootstrap()
                .withAddress(new InetSocketAddress(port))
                .withFiltersSource(filtersSource)
                .withAllowRequestToOriginServer(true)
                .withConnectTimeout(5 * 1000)
                .withAuthenticateSslClients(false)
                .start();
        proxyServerList.add(server);
        System.err.println(">>>>>>>>>>>>>>代理服务器启动在端口: " + port);
    }

    @PreDestroy
    public void shutDownProxyServer() {
        if (!proxyServerList.isEmpty()) {
            proxyServerList.forEach(e->{
                e.abort();
                System.err.println(">>>>>>>>>>>>>>代理服务器关闭在端口:" +e.getListenAddress().getPort());
            });
        }
    }

}
