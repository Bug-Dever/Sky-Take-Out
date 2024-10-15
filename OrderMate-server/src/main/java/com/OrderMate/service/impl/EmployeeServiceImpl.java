package com.OrderMate.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.OrderMate.constant.MessageConstant;
import com.OrderMate.constant.PasswordConstant;
import com.OrderMate.constant.StatusConstant;
import com.OrderMate.dto.EmployeeDTO;
import com.OrderMate.dto.EmployeeLoginDTO;
import com.OrderMate.dto.EmployeePageQueryDTO;
import com.OrderMate.entity.Employee;
import com.OrderMate.exception.AccountLockedException;
import com.OrderMate.exception.AccountNotFoundException;
import com.OrderMate.exception.PasswordErrorException;
import com.OrderMate.mapper.EmployeeMapper;
import com.OrderMate.result.PageResult;
import com.OrderMate.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // 对前端传来的明文密码进行加密处理
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     *
     * @param employeeDTO
     * @return
     */
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        // 对象属性拷贝
        BeanUtils.copyProperties(employeeDTO, employee);
        // 剩余的属性需要手动设置
        employee.setStatus(StatusConstant.ENABLE);
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
        // 使用ThreadLocal技术动态获取当前用户的id
//        employee.setCreateUser(BaseContext.getCurrentId());
//        employee.setUpdateUser(BaseContext.getCurrentId());

        // 调用持久层
        employeeMapper.insert(employee);
    }

    // 分页查询
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        // select * from employee limit 0,10 较繁琐
        // 开始分页查询 -》 使用PageHelper插件
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO); // 返回值是该插件固定的
        long total = page.getTotal();
        List<Employee> records = page.getResult();
        PageResult pageResult = new PageResult(total,records);
        return pageResult;
    }

    // 启用禁用员工账号
    public void startOrStop(Integer status, Long id) {
        // update employee set status = ? where id = ?
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();
        employeeMapper.update(employee);

    }

    // 根据id查询员工信息
    public Employee getById(Long id) {
        Employee employee = employeeMapper.getById(id);
        return employee;
    }

    // 修改员工信息
    public void update(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee); // 拷贝属性
        // 下两个属性是DTO中没的，补充完整
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.update(employee);
    }
}
