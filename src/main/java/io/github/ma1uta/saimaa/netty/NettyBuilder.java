/*
 * Copyright sablintolya@gmai.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.ma1uta.saimaa.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;

import java.net.URI;
import java.util.function.Consumer;
import javax.ws.rs.ProcessingException;

/**
 * Builder of the Netty channels.
 */
public class NettyBuilder {

    /**
     * Default http port.
     */
    public static final int DEFAULT_HTTP_PORT = 80;

    /**
     * Default https port.
     */
    public static final int DEFAULT_HTTPS_PORT = 443;

    /**
     * Default backlog value.
     */
    public static final int DEFAULT_BACKLOG = 2014;

    private NettyBuilder() {
        // singleton.
    }

    /**
     * Create and start Netty server.
     *
     * @param inetHost      Host to binding.
     * @param port          Port to binding.
     * @param initializer   Channel initializer.
     * @param closeListener Channel close listener.
     * @return Netty channel instance.
     * @throws ProcessingException when there is an issue with creating new container.
     */
    public static Channel createServer(String inetHost, int port, ChannelInitializer<?> initializer,
                                       Consumer<Future<? super Void>> closeListener)
        throws ProcessingException {

        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, DEFAULT_BACKLOG)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(initializer);

            Channel ch = bootstrap.bind(inetHost, port).sync().channel();

            ch.closeFuture().addListener(future -> {
                if (closeListener != null) {
                    closeListener.accept(future);
                }

                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            });

            return ch;
        } catch (InterruptedException e) {
            throw new ProcessingException(e);
        }
    }

    /**
     * Create and start Netty client.
     *
     * @param inetHost      Host to connecting.
     * @param port          Port to connecting.
     * @param initializer   Channel initializer.
     * @param closeListener Channel close listener.
     * @return Netty channel instance.
     * @throws ProcessingException when there is an issue with creating new client.
     */
    public static Channel createClient(String inetHost, int port, ChannelInitializer<?> initializer,
                                       Consumer<Future<? super Void>> closeListener) {

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(initializer);

            Channel ch = bootstrap.connect(inetHost, port).sync().channel();

            ch.closeFuture().addListener(future -> {
                if (closeListener != null) {
                    closeListener.accept(future);
                }

                workerGroup.shutdownGracefully();
            });

            return ch;
        } catch (InterruptedException e) {
            throw new ProcessingException(e);
        }
    }

    /**
     * Extract port from the uri.
     *
     * @param uri uri.
     * @return port.
     */
    public static int getPort(URI uri) {
        if (uri.getPort() == -1) {
            if ("http".equalsIgnoreCase(uri.getScheme())) {
                return DEFAULT_HTTP_PORT;
            } else if ("https".equalsIgnoreCase(uri.getScheme())) {
                return DEFAULT_HTTPS_PORT;
            }

            throw new IllegalArgumentException("URI scheme must be 'http' or 'https'.");
        }

        return uri.getPort();
    }
}
