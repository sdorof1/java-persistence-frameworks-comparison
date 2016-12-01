package com.clevergang.dbtests.repository.impl.ebean.entities;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author Bretislav Wajtr <bretislav.wajtr@ibacz.eu>
 */
@Entity
@Table(name = "company")
public class CompanyEntity {

    @Id
    @GeneratedValue
    private Integer pid;

    @Column
    private String name;

    @Column
    private String address;

    public Integer getPid() {
        return pid;}

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyEntity that = (CompanyEntity) o;
        return Objects.equals(pid, that.pid) &&
                Objects.equals(name, that.name) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pid, name, address);
    }

    @Override
    public String toString() {
        return "CompanyEntity{" +
                "pid=" + pid +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
