package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);

    // EMG - The user repository, and the order repository are injected
    // into the orderController object
    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void submit_order_username_happy_path() throws Exception {
        // EMG - Firstly, a user setup is completed. Please, note that an initial cart is created
        // at user creation
        User user = new User();
        user.setUsername("test");
        Cart cart = new Cart();
        cart.setId((long) 0);
        cart.setUser(user);
        user.setCart(cart);
        user.setId(0);
        user.setPassword("testPassword");

        // EMG - Secondly, an item setup is completed
        Item item = new Item();
        item.setId((long) 0);
        item.setName("testItem");
        item.setPrice(new BigDecimal(2.99));
        item.setDescription("This is a testItem description");

        // EMG - It is assumed that a cartController.addTocart has been previously applied
        List<Item> itemsArray = new ArrayList<>();
        for (int i=0; i < 3; i++) {
            itemsArray.add(item);
        }
        cart.setItems(itemsArray);
        cart.setTotal(BigDecimal.valueOf(8.97));
        user.setCart(cart);

        when(userRepository.findByUsername("test")).thenReturn(user);

        // EMG - The method under test is called
        final ResponseEntity<UserOrder> response = orderController.submit("test");

        // EMG - Assertions are made
        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());

        UserOrder actualUserOrder = response.getBody();
        assertNotNull(actualUserOrder);
        assertEquals(cart.getItems(), actualUserOrder.getItems());
        assertEquals(cart.getUser(), actualUserOrder.getUser());
        assertEquals(cart.getTotal(), actualUserOrder.getTotal());
    }

    @Test
    public void submit_order_username_not_found() throws Exception {

        when(userRepository.findByUsername("test")).thenReturn(null);

        // EMG - The method under test is called
        final ResponseEntity<UserOrder> response = orderController.submit("test");

        // EMG - Assertions are made
        assertNotNull(response);
        assertEquals(404,response.getStatusCodeValue());
    }

    @Test
    public void get_orders_for_user_happy_path() throws Exception {
        // EMG - Firstly, a user setup is completed. Please, note that an initial cart is created
        // at user creation
        User user = new User();
        user.setUsername("test");
        Cart cart = new Cart();
        cart.setId((long) 0);
        cart.setUser(user);
        user.setCart(cart);
        user.setId(0);
        user.setPassword("testPassword");

        // EMG - Secondly, an item setup is completed
        Item item = new Item();
        item.setId((long) 0);
        item.setName("testItem");
        item.setPrice(new BigDecimal(2.99));
        item.setDescription("This is a testItem description");

        // EMG - It is assumed that a cartController.addTocart has been previously applied
        List<Item> itemsArray = new ArrayList<>();
        for (int i=0; i < 3; i++) {
            itemsArray.add(item);
        }
        cart.setItems(itemsArray);
        cart.setTotal(BigDecimal.valueOf(8.97));
        user.setCart(cart);

        when(userRepository.findByUsername("test")).thenReturn(user);

        // EMG - It is assumed that a orderController.submit has been previously applied. For instance,
        // let' s assume it has been applied twice
        UserOrder order = UserOrder.createFromCart(user.getCart());
        List<UserOrder> expectedUserOrders = new ArrayList<>();
        for (int i=0; i < 2; i++) {
            order.setId((long) i);
            expectedUserOrders.add(order);
        }

        when(orderRepository.findByUser(user)).thenReturn(expectedUserOrders);

        // EMG - The method under test is called
        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");

        // EMG - Assertions are made
        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());

        List<UserOrder> actualUserOrders = response.getBody();
        assertNotNull(actualUserOrders);
        assertEquals(expectedUserOrders, actualUserOrders);
    }

    @Test
    public void get_orders_for_user_username_not_found() throws Exception {

        when(userRepository.findByUsername("test")).thenReturn(null);

        // EMG - The method under test is called
        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");

        // EMG - Assertions are made
        assertNotNull(response);
        assertEquals(404,response.getStatusCodeValue());
    }

}
