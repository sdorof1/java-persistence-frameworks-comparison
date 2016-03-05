package com.clevergang.dbtests.model;

import java.math.BigDecimal;

/**
 * @author Bretislav Wajtr <bretislav.wajtr@clevergang.com>
 */
public class Employee {

    private Integer pid;
    private Integer company_pid;
    private String name;
    private String surname;
    private String mail;
    private BigDecimal salary;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getCompany_pid() {
        return company_pid;
    }

    public void setCompany_pid(Integer company_pid) {
        this.company_pid = company_pid;
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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
        if (company_pid != null ? !company_pid.equals(employee.company_pid) : employee.company_pid != null)
            return false;
        if (name != null ? !name.equals(employee.name) : employee.name != null)
            return false;
        if (surname != null ? !surname.equals(employee.surname) : employee.surname != null)
            return false;
        if (mail != null ? !mail.equals(employee.mail) : employee.mail != null)
            return false;
        return salary != null ? salary.equals(employee.salary) : employee.salary == null;

    }

    @Override
    public int hashCode() {
        int result = pid != null ? pid.hashCode() : 0;
        result = 31 * result + (company_pid != null ? company_pid.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (mail != null ? mail.hashCode() : 0);
        result = 31 * result + (salary != null ? salary.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Employee{" +
            "pid=" + pid +
            ", company_pid=" + company_pid +
            ", name='" + name + '\'' +
            ", surname='" + surname + '\'' +
            ", mail='" + mail + '\'' +
            ", salary=" + salary +
            '}';
    }
}
