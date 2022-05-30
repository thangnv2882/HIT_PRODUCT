package com.example.strawberry.domain.entity;

import com.example.strawberry.domain.entity.base.AbstractAuditingEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserRegister extends AbstractAuditingEntity {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String fullName;

    @NotBlank
    private String password;

    private String email;

    private String phoneNumber;

    private String code;

}