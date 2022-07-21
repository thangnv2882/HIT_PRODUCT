//package com.example.strawberry.domain.entity;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.Nationalized;
//import org.hibernate.validator.constraints.Length;
//
//import javax.persistence.*;
//import java.sql.Timestamp;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "rooms")
//public class Message {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long idMessage;
//
//    private Long idUserSender;
//    private Long idUserReceiver;
//
//    @Nationalized
//    @Length(max = 10000)
//    private String content;
//
//    @CreationTimestamp
//    @Column(name = "created_at")
//    private Timestamp createdAt;
//
//    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
//    @JoinColumn(name = "room_id")
//    private RoomChat roomChat;
//
//
//
//}
