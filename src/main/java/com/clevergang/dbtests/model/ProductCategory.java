package com.clevergang.dbtests.model;

/**
 * @author Bretislav Wajtr <bretislav.wajtr@clevergang.com>
 */
public class ProductCategory {

    private Integer pid;
    private String category;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ProductCategory that = (ProductCategory) o;

        if (pid != null ? !pid.equals(that.pid) : that.pid != null)
            return false;
        return category != null ? category.equals(that.category) : that.category == null;

    }

    @Override
    public int hashCode() {
        int result = pid != null ? pid.hashCode() : 0;
        result = 31 * result + (category != null ? category.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductCategory{" +
            "pid=" + pid +
            ", category='" + category + '\'' +
            '}';
    }
}
