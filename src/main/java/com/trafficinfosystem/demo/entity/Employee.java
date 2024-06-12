package com.trafficinfosystem.demo.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "employee")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    @NotBlank(message = "用户名不能为空")
    @Indexed(unique = true)
    private String username;
    @NotBlank(message = "姓名不能为空")
    private String name;
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "手机号不能为空")
    private String phone;
    @NotBlank(message = "性别不能为空")
    private String sex;
    @NotBlank(message = "状态不能为空")//状态: 0:禁用 1:启用
    private Integer status;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

}
