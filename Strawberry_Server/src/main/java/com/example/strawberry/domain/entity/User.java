package com.example.strawberry.domain.entity;

import com.example.strawberry.domain.entity.base.AbstractAuditingEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends AbstractAuditingEntity {

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


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private Set<Post> posts = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "user_group",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    @JsonIgnore
    private Set<Group> groups = new HashSet<>();


//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userRequest")
//    @JsonIgnore
//    private Set<FriendShip> friendsRequest;
//
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userResponse")
//    @JsonIgnore
//    private Set<FriendShip> friendsResponse;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(fullName, user.fullName) && Objects.equals(email, user.email) && Objects.equals(phoneNumber, user.phoneNumber) && Objects.equals(password, user.password) && Objects.equals(gender, user.gender) && Objects.equals(address, user.address) && Objects.equals(birthday, user.birthday) && Objects.equals(biography, user.biography) && Objects.equals(linkAvt, user.linkAvt) && Objects.equals(code, user.code) && Objects.equals(posts, user.posts) && Objects.equals(groups, user.groups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, fullName, email, phoneNumber, password, gender, address, birthday, biography, linkAvt, code, posts, groups);
    }
}
