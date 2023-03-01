package com.example.demo.Entity;

import com.example.demo.Entity.Enum.ERoles;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@Table(name = "os_users")
public class UserEntity implements UserDetails {

    @Id
    @ApiModelProperty(notes = ":id Пользователя", name = "id", required = true, example = "13")
    private Long id;

    @ApiModelProperty(notes = "Email пользователя", name = "telephoneNumber", required = true, example = "first@mail.ru")
    @NumberFormat
    @NotNull
    @Column(value = "telephone_number")
    private String username;


    @ApiModelProperty(notes = "Пароль пользователя", name = "password", required = true,
            example = "$2a$12$4kMSRkNGisnBLGs1l28ZVum9dzm.xKeBftE/Vr7MsbypMH9sPXyH.")
    @NotBlank(message = "Некорректный формат password")
    @Column(value = "password")
    private String password;

    @Column(value = "fio")
    private String FIO;

    @Column(value = "verification")
    private Boolean verification;

    @Nullable
    @Column(value = "is_man")
    private Boolean isMan;

    @Nullable
    @Column(value = "time_of_creation")
    private Long timeOfCreation;

    @Nullable
    @Column(value = "code")
    private Integer code;

    @Column
    private ERoles role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
