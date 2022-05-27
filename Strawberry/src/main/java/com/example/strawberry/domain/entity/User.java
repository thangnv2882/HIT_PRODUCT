package com.example.strawberry.domain.entity;

import com.example.strawberry.domain.entity.base.AbstractAuditingEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

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

    @NotBlank
    private String username;

    @NotBlank
    private String password;

//    @NotBlank
    private String email;

    @Nationalized
    @NotBlank
    private String firstName;

    @Nationalized
    @NotBlank
    private String lastName;

    @Nationalized
//    @NotBlank
    private String fullName;

    @Nationalized
//    @NotBlank
    private String gender;

//    @NotBlank
    private String phoneNumber;

    @Nationalized
//    @NotBlank
    private String address;

//    @NotBlank
    private String birthday;

    @Nationalized
//    @NotBlank
    private String biography;

//    @NotBlank
    private String linkAvt;

    private String code;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private Set<Post> posts = new HashSet<>();

}
