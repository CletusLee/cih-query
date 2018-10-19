package org.chc.controller;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.chc.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController()
@EnableWebMvc
@RequestMapping("/employee")
public class EmployeeQueryController {

    private Logger logger = LoggerFactory.getLogger(EmployeeQueryController.class);
    private static String jdbcConnection = System.getenv("jdbcConnection");
    private static String userName = System.getenv("userName");
    private static String password = System.getenv("password");

    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable String id) throws SQLException, IOException {
        logger.info("Get the employee with id = " + id);
        ConnectionSource connectionSource = new JdbcConnectionSource(jdbcConnection, userName, password);
        Dao<Employee, String> employeeDao = DaoManager.createDao(connectionSource, Employee.class);
        Employee employee = employeeDao.queryForId(id);
        connectionSource.close();
        return employee;
    }

    @GetMapping
    public List<Employee> getAllEmployee() throws SQLException, IOException {
        ConnectionSource connectionSource = new JdbcConnectionSource(jdbcConnection, userName, password);
        Dao<Employee, String> employeeDao = DaoManager.createDao(connectionSource, Employee.class);
        List<Employee> employees = employeeDao.queryForAll();
        connectionSource.close();
        return employees;
    }

}
