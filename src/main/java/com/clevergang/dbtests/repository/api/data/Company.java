package com.clevergang.dbtests.repository.api.data;

import java.util.Objects;

/**
 * @author Bretislav Wajtr <bretislav.wajtr@clevergang.com>
 */
@SuppressWarnings("unused")
public class Company {

    private Integer pid;
    private String name;
    private String address;

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
        Company company = (Company) o;
        return Objects.equals(pid, company.pid) &&
                Objects.equals(name, company.name) &&
                Objects.equals(address, company.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pid, name, address);
    }

    @Override
    public String toString() {
        return "Company{" +
            "pid=" + pid +
            ", name='" + name + '\'' +
            ", address='" + address + '\'' +
            '}';
    }
}
