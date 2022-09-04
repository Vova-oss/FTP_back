package com.example.demo.Security.Entity;

import com.example.demo.Entity.UserEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "os_refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = ":id токена", name = "id", required = true, example = "5")
    private Long id;


    @ApiModelProperty(notes = "Пользователь, которому принадлежит данный токен", name = "userEntity", required = true)
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;


    @ApiModelProperty(notes = "Токен", name = "token", required = true)
    @Column(nullable = false, unique = true)
    private String token;


    @ApiModelProperty(notes = "Дата, после которой токен перестанет действовать", name = "expiry_date", required = true)
    @Column(nullable = false, name = "expiry_date")
    private Instant expiryDate;

}
