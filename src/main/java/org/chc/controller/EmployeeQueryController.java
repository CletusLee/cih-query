package org.chc.controller;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.chc.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@RestController()
@EnableWebMvc
@RequestMapping("/employee")
public class EmployeeQueryController {

    private Logger logger = LoggerFactory.getLogger(EmployeeQueryController.class);
    private static String jdbcConnection = System.getenv("jdbcConnection");
    private static String userName = System.getenv("userName");
    private static String password = System.getenv("password");

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable String id) throws SQLException, IOException {
        logger.info("Get the employee with id = " + id);
        ConnectionSource connectionSource = new JdbcConnectionSource(jdbcConnection, userName, password);
        Dao<Employee, String> employeeDao = DaoManager.createDao(connectionSource, Employee.class);
        Employee employee = employeeDao.queryForId(id);
        connectionSource.close();

        if (employee == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(employee, HttpStatus.OK);
        }
    }

    @GetMapping
    public List<Employee> getAllEmployee(@RequestParam(required = false) String name) throws SQLException, IOException {
        ConnectionSource connectionSource = new JdbcConnectionSource(jdbcConnection, userName, password);
        Dao<Employee, String> employeeDao = DaoManager.createDao(connectionSource, Employee.class);
        List<Employee> employees = employeeDao.queryForAll();
        connectionSource.close();
        if (name == null) {
            return employees;
        } else {
            return employees.stream().filter(employee -> name.equalsIgnoreCase(employee.getName())).collect(Collectors.toList());
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Employee> getEmployeeWithName(@PathVariable String name) throws SQLException, IOException {
        logger.info("Get the employee with name = " + name);
        ConnectionSource connectionSource = new JdbcConnectionSource(jdbcConnection, userName, password);
        Dao<Employee, String> employeeDao = DaoManager.createDao(connectionSource, Employee.class);
        List<Employee> employees = employeeDao.queryForEq("name", name);
        connectionSource.close();

        if (employees.size() == 0) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(employees.get(0), HttpStatus.OK);
        }
    }

}
