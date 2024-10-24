package com.example.printer_management;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.unix.DomainSocketAddress;
import io.netty.channel.unix.ServerDomainSocketChannel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class UnixSocketConfiguration {

    @Bean
    public CommandLineRunner run() {
        return args -> {
            NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
            NioEventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup)
                        .channel(ServerDomainSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) {
                                // Customize your channel initialization if needed
                            }
                        });

                // Get the project root directory
                String projectRoot = System.getProperty("user.dir");
                Path socketPath = Paths.get(projectRoot, "printerapp.sock");

                // Delete existing socket file if it exists
                File socketFile = socketPath.toFile();
                if (socketFile.exists()) {
                    socketFile.delete();
                }

                ChannelFuture f = b.bind(new DomainSocketAddress(socketPath.toString())).sync();
                f.channel().closeFuture().sync();
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        };
    }
}
