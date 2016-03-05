package com.clevergang.dbtests.model;

/**
 * @author Bretislav Wajtr <bretislav.wajtr@clevergang.com>
 */
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
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Company company = (Company) o;

        if (pid != null ? !pid.equals(company.pid) : company.pid != null)
            return false;
        if (name != null ? !name.equals(company.name) : company.name != null)
            return false;
        return address != null ? address.equals(company.address) : company.address == null;

    }

    @Override
    public int hashCode() {
        int result = pid != null ? pid.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
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
