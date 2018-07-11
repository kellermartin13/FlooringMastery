/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dto.Order;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Keller Martin
 */
public class FlooringServiceLayerImplTest {
    
    FlooringDaoStub stub = new FlooringDaoStub();
    FlooringServiceLayer service = new FlooringServiceLayerImpl(stub);
    public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
    LocalDate ld = LocalDate.parse(("12122018"), formatter);
    LocalDate newLd = LocalDate.parse(("11012018"), formatter);
    
    public FlooringServiceLayerImplTest() {
        
    }
    
    
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    
    /**
     * Test of calculateTotalCost method, of class FlooringServiceLayerImpl.
     */
    @Test
    public void testCalculateTotalCost() throws Exception {
        Order newOrder = new Order();
        newOrder.setArea(new BigDecimal("100"));
        newOrder.setCustomerName("Katie");
        newOrder.setDateNeeded(newLd);
        newOrder.setOrderNum(2);
        newOrder.setProductType("Wood");
        newOrder.setState("OH");
        newOrder = service.calculateTotalCost(newOrder);
        
        assertEquals(new BigDecimal("515.00"), newOrder.getMaterialCost());
        assertEquals(new BigDecimal("475.00"), newOrder.getLaborCost());
        assertEquals(new BigDecimal("61.88"), newOrder.getTax());
        assertEquals(new BigDecimal("1051.88"), newOrder.getTotal());
        
    }

    /**
     * Test of getOrdersByDate method, of class FlooringServiceLayerImpl.
     */
    @Test
    public void testGetOrdersByDate() throws Exception {
        Order newOrder = new Order();
        newOrder.setArea(BigDecimal.ZERO);
        newOrder.setCostPerSqFt(BigDecimal.ZERO);
        newOrder.setCustomerName("jeff");
        
        newOrder.setDateNeeded(newLd);
        newOrder.setLaborCost(BigDecimal.ZERO);
        newOrder.setLaborCostPerSqFt(BigDecimal.ZERO);
        newOrder.setMaterialCost(BigDecimal.ZERO);
        newOrder.setOrderNum(2);
        newOrder.setProductType("Tile");
        newOrder.setState("KY");
        newOrder.setTax(BigDecimal.TEN);
        newOrder.setTaxRate(BigDecimal.ZERO);
        newOrder.setTotal(BigDecimal.ONE);
        service.addNewOrder(newOrder);
        assertEquals(2, service.getAllOrders().size());
        
        List<Order> ordersByDate = service.getOrdersByDate(ld);
        assertEquals(1, ordersByDate.size());
        assertEquals(ordersByDate.get(0), service.getOrder(1, ld));
        
        ordersByDate = service.getOrdersByDate(newLd);
        assertEquals(1, ordersByDate.size());
        assertEquals(ordersByDate.get(0), service.getOrder(2, newLd));
    }

    /**
     * Test of addNewOrder method, of class FlooringServiceLayerImpl.
     */
    @Test
    public void testAddNewOrder() throws Exception {
        Order newOrder = new Order();
        newOrder.setArea(BigDecimal.ZERO);
        newOrder.setCostPerSqFt(BigDecimal.ZERO);
        newOrder.setCustomerName("jeff");
        
        newOrder.setDateNeeded(newLd);
        newOrder.setLaborCost(BigDecimal.ZERO);
        newOrder.setLaborCostPerSqFt(BigDecimal.ZERO);
        newOrder.setMaterialCost(BigDecimal.ZERO);
        newOrder.setProductType("Tile");
        newOrder.setState("KY");
        newOrder.setTax(BigDecimal.TEN);
        newOrder.setTaxRate(BigDecimal.ZERO);
        newOrder.setTotal(BigDecimal.ONE);
        service.addNewOrder(newOrder);
        Order order = service.getOrder(2, newLd);
        Assert.assertTrue(2 == order.getOrderNum());
        assertEquals("jeff", order.getCustomerName());
        
        
    }
    
}
