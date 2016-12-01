package com.clevergang.dbtests.repository.api.data;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author Bretislav Wajtr
 */
public class Employee {

    private Integer pid;
    private Integer department_pid;
    private String name;
    private String surname;
    private String email;
    private BigDecimal salary;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getDepartmentPid() {
        return department_pid;
    }

    public void setDepartmentPid(Integer department_pid) {
        this.department_pid = department_pid;
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
        Employee employee = (Employee) o;
        return Objects.equals(pid, employee.pid) &&
                Objects.equals(department_pid, employee.department_pid) &&
                Objects.equals(name, employee.name) &&
                Objects.equals(surname, employee.surname) &&
                Objects.equals(email, employee.email) &&
                Objects.equals(salary, employee.salary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pid, department_pid, name, surname, email, salary);
    }

    @Override
    public String toString() {
        return "Employee{" +
            "pid=" + pid +
            ", department_pid=" + department_pid +
            ", name='" + name + '\'' +
            ", surname='" + surname + '\'' +
            ", email='" + email + '\'' +
            ", salary=" + salary +
            '}';
    }
}
