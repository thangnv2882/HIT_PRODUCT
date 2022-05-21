package com.example.product.daos;

import com.example.product.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

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
}
