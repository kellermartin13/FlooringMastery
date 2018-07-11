/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author Keller Martin
 */
public class Order {
    
    private Integer orderNum;
    private String customerName, state, productType;
    private BigDecimal taxRate, area, costPerSqFt, laborCostPerSqFt, 
            materialCost, laborCost, tax, total;
    private LocalDate dateNeeded;
    
//    public Order(Integer orderNum) {
//        this.orderNum = orderNum;
//    }
    
    public Order() {
        
    }
    
    public Order(Integer orderNum, String customerName, String state, 
            String productType, LocalDate dateNeeded, BigDecimal area) {
        this.orderNum = orderNum;
        this.customerName = customerName;
        this.state = state;
        this.productType = productType;
        this.dateNeeded = dateNeeded;
        this.area = area;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.orderNum);
        hash = 89 * hash + Objects.hashCode(this.customerName);
        hash = 89 * hash + Objects.hashCode(this.state);
        hash = 89 * hash + Objects.hashCode(this.productType);
        hash = 89 * hash + Objects.hashCode(this.taxRate);
        hash = 89 * hash + Objects.hashCode(this.area);
        hash = 89 * hash + Objects.hashCode(this.costPerSqFt);
        hash = 89 * hash + Objects.hashCode(this.laborCostPerSqFt);
        hash = 89 * hash + Objects.hashCode(this.materialCost);
        hash = 89 * hash + Objects.hashCode(this.laborCost);
        hash = 89 * hash + Objects.hashCode(this.tax);
        hash = 89 * hash + Objects.hashCode(this.total);
        hash = 89 * hash + Objects.hashCode(this.dateNeeded);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Order other = (Order) obj;
        if (!Objects.equals(this.customerName, other.customerName)) {
            return false;
        }
        if (!Objects.equals(this.state, other.state)) {
            return false;
        }
        if (!Objects.equals(this.productType, other.productType)) {
            return false;
        }
        if (!Objects.equals(this.orderNum, other.orderNum)) {
            return false;
        }
        if (!Objects.equals(this.taxRate, other.taxRate)) {
            return false;
        }
        if (!Objects.equals(this.area, other.area)) {
            return false;
        }
        if (!Objects.equals(this.costPerSqFt, other.costPerSqFt)) {
            return false;
        }
        if (!Objects.equals(this.laborCostPerSqFt, other.laborCostPerSqFt)) {
            return false;
        }
        if (!Objects.equals(this.materialCost, other.materialCost)) {
            return false;
        }
        if (!Objects.equals(this.laborCost, other.laborCost)) {
            return false;
        }
        if (!Objects.equals(this.tax, other.tax)) {
            return false;
        }
        if (!Objects.equals(this.total, other.total)) {
            return false;
        }
        if (!Objects.equals(this.dateNeeded, other.dateNeeded)) {
            return false;
        }
        return true;
    }
    
    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public BigDecimal getCostPerSqFt() {
        return costPerSqFt;
    }

    public void setCostPerSqFt(BigDecimal costPerSqFt) {
        this.costPerSqFt = costPerSqFt;
    }

    public BigDecimal getLaborCostPerSqFt() {
        return laborCostPerSqFt;
    }

    public void setLaborCostPerSqFt(BigDecimal laborCostPerSqFt) {
        this.laborCostPerSqFt = laborCostPerSqFt;
    }

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost;
    }

    public BigDecimal getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(BigDecimal laborCost) {
        this.laborCost = laborCost;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDate getDateNeeded() {
        return dateNeeded;
    }

    public void setDateNeeded(LocalDate dateNeeded) {
        this.dateNeeded = dateNeeded;
    }
    
    @Override
    public String toString() {
        return "Customer Name: " + customerName + " |Order Number: " 
                + orderNum + " |Date: " + dateNeeded;
    }
    
    
    
}
