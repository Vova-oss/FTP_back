package com.example.demo.Entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@Table(name = "os_role")
public class Role {

    @Id
    @ApiModelProperty(notes = ":id Роли", name = "id", required = true, example = "13")
    private Long id;

    @ApiModelProperty(notes = "Роль (ERoles)", name = "role", required = true, example = "USER")
    @Column(value = "role")
    private String role;

}
