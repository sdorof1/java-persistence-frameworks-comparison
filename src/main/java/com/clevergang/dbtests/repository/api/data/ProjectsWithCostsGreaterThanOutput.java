package com.clevergang.dbtests.repository.api.data;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author Bretislav Wajtr <bretislav.wajtr@ibacz.eu>
 */
public class ProjectsWithCostsGreaterThanOutput {

    private String projectName;
    private BigDecimal totalCost;
    private String companyName;
    private BigDecimal companyCost;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public BigDecimal getCompanyCost() {
        return companyCost;
    }

    public void setCompanyCost(BigDecimal companyCost) {
        this.companyCost = companyCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectsWithCostsGreaterThanOutput that = (ProjectsWithCostsGreaterThanOutput) o;
        return Objects.equals(projectName, that.projectName) &&
                Objects.equals(totalCost, that.totalCost) &&
                Objects.equals(companyName, that.companyName) &&
                Objects.equals(companyCost, that.companyCost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectName, totalCost, companyName, companyCost);
    }

    @Override
    public String toString() {
        return "ProjectsWithCostsGreaterThanOutput{" +
                "projectName='" + projectName + '\'' +
                ", totalCost=" + totalCost +
                ", companyName='" + companyName + '\'' +
                ", companyCost=" + companyCost +
                '}';
    }
}
