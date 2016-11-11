package com.clevergang.dbtests.repository.api.data;

import java.util.Objects;

/**
 * @author Bretislav Wajtr <bretislav.wajtr@ibacz.eu>
 */
public class RegisterEmployeeOutput {
    private Integer companyPid;
    private Integer departmentPid;
    private Integer employeePid;

    public Integer getCompanyPid() {
        return companyPid;
    }

    public void setCompanyPid(Integer companyPid) {
        this.companyPid = companyPid;
    }

    public Integer getDepartmentPid() {
        return departmentPid;
    }

    public void setDepartmentPid(Integer departmentPid) {
        this.departmentPid = departmentPid;
    }

    public Integer getEmployeePid() {
        return employeePid;
    }

    public void setEmployeePid(Integer employeePid) {
        this.employeePid = employeePid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisterEmployeeOutput that = (RegisterEmployeeOutput) o;
        return Objects.equals(companyPid, that.companyPid) &&
                Objects.equals(departmentPid, that.departmentPid) &&
                Objects.equals(employeePid, that.employeePid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyPid, departmentPid, employeePid);
    }

    @Override
    public String toString() {
        return "RegisterEmployeeOutput{" +
                "companyPid=" + companyPid +
                ", departmentPid=" + departmentPid +
                ", employeePid=" + employeePid +
                '}';
    }
}
