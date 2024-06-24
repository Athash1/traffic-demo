package com.trafficinfosystem.demo.service;

import com.trafficinfosystem.demo.apiManage.DynamicUpdateService;
import com.trafficinfosystem.demo.constant.MessageConstant;
import com.trafficinfosystem.demo.constant.PasswordConstant;
import com.trafficinfosystem.demo.constant.StatusConstant;
import com.trafficinfosystem.demo.context.BaseContext;
import com.trafficinfosystem.demo.dto.EmployeeDTO;
import com.trafficinfosystem.demo.dto.EmployeeLoginDTO;
import com.trafficinfosystem.demo.dto.EmployeePageQueryDTO;
import com.trafficinfosystem.demo.dto.EmployeePasswordDTO;
import com.trafficinfosystem.demo.entity.Employee;
import com.trafficinfosystem.demo.exception.AccountLockedException;
import com.trafficinfosystem.demo.exception.AccountNotFoundException;
import com.trafficinfosystem.demo.exception.PasswordErrorException;
import com.trafficinfosystem.demo.repositories.EmployeeRepository;
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
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DynamicUpdateService dynamicUpdateService;

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserRepository userRepository;

    /**
     * employeeLogin
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据 (Query data in the database based on username)
        Employee employee = employeeRepository.findByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        // (Handle various abnormal situations (username does not exist, password is incorrect, account is locked))
        if (employee == null) {
            //账号不存在 (Account does not exist)
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对 (Password comparison)
        //Handle passwords with BCrypt
        //This is the encrypted password that you retrieve from the database
        String storedEncryptedPassword = employee.getPassword();

        // The password that the user entered when they tried to log in
        String attemptedPassword = password;

        // Check whether the password you entered matches the encrypted password you stored
        //attemptedPasswordit it's a plaintext password，storedEncryptedPasswordit it's a cipher code
        boolean isMatch = passwordEncoder.matches(attemptedPassword, storedEncryptedPassword);

        if (!isMatch) {
            //密码错误 (wrong password)
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //The account is locked
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、Returns the solid object
        return employee;
    }

    /**
     * Add new employees
     * @param employeeDTO
     */
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();

        //Object attribute copy
        BeanUtils.copyProperties(employeeDTO, employee);

        //Set the account status, default normal status （1 means normal, 0 means locked）
        employee.setStatus(StatusConstant.ENABLE);

        //Set password, default password is 123456
        employee.setPassword(passwordEncoder.encode(PasswordConstant.DEFAULT_PASSWORD));


        employeeRepository.insert(employee);
    }

    /**
     * Paging query
     * @param employeePageQueryDTO
     * @return
     */
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        Pageable pageable = PageRequest.of(employeePageQueryDTO.getPage() - 1, employeePageQueryDTO.getPageSize(), Sort.by("createTime").descending());
        Page<Employee> page = employeeRepository.findByNameLike(employeePageQueryDTO.getName(), pageable);

        return new PageResult(page.getTotalElements(), page.getContent());
    }

    /**
     * Enable Disabling Employee Accounts
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, String id) {
        // update employee set status = ? where id = ?
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().set("status", status);
        mongoTemplate.updateFirst(query, update, Employee.class);
    }

    /**
     * Query employee information based on ID
     * @param id
     * @return
     */
    public Employee getById(String id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        return employeeOptional.orElse(null);
    }

    /**
     * Edit employee information
     * @param employeeDTO
     */
    public void update(EmployeeDTO employeeDTO) {
        Map<String, Object> updates = new HashMap<>();

        if (employeeDTO.getName() != null) updates.put("name", employeeDTO.getName());
        if (employeeDTO.getUsername() != null) updates.put("username", employeeDTO.getUsername());
        if (employeeDTO.getPhone() != null) updates.put("phone", employeeDTO.getPhone());
        if (employeeDTO.getSex() != null) updates.put("sex", employeeDTO.getSex());
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(BaseContext.getCurrentId());

        // Explicitly update the time and user
        updates.put("updateTime", LocalDateTime.now());
        updates.put("updateUser", BaseContext.getCurrentId());
        dynamicUpdateService.updateFields(employeeDTO.getId(), updates, Employee.class);
    }

    /**
     * Update password
     * @param employeePasswordDTO
     */
    public void updatePassword(EmployeePasswordDTO employeePasswordDTO) {
        //Update your password
        String newPassword = passwordEncoder.encode(employeePasswordDTO.getPassword());
        Query query = new Query(Criteria.where("id").is(employeePasswordDTO.getId()));
        Update update = new Update().set("password", newPassword);
        mongoTemplate.updateFirst(query, update, Employee.class);
    }

    /**
     * Delete employee information based on username
     * @param username
     */
    public void deleteByUserName(String username) {
        userRepository.deleteByUsername(username);
    }
}
