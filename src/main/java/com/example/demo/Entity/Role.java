package com.example.demo.Entity;

import com.example.demo.Entity.Enum.ERoles;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Table(name = "os_role")
public class Role {

    @Id
    @ApiModelProperty(notes = ":id Роли", name = "id", required = true, example = "13")
    private Long id;

    @ApiModelProperty(notes = "Роль (ERoles)", name = "role", required = true, example = "USER")
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private ERoles role;

}
