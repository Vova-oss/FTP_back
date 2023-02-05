package com.example.demo.Entity;

import com.example.demo.Entity.Enum.ERoles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "os_role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = ":id Роли", name = "id", required = true, example = "13")
    private Long id;

    @ApiModelProperty(notes = "Роль (ERoles)", name = "role", required = true, example = "USER")
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private ERoles role;


    @ApiModelProperty(notes = "Пользователи, к которому относится данная роль", name = "name")
    @JsonIgnore
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<UserEntity> userEntities;

}
