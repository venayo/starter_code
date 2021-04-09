package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private static final Logger log = LoggerFactory.getLogger(OrderControllerTest.class);

    private OrderController orderController;

    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
    }

    @Test
    public void submit() throws Exception {
        log.info("submit");

        User user = new User();
        user.setCart(new Cart());
        user.setUsername("test");
        user.setId(1);
        user.setPassword("test");

        Item item1 = new Item();
        item1.setId(1l);
        item1.setName("Square Widget");
        item1.setDescription("A widget that is square");
        item1.setPrice(new BigDecimal(1.99));

        Item item2 = new Item();
        item2.setId(2l);
        item2.setName("Square Widget");
        item2.setDescription("A widget that is square");
        item2.setPrice(new BigDecimal(1.99));

        user.getCart().addItem(item1);
        user.getCart().addItem(item2);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        final ResponseEntity<UserOrder> responseFindUser = orderController.submit(user.getUsername());

        assertEquals(200, responseFindUser.getStatusCodeValue());
        assertNotNull(responseFindUser.getBody());
        assertEquals(2, responseFindUser.getBody().getItems().size());
        assertEquals(3.98, responseFindUser.getBody().getTotal().doubleValue(), 0);

    }

    @Test
    public void get_orders_for_user() throws Exception {
        log.info("get_orders_for_user");

        User user = new User();
        user.setCart(new Cart());
        user.setUsername("test");
        user.setId(1);
        user.setPassword("test");

        Item item1 = new Item();
        item1.setId(1l);
        item1.setName("Square Widget");
        item1.setDescription("A widget that is square");
        item1.setPrice(new BigDecimal(1.99));

        Item item2 = new Item();
        item2.setId(2l);
        item2.setName("Square Widget");
        item2.setDescription("A widget that is square");
        item2.setPrice(new BigDecimal(1.99));

        user.getCart().addItem(item1);
        user.getCart().addItem(item2);

        List<UserOrder> userOrders = new ArrayList<>();
        UserOrder userOrder = UserOrder.createFromCart(user.getCart());
        userOrder.setId(1l);
        userOrder.setUser(user);
        userOrders.add(userOrder);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(userOrders);

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(user.getUsername());

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(user.getUsername(), response.getBody().get(0).getUser().getUsername());

    }
}
