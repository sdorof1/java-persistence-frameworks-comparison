package com.clevergang.dbtests.repository.impl.ebean.entities;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author Bretislav Wajtr
 */
@Entity
@Table(name = "department")
public class DepartmentEntity {

    @Id
    @GeneratedValue
    private Integer pid;

    @Column(name="company_pid")
    private Integer companyPid;

    @Column
    private String name;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getCompanyPid() {
        return companyPid;
    }

    public void setCompanyPid(Integer companyPid) {
        this.companyPid = companyPid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepartmentEntity that = (DepartmentEntity) o;
        return Objects.equals(pid, that.pid) &&
                Objects.equals(companyPid, that.companyPid) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pid, companyPid, name);
    }

    @Override
    public String toString() {
        return "DepartmentEntity{" +
                "pid=" + pid +
                ", companyPid=" + companyPid +
                ", name='" + name + '\'' +
                '}';
    }
}
