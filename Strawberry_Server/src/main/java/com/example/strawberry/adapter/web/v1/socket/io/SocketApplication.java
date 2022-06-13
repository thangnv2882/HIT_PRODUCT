package com.example.strawberry.adapter.web.v1.socket.io;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.Transport;
import com.example.strawberry.application.constants.SocketIOConstant;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//@Component
public class SocketApplication {

    public static void startAplication () {
        Configuration configuration = new Configuration();
        configuration.setHostname(SocketIOConstant.HOST);
        configuration.setPort(SocketIOConstant.PORT);
        configuration.setTransports(Transport.POLLING);

        final SocketIOServer socketIOServer= new SocketIOServer(configuration);

        socketIOServer.addEventListener("client-send-room", JSONObject.class, (client, data, ackRequest)->{
            System.out.println(data);
            client.sendEvent("reply-client", "lại nè");
//            client.joinRoom(data);
//            ROOM = data.toString();
        });

        socketIOServer.addEventListener("client-leave-room", JSONObject.class, (client, data, ackRequest)->{
//            client.leaveRoom(data);
        });

        socketIOServer.addEventListener("client-send-message", JSONObject.class, (client, data, ackRequest)->{
            JSONObject jsonObject = (JSONObject) data;
            System.out.println(jsonObject);
            //one to room
            String roomNameString= client.getAllRooms().toArray()[1].toString();
            System.out.println(roomNameString);
//            System.out.println(ROOM);
            socketIOServer.getRoomOperations(roomNameString).sendEvent("server-send-message", data);
        });

        socketIOServer.start();
    }
}
