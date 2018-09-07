package com.pirate.esredisdemo.proxy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.littleshoot.proxy.HttpFiltersAdapter;

import java.io.UnsupportedEncodingException;

public class CustomHttpFilter extends HttpFiltersAdapter {
    public CustomHttpFilter(HttpRequest originalRequest, ChannelHandlerContext ctx) {
        super(originalRequest, ctx);
    }

    @Override
    public HttpResponse clientToProxyRequest(HttpObject httpObject) {
        try {
            ByteBuf buffer = Unpooled.wrappedBuffer("SUCCESS".getBytes("UTF-8"));
            DefaultFullHttpResponse response =
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buffer);
            HttpHeaders.setContentLength(response, buffer.readableBytes());
            HttpHeaders.setHeader(response, HttpHeaders.Names.CONTENT_TYPE, "text/html");
            return response;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return super.clientToProxyRequest(httpObject);
    }

    @Override
    public HttpObject proxyToClientResponse(HttpObject httpObject) {
        return super.proxyToClientResponse(httpObject);
    }
}
