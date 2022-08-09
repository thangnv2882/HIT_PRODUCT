package com.example.strawberry.domain.entity;

import com.example.strawberry.domain.entity.base.AbstractAuditingEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends AbstractAuditingEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    @Nationalized
    private String firstName;

    @Nationalized
    private String lastName;

    @Nationalized
    private String fullName;

    private String email;

    private String password;

    @Nationalized
    private String gender;

    @Nationalized
    private String address;

    private String birthday;

    @Nationalized
    private String biography;

    private String linkAvt;

    private String linkCover;

    private String code;

    //    link to table post
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
//    @JsonIgnore
    private Set<Post> posts = new HashSet<>();

    //    link to table comment
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private Set<Comment> comments = new HashSet<>();

    //    link to table reaction
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private Set<Reaction> reactions = new HashSet<>();

    //    link to table user_group_detail
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private Set<UserGroup> userGroups = new HashSet<>();

    //    link to table notification
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private Set<Notification> notifications = new HashSet<>();

}
