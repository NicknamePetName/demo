package com.example.demo.dataobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDO implements Serializable {

    private Integer id;
    private String userName;
    private String password;
    private String nickName;
    private String email;
    private String phone;
    private String avatar;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;


}
