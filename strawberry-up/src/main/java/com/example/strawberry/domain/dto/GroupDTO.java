package com.example.strawberry.domain.dto;

import com.example.strawberry.adapter.web.base.AccessType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupDTO {

    private String name;
    private AccessType access;

}
