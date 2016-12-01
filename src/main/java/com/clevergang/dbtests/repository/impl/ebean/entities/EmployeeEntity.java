package com.clevergang.dbtests.repository.impl.ebean.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "employee")
public class EmployeeEntity {

    @Id
    @GeneratedValue
    private Integer pid;

    @Column(name = "department_pid")
    private Integer departmentPid;

    @Column
    private String name;

    @Column
    private String surname;

    @Column
    private String email;

    @Column
    private BigDecimal salary;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getDepartmentPid() {
        return departmentPid;
    }

    public void setDepartmentPid(Integer departmentPid) {
        this.departmentPid = departmentPid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeEntity employee = (EmployeeEntity) o;
        return Objects.equals(pid, employee.pid) &&
                Objects.equals(departmentPid, employee.departmentPid) &&
                Objects.equals(name, employee.name) &&
                Objects.equals(surname, employee.surname) &&
                Objects.equals(email, employee.email) &&
                Objects.equals(salary, employee.salary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pid, departmentPid, name, surname, email, salary);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "pid=" + pid +
                ", departmentPid=" + departmentPid +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", salary=" + salary +
                '}';
    }
}
