package com.example.strawberry.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "user_group")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserGroup {
//    @EmbeddedId
//    private UserGroupKey id;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUserGroup;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private Group group;

    private Long idUserCreated;
}
