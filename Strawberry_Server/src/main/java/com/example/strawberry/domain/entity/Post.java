package com.example.strawberry.domain.entity;

import com.example.strawberry.adapter.web.base.AccessType;
import com.example.strawberry.adapter.web.base.ReactionType;
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
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
public class Post extends AbstractAuditingEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPost;

    //    @NotBlank
    @Nationalized
    @Length(max = 10000)
    private String contentPost;

    //    @NotBlank
//    @Length(max = 10000)
//    private String slugPost;

//    @NotBlank
    private AccessType access;

//    link to table user
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

//    link to table reaction
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "post")
    @JsonIgnore
    private Set<Reaction> reactions = new HashSet<>();

//    link to table group
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private Group group;

//    link to table image
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "post")
    @JsonIgnore
    private Set<Image> images = new HashSet<>();

//    link to table video
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "post")
    @JsonIgnore
    private Set<Video> videos = new HashSet<>();

//    link to table comment
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "post")
    @JsonIgnore
    private Set<Comment> comments = new HashSet<>();

}
