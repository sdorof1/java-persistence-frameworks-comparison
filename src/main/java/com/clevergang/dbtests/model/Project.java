package com.clevergang.dbtests.model;

import java.time.LocalDate;

/**
 * @author Bretislav Wajtr <bretislav.wajtr@clevergang.com>
 */
public class Project {
    private Integer pid;
    private String name;
    private LocalDate date;

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Project project = (Project) o;

        if (pid != null ? !pid.equals(project.pid) : project.pid != null)
            return false;
        if (name != null ? !name.equals(project.name) : project.name != null)
            return false;
        return date != null ? date.equals(project.date) : project.date == null;

    }

    @Override
    public int hashCode() {
        int result = pid != null ? pid.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Project{" +
            "pid=" + pid +
            ", name='" + name + '\'' +
            ", date=" + date +
            '}';
    }
}
