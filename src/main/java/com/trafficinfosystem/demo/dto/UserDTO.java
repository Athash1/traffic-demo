package com.trafficinfosystem.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class UserDTO {
    private String id;//创建账户时可选

    private String username;

    private String name;

    private String phone;

    private String sex;

    private Integer status;//账户状态,0:正常,1:禁用

}
