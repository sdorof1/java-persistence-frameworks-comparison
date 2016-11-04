package com.clevergang.dbtests.repository.api.data;

import java.time.LocalDate;
import java.util.Objects;

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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(pid, project.pid) &&
                Objects.equals(name, project.name) &&
                Objects.equals(date, project.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pid, name, date);
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
