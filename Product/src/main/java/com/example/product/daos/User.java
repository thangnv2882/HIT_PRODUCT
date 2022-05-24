package com.example.product.daos;

import com.example.product.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;

    @Nationalized
    @NotBlank
    private String fullName;

    @NotBlank
    private String gender;

    @NotBlank
    private String phoneNumber;

    @Nationalized
    @NotBlank
    private String address;

    @NotBlank
    private String birthday;

    @Nationalized
    @NotBlank
    private String biography;

    @NotBlank
    private String linkAvt;

    private String codeActive;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(idUser, user.idUser) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && Objects.equals(fullName, user.fullName) && Objects.equals(gender, user.gender) && Objects.equals(phoneNumber, user.phoneNumber) && Objects.equals(address, user.address) && Objects.equals(birthday, user.birthday) && Objects.equals(biography, user.biography) && Objects.equals(linkAvt, user.linkAvt) && Objects.equals(codeActive, user.codeActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, username, password, email, fullName, gender, phoneNumber, address, birthday, biography, linkAvt, codeActive);
    }
}
