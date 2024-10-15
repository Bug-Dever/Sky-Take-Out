package com.OrderMate.service;

import com.OrderMate.dto.EmployeeDTO;
import com.OrderMate.dto.EmployeeLoginDTO;
import com.OrderMate.dto.EmployeePageQueryDTO;
import com.OrderMate.entity.Employee;
import com.OrderMate.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    // 新增员工业务方法
    void save(EmployeeDTO employeeDTO);

    // 分页查询方法
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    // 启用禁用员工账号
    void startOrStop(Integer status, Long id);

    // 根据id查询员工信息
    Employee getById(Long id);

    // 编辑员工信息
    void update(EmployeeDTO employeeDTO);
}
