package com.clevergang.dbtests.model;

import java.math.BigDecimal;

/**
 * @author Bretislav Wajtr <bretislav.wajtr@clevergang.com>
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
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Employee employee = (Employee) o;

        if (pid != null ? !pid.equals(employee.pid) : employee.pid != null)
            return false;
        if (department_pid != null ? !department_pid.equals(employee.department_pid) : employee.department_pid != null)
            return false;
        if (name != null ? !name.equals(employee.name) : employee.name != null)
            return false;
        if (surname != null ? !surname.equals(employee.surname) : employee.surname != null)
            return false;
        if (email != null ? !email.equals(employee.email) : employee.email != null)
            return false;
        return salary != null ? salary.equals(employee.salary) : employee.salary == null;

    }

    @Override
    public int hashCode() {
        int result = pid != null ? pid.hashCode() : 0;
        result = 31 * result + (department_pid != null ? department_pid.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (salary != null ? salary.hashCode() : 0);
        return result;
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
