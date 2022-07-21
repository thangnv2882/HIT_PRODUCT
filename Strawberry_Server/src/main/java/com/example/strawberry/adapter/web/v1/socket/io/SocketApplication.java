//package com.example.strawberry.adapter.web.v1.socket.io;
//
//import com.corundumstudio.socketio.Configuration;
//import com.corundumstudio.socketio.SocketIOServer;
//import com.corundumstudio.socketio.Transport;
//import com.example.strawberry.application.constants.SocketIOConstant;
//import com.example.strawberry.application.service.ICommentService;
//import com.example.strawberry.application.service.IPostService;
//import com.example.strawberry.application.service.Impl.PostServiceImpl;
//import com.example.strawberry.domain.entity.Comment;
//import org.json.simple.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.Set;
//
////@Component
//public class SocketApplication {
//
//    public static String ROOM;
//    public static void startAplication () {
//        Configuration configuration = new Configuration();
//        configuration.setHostname(SocketIOConstant.HOST);
//        configuration.setPort(SocketIOConstant.PORT);
//        configuration.setTransports(Transport.POLLING);
//
//        final SocketIOServer socketIOServer= new SocketIOServer(configuration);
//
//        socketIOServer.addEventListener("client-send-room", JSONObject.class, (client, data, ackRequest)->{
//            System.out.println(data);
//            client.joinRoom(data);
//            ROOM = data.toString();
//        });
//
//        socketIOServer.addEventListener("client-leave-room", JSONObject.class, (client, data, ackRequest)->{
////            client.leaveRoom(data);
//        });
//
//        socketIOServer.addEventListener("client-send-message", JSONObject.class, (client, data, ackRequest)->{
//            JSONObject jsonObject = (JSONObject) data;
//            System.out.println(jsonObject);
//            //one to room
//            String roomNameString= client.getAllRooms().toArray()[1].toString();
//            System.out.println(roomNameString);
////            System.out.println(ROOM);
//            socketIOServer.getRoomOperations(roomNameString).sendEvent("server-send-message", data);
//        });
//
//        socketIOServer.addEventListener("client-send-comment", Long.class, (client, dataIdPost, ackRequest)->{
//            System.out.println(dataIdPost);
//            String roomNameString= client.getAllRooms().toArray()[1].toString();
//            System.out.println(dataIdPost);
//            Set<Comment> comments = PostServiceImpl.getAllCommentByIdPost(dataIdPost);
//            socketIOServer.getRoomOperations(roomNameString).sendEvent("server-send-message", comments);
//
//        });
//
//
//        socketIOServer.start();
//    }
//}
