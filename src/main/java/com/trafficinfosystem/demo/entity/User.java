package com.trafficinfosystem.demo.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    //姓名
    private String name;
    //用户名
    private String username;
    //密码
    private String password;
    //手机号
    private String phone;
    //性别 0 女 1 男
    private String sex;
    //状态: 0:禁用 1:启用
    private Integer status;

    private String avatar;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private String createUser;

    private String updateUser;

    private LocalDateTime ReminderTime;

    //The total number of times a user used the service
    private Long totalServiceUsage = 0L;

    public void incrementServiceUsage() {
        if (totalServiceUsage == null) {
            totalServiceUsage = 0L;
        }
        totalServiceUsage++;
    }
}