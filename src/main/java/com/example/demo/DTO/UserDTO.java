package com.example.demo.DTO;

import com.example.demo.Entity.UserEntity;
import org.springframework.security.core.userdetails.User;

public class UserDTO {

    private String fio;
    private Boolean isMan;
    private String telephoneNumber;

    public static UserDTO createUserDTO(UserEntity userEntity){
        UserDTO userDTO = new UserDTO();
        userDTO.fio = userEntity.getFIO();
        userDTO.isMan = userEntity.getIsMan();
        userDTO.telephoneNumber = userEntity.getTelephoneNumber();
        return userDTO;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public Boolean getMan() {
        return isMan;
    }

    public void setMan(Boolean man) {
        isMan = man;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }
}
