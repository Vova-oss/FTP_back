package com.example.demo.Entity;

import com.example.demo.Security.Entity.RefreshToken;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.dialect.Ingres9Dialect;
import org.jetbrains.annotations.Nullable;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@Table(name = "os_users",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "telephoneNumber")
        })
public class UserEntity {

    @Id
    @ApiModelProperty(notes = ":id Пользователя", name = "id", required = true, example = "13")
    private Long id;

    @ApiModelProperty(notes = "Email пользователя", name = "telephoneNumber", required = true, example = "first@mail.ru")
    @NumberFormat
    @NotNull
    @Column(name = "telephoneNumber")
    private String telephoneNumber;


    @ApiModelProperty(notes = "Пароль пользователя", name = "password", required = true,
            example = "$2a$12$4kMSRkNGisnBLGs1l28ZVum9dzm.xKeBftE/Vr7MsbypMH9sPXyH.")
    @NotBlank(message = "Некорректный формат password")
    @Column(name = "password")
    private String password;

    private String FIO;

    private Boolean verification;

    @Nullable
    private Boolean isMan;

    @Nullable
    private Long timeOfCreation;

    @Nullable
    private Integer code;

}
