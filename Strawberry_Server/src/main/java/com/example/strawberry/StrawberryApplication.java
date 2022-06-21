package com.example.strawberry;

import com.corundumstudio.socketio.SocketIOServer;
//import com.example.strawberry.adapter.web.v1.socket.io.SocketApplication;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class StrawberryApplication implements CommandLineRunner {

//    private final SocketIOServer server;
//
//    public StrawberryApplication(SocketIOServer server) {
//        this.server = server;
//    }
//    @Override
//    public void run(String... args) throws Exception {
//        server.start();
//    }

    public static void main(String[] args) {
        SpringApplication.run(StrawberryApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        SocketApplication.startAplication();
    }
}
