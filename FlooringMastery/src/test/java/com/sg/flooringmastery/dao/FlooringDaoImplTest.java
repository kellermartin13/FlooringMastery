/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Keller Martin
 */
public class FlooringDaoImplTest {

    public FlooringDao dao = new FlooringDaoImpl();
    public DateTimeFormatter formatted = DateTimeFormatter.ofPattern("MMddyyyy");

    public FlooringDaoImplTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws FlooringPersistenceException {
        List<Order> orderList = dao.getAllOrders();
        for (Order currentOrder : orderList) {
            orderList.remove(currentOrder);
        }
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getAllOrders method, of class FlooringDaoImpl.
     */
    @Test
    public void testGetAllOrders() throws Exception {
        List<Order> orderList = dao.getAllOrders();
        assertEquals("List should be empty to begin", 0,
                orderList.size());
        Order order1 = new Order();
        order1.setCustomerName("Greg");
        order1.setProductType("Wood");
        order1.setState("PA");
        order1.setDateNeeded(LocalDate.now());
        dao.addNewOrder(order1);

        Order order2 = new Order();
        order2.setCustomerName("Katie");
        order2.setProductType("Tile");
        order2.setState("MI");
        order2.setDateNeeded(LocalDate.now());
        dao.addNewOrder(order2);

        Order order3 = new Order();

        orderList = dao.getAllOrders();
        assertEquals("Size 2 expected", 2, orderList.size());

        assertTrue(orderList.contains(order1));
        assertTrue(orderList.contains(order2));
        assertFalse(orderList.contains(order3));
    }

    /**
     * Test of getOrder method, of class FlooringDaoImpl.
     */
    @Test
    public void testAddGetOrder() throws Exception {
        List<Order> orderList = dao.getAllOrders();
        assertEquals("List should be empty to begin", 0,
                orderList.size());
        Order order1 = new Order();
        order1.setCustomerName("Greg");
        order1.setProductType("Wood");
        order1.setState("PA");
        order1.setDateNeeded(LocalDate.now());
        order1.setOrderNum(1);
        dao.addNewOrder(order1);

        Order order2 = new Order();
        order2.setCustomerName("Katie");
        order2.setProductType("Tile");
        order2.setState("MI");
        order2.setDateNeeded(LocalDate.now());
        order2.setOrderNum(2);
        dao.addNewOrder(order2);

        Order order = dao.getOrder(1, order1.getDateNeeded());
        assertEquals(order, order1);

        order = dao.getOrder(2, order2.getDateNeeded());
        assertEquals(order, order2);
    }

    /**
     * Test of editOrder method, of class FlooringDaoImpl.
     */
    @Test
    public void testEditOrder() throws Exception {
        boolean correctExceptionThrown = false;
        Order oldOrder = new Order();
        oldOrder.setCustomerName("Greg");
        oldOrder.setProductType("Wood");
        oldOrder.setState("PA");
        oldOrder.setDateNeeded(LocalDate.now());
        oldOrder.setOrderNum(1);
        dao.addNewOrder(oldOrder);

        Order updatedOrder = new Order();
        updatedOrder.setCustomerName("Katie");
        updatedOrder.setProductType("Tile");
        updatedOrder.setState("MI");
        updatedOrder.setDateNeeded(LocalDate.now());
        updatedOrder.setOrderNum(1);

        Order order = dao.getOrder(1, oldOrder.getDateNeeded());
        assertEquals(order, oldOrder);

        //TESTING FOR UPDATING ORDER WITH SAME DATES
        dao.editOrder(updatedOrder, oldOrder);
        assertEquals("Order 1 should now refer to updatedOrder", updatedOrder,
                dao.getOrder(1, LocalDate.now()));

        LocalDate oldDate = LocalDate.parse("12122018", formatted);
        LocalDate newDate = LocalDate.parse("11012018", formatted);

        Order oldOrderDate = new Order();
        oldOrderDate.setCustomerName("Greg");
        oldOrderDate.setProductType("Wood");
        oldOrderDate.setState("PA");
        oldOrderDate.setDateNeeded(oldDate);
        oldOrderDate.setOrderNum(2);
        dao.addNewOrder(oldOrderDate);

        Order updatedOrderDate = new Order();
        updatedOrderDate.setCustomerName("Katie");
        updatedOrderDate.setProductType("Tile");
        updatedOrderDate.setState("MI");
        updatedOrderDate.setDateNeeded(newDate);
        updatedOrderDate.setOrderNum(2);

        order = dao.getOrder(2, oldOrderDate.getDateNeeded());
        assertEquals(order, oldOrderDate);

        //TESTING FOR UPDATING ORDER WITH UPDATED DATE
        dao.editOrder(updatedOrderDate, oldOrderDate);
        assertEquals("Should get updated order", updatedOrderDate,
                dao.getOrder(2, newDate));

        //EXCEPTION TESTING
        //GIVEN DATE NOT IN SYSTEM
        try {
            dao.getOrder(2, LocalDate.now());
            fail("Did not throw expected exception");
        } catch (NoSuchDateException | NoSuchOrderException e) {
            correctExceptionThrown = true;
        }
        assertTrue(correctExceptionThrown);
        correctExceptionThrown = false;

        //NO ORDERS FOR GIVEN DATE
        try {
            dao.getOrder(7, newDate);
            fail("Did not throw expected exception");
        } catch (NoSuchOrderException e) {
            correctExceptionThrown = true;
        }
        assertTrue(correctExceptionThrown);

    }

    /**
     * Test of removeOrder method, of class FlooringDaoImpl.
     */
    @Test
    public void testRemoveOrder() throws Exception {
        boolean correctExceptionThrown = false;
        Order order1 = new Order();
        order1.setCustomerName("Greg");
        order1.setProductType("Wood");
        order1.setState("PA");
        order1.setDateNeeded(LocalDate.now());
        order1.setOrderNum(1);
        dao.addNewOrder(order1);

        Order order2 = new Order();
        order2.setCustomerName("Katie");
        order2.setProductType("Tile");
        order2.setState("MI");
        order2.setDateNeeded(LocalDate.now());
        order2.setOrderNum(2);
        dao.addNewOrder(order2);

        Order order = dao.getOrder(1, order1.getDateNeeded());
        assertEquals(order, order1);
        order = dao.getOrder(2, order2.getDateNeeded());
        assertEquals(order, order2);

        dao.removeOrder(1, order1.getDateNeeded());
        try {
            dao.getOrder(1, LocalDate.now());
            fail("Expected exception was not thrown.");
        } catch (NoSuchOrderException e) {
            correctExceptionThrown = true;
        }
        assertTrue(correctExceptionThrown);
        correctExceptionThrown = false;

        dao.removeOrder(2, LocalDate.now());

        assertEquals("Map should be empty", 0, dao.getAllOrders().size());
    }

    /**
     * Test of getStateTaxes method, of class FlooringDaoImpl.
     */
    @Test
    public void testAddGetGetAllStateTaxes() throws Exception {
        BigDecimal bdTx = new BigDecimal("5.45");
        BigDecimal bdKy = new BigDecimal("7.15");
        Tax stateTax = new Tax("TX", bdTx);
        Tax stateTax2 = new Tax("KY", bdKy);

        assertEquals("Map should be empty", 0, dao.getAllStateTaxes().size());
        dao.addNewStateTax(stateTax);
        assertEquals("Map should have 1 object", 1,
                dao.getAllStateTaxes().size());
        assertEquals("Object should be equal to stateTax",
                stateTax, dao.getStateTaxes("TX"));

        dao.addNewStateTax(stateTax2);
        assertEquals("Map should have 2 objects", 2,
                dao.getAllStateTaxes().size());
        assertEquals("Object should be equal to stateTax2",
                stateTax2, dao.getStateTaxes("KY"));
    }

    /**
     * Test of editStateTax method, of class FlooringDaoImpl.
     */
    @Test
    public void testEditRemoveStateTax() throws Exception {
        BigDecimal bdTx = new BigDecimal("5.45");
        BigDecimal bdKy = new BigDecimal("7.15");
        Tax stateTax = new Tax("TX", bdTx);
        Tax stateTax2 = new Tax("KY", bdKy);
        dao.addNewStateTax(stateTax);

        assertEquals("TX should be in map", stateTax, dao.getStateTaxes("TX"));

        dao.editStateTax(stateTax, stateTax2);
        assertEquals("Should equal stateTax2", stateTax2,
                dao.getStateTaxes("KY"));
        assertNull("TX should not exist", dao.getStateTaxes("TX"));

        dao.removeStateTax("KY");
        assertEquals("Map should be empty", 0, dao.getAllStateTaxes().size());
        assertNull("KY should not exist", dao.getStateTaxes("KY"));

    }

    /**
     * Test of getProductCosts method, of class FlooringDaoImpl.
     */
    @Test
    public void testAddGetGetAllProductCosts() throws Exception {
        BigDecimal costWood = new BigDecimal("5.45");
        BigDecimal laborCostWood = new BigDecimal("7.15");
        Product wood = new Product("Wood", costWood, laborCostWood);
        BigDecimal costTile = new BigDecimal("3.25");
        BigDecimal laborCostTile = new BigDecimal("5.15");
        Product tile = new Product("Tile", costTile, laborCostTile);

        assertEquals("Map should be empty", 0, dao.getAllProductCosts().size());

        dao.addNewProductCosts(wood);
        assertEquals(1, dao.getAllProductCosts().size());
        assertEquals(wood, dao.getProductCosts("Wood"));

        dao.addNewProductCosts(tile);
        assertEquals(2, dao.getAllProductCosts().size());
        assertEquals(tile, dao.getProductCosts("Tile"));
    }

    /**
     * Test of editProductCosts method, of class FlooringDaoImpl.
     */
    @Test
    public void testEditRemoveProductCosts() throws Exception {
        BigDecimal costWood = new BigDecimal("5.45");
        BigDecimal laborCostWood = new BigDecimal("7.15");
        Product wood = new Product("Wood", costWood, laborCostWood);
        BigDecimal costTile = new BigDecimal("3.25");
        BigDecimal laborCostTile = new BigDecimal("5.15");
        Product tile = new Product("Tile", costTile, laborCostTile);
        dao.addNewProductCosts(wood);

        assertEquals("Wood should be in map", wood, dao.getProductCosts("Wood"));

        dao.editProductCosts(wood, tile);
        assertEquals("Should equal tile", tile,
                dao.getProductCosts("Tile"));
        assertNull("Wood should not exist", dao.getProductCosts("Wood"));

        dao.removeProductCosts("Tile");
        assertEquals("Map should be empty", 0, dao.getAllProductCosts().size());
        assertNull("Tile should not exist", dao.getProductCosts("Tile"));
    }

    /**
     * Test of saveCurrentWork method, of class FlooringDaoImpl.
     */
    @Test
    public void testSaveCurrentWork() throws Exception {

    }

    /**
     * Test of loadInitial method, of class FlooringDaoImpl.
     */
    @Test
    public void testLoadInitial() throws Exception {
    }
}
