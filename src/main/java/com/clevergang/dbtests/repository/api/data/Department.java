package com.clevergang.dbtests.repository.api.data;

import java.util.Objects;

/**
 * @author Bretislav Wajtr
 */
@SuppressWarnings("unused")
public class Department {
    private Integer pid;
    private Integer company_pid;
    private String name;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCompanyPid() {
        return company_pid;
    }

    public void setCompanyPid(Integer company_pid) {
        this.company_pid = company_pid;
    }

    public Department() {
        // Actually jOOQ requires default constructor here
    }

    public Department(Integer company_pid, String name) {
        this.company_pid = company_pid;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(pid, that.pid) &&
                Objects.equals(company_pid, that.company_pid) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pid, company_pid, name);
    }

    @Override
    public String toString() {
        return "Department{" +
            "pid=" + pid +
            ", company_pid=" + company_pid +
            ", name='" + name + '\'' +
            '}';
    }
}

