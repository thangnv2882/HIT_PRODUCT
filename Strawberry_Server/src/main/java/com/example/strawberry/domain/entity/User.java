package com.example.strawberry.domain.entity;

import com.example.strawberry.adapter.web.base.AuthenticationProvider;
import com.example.strawberry.domain.entity.base.AbstractAuditingEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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
    @NotBlank
    private String firstName;

    @Nationalized
    @NotBlank
    private String lastName;

    //    @NotBlank
    @Nationalized
    private String fullName;

    //    @NotBlank
    private String email;

    //    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String password;

    @Nationalized
    private String gender;

    @Nationalized
    private String address;

    private String birthday;

    @Nationalized
    @Length(max = 10000)
    private String biography;

    private String linkAvt;

    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider")
    private AuthenticationProvider authProvider;


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

    /*
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "user_room",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id"))
    @JsonIgnore
    private Set<RoomChat> roomChats = new HashSet<>();
     */

}
