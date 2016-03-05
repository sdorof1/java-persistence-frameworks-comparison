package com.clevergang.dbtests.model;

import java.math.BigDecimal;

/**
 * @author Bretislav Wajtr <bretislav.wajtr@clevergang.com>
 */
public class Product {

    private Integer pid;
    private Integer category_pid;
    private String name;
    private String description;
    private BigDecimal price;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getCategory_pid() {
        return category_pid;
    }

    public void setCategory_pid(Integer category_pid) {
        this.category_pid = category_pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Product product = (Product) o;

        if (pid != null ? !pid.equals(product.pid) : product.pid != null)
            return false;
        if (category_pid != null ? !category_pid.equals(product.category_pid) : product.category_pid != null)
            return false;
        if (name != null ? !name.equals(product.name) : product.name != null)
            return false;
        if (description != null ? !description.equals(product.description) : product.description != null)
            return false;
        return price != null ? price.equals(product.price) : product.price == null;

    }

    @Override
    public int hashCode() {
        int result = pid != null ? pid.hashCode() : 0;
        result = 31 * result + (category_pid != null ? category_pid.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Product{" +
            "pid=" + pid +
            ", category_pid=" + category_pid +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", price=" + price +
            '}';
    }
}
