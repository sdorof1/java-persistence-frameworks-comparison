package com.clevergang.dbtests.repository.impl.ebean.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "projectemployee")
public class ProjectEmployeeEntity {

  @Column(name = "project_pid")
  private Integer projectPid;

  @Column(name = "employee_pid")
  private Integer employeePid;

  public Integer getProjectPid() {
    return projectPid;
  }

  public void setProjectPid(Integer projectPid) {
    this.projectPid = projectPid;
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
    ProjectEmployeeEntity that = (ProjectEmployeeEntity) o;
    return Objects.equals(projectPid, that.projectPid) &&
            Objects.equals(employeePid, that.employeePid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(projectPid, employeePid);
  }

  @Override
  public String toString() {
    return "Projectemployee{" +
            "projectPid=" + projectPid +
            ", employeePid=" + employeePid +
            '}';
  }
}
