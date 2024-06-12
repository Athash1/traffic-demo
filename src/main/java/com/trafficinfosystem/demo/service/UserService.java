package com.trafficinfosystem.demo.service;

import com.trafficinfosystem.demo.constant.MessageConstant;
import com.trafficinfosystem.demo.constant.PasswordConstant;
import com.trafficinfosystem.demo.constant.StatusConstant;
import com.trafficinfosystem.demo.dto.*;
import com.trafficinfosystem.demo.entity.Employee;
import com.trafficinfosystem.demo.entity.User;
import com.trafficinfosystem.demo.exception.AccountLockedException;
import com.trafficinfosystem.demo.exception.AccountNotFoundException;
import com.trafficinfosystem.demo.exception.LoginFailedException;
import com.trafficinfosystem.demo.exception.PasswordErrorException;
import com.trafficinfosystem.demo.repositories.UserRepository;
import com.trafficinfosystem.demo.result.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DynamicUpdateService dynamicUpdateService;

    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * 员工登录
     *
     * @param userLoginDTO
     * @return
     */
    public User login(UserLoginDTO userLoginDTO) {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据 (Query data in the database based on username)
        User user = userRepository.findByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        // (Handle various abnormal situations (username does not exist, password is incorrect, account is locked))
        if (user == null) {
            //账号不存在 (Account does not exist)
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对 (Password comparison)
        //Handle passwords with BCrypt
        //This is the encrypted password that you retrieve from the database
        String storedEncryptedPassword = user.getPassword();

        // The password that the user entered when they tried to log in
        String attemptedPassword = password;

        // Check whether the password you entered matches the encrypted password you stored
        //attemptedPasswordit it's a plaintext password，storedEncryptedPasswordit it's a cipher code
        boolean isMatch = passwordEncoder.matches(attemptedPassword, storedEncryptedPassword);

        if (!isMatch) {
            //密码错误 (wrong password)
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (user.getStatus() == StatusConstant.DISABLE) {
            //The account is locked
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、Returns the solid object
        return user;
    }

    /**
     * Add new employees
     * @param userDTO
     */
    public void save(UserDTO userDTO) {
        User user = new User();

        //Object attribute copy
        BeanUtils.copyProperties(userDTO, user);

        //Set the account status, default normal status （1 means normal, 0 means locked）
        user.setStatus(StatusConstant.ENABLE);

        //Set password, default password is 123456
        user.setPassword(passwordEncoder.encode(PasswordConstant.DEFAULT_PASSWORD_USER));

        userRepository.insert(user);
    }

    /**
     * Paging query
     * @param userPageQueryDTO
     * @return
     */
    public PageResult pageQuery(UserPageQueryDTO userPageQueryDTO) {
        Pageable pageable = PageRequest.of(userPageQueryDTO.getPage() - 1, userPageQueryDTO.getPageSize(), Sort.by("createTime").descending());
        Page<User> page = userRepository.findByNameLike(userPageQueryDTO.getName(), pageable);

        return new PageResult(page.getTotalElements(), page.getContent());
    }
}
