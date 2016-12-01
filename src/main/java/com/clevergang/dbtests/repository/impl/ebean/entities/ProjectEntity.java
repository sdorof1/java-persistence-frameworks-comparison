package com.clevergang.dbtests.repository.impl.ebean.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "project")
public class ProjectEntity {

  @Id
  @GeneratedValue
  private Integer pid;

  @Column
  private String name;

  @Column(name = "datestarted")
  private LocalDate dateStarted;

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

  public LocalDate getDateStarted() {
    return dateStarted;
  }

  public void setDateStarted(LocalDate dateStarted) {
    this.dateStarted = dateStarted;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProjectEntity project = (ProjectEntity) o;
    return Objects.equals(pid, project.pid) &&
            Objects.equals(name, project.name) &&
            Objects.equals(dateStarted, project.dateStarted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pid, name, dateStarted);
  }

  @Override
  public String toString() {
    return "Project{" +
            "pid=" + pid +
            ", name='" + name + '\'' +
            ", dateStarted=" + dateStarted +
            '}';
  }
}
