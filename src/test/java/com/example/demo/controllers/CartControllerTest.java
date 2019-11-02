package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    // EMG - The user repository, the cart repository, and the item repository are injected
    // into the cartController object
    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void add_to_cart_happy_path() throws Exception {
        // EMG - Firstly, a modify cart request is set up
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(0);
        modifyCartRequest.setQuantity(3);

        // EMG - Secondly, a user setup is completed. Please, note that an initial cart is created at user creation
        User user = new User();
        user.setUsername("test");
        Cart cart = new Cart();
        cart.setId((long) 0);
        cart.setUser(user);
        user.setCart(cart);
        user.setId(0);
        user.setPassword("testPassword");

        when(userRepository.findByUsername("test")).thenReturn(user);

        // EMG - Thirdly, an item setup is completed
        Item item = new Item();
        item.setId((long) 0);
        item.setName("testItem");
        item.setPrice(new BigDecimal(2.99));
        item.setDescription("This is a testItem description");

        when(itemRepository.findById((long) 0)).thenReturn(java.util.Optional.of(item));

        // EMG - The method under test is called
        final ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        // EMG - Assertions are made
        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());

        Cart actualCart = response.getBody();
        assertNotNull(actualCart);
        assertEquals(cart.getId(), actualCart.getId());
        List<Item> itemsArray = Arrays.asList(new Item[modifyCartRequest.getQuantity()]);
        for (int i=0; i < itemsArray.size(); i++) {
            itemsArray.set(i, item);
        }
        assertEquals(itemsArray, actualCart.getItems());
        assertEquals(cart.getUser(), actualCart.getUser());
        assertEquals(new BigDecimal(8.97), actualCart.getTotal());
    }

    @Test
    public void add_to_cart_user_not_found() throws Exception {
        // EMG - Firstly, a modify cart request is set up
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(0);
        modifyCartRequest.setQuantity(3);

        // EMG - Secondly, a user setup is completed, where the user is null.
        User user = new User();

        when(userRepository.findByUsername("test")).thenReturn(user);

        // EMG - The method under test is called
        final ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        // EMG - Assertions are made
        assertNotNull(response);
        assertEquals(404,response.getStatusCodeValue());

    }

    @Test
    public void add_to_cart_item_not_found() throws Exception {
        // EMG - Firstly, a modify cart request is set up
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(3);

        // EMG - Secondly, a user setup is completed. Please, note that an initial cart is created at user creation
        User user = new User();
        user.setUsername("test");
        Cart cart = new Cart();
        cart.setId((long) 0);
        cart.setUser(user);
        user.setCart(cart);
        user.setId(0);
        user.setPassword("testPassword");

        when(userRepository.findByUsername("test")).thenReturn(user);

        // EMG - Thirdly, an item setup is completed
        Item item = new Item();

        when(itemRepository.findById((long) 0)).thenReturn(java.util.Optional.of(item));

        // EMG - The method under test is called
        final ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        // EMG - Assertions are made
        assertNotNull(response);
        assertEquals(404,response.getStatusCodeValue());

    }

    @Test
    public void remove_from_cart_happy_path() throws Exception {
        // EMG - Firstly, a modify cart request is set up
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(0);
        modifyCartRequest.setQuantity(1);

        // EMG - Secondly, a user setup is completed. Please, note that an initial cart is created at user creation
        User user = new User();
        user.setUsername("test");
        Cart cart = new Cart();
        cart.setId((long) 0);
        cart.setUser(user);
        user.setCart(cart);
        user.setId(0);
        user.setPassword("testPassword");

        // EMG - Thirdly, an item setup is completed
        Item item = new Item();
        item.setId((long) 0);
        item.setName("testItem");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("This is a testItem description");

        when(itemRepository.findById((long) 0)).thenReturn(java.util.Optional.of(item));

        // EMG - Note that a cartController.addTocart has been previously applied
        List<Item> itemsArray = new ArrayList<>();
        for (int i=0; i < 3; i++) {
            itemsArray.add(item);
        }
        cart.setItems(itemsArray);
        cart.setTotal(BigDecimal.valueOf(8.97));
        user.setCart(cart);

        when(userRepository.findByUsername("test")).thenReturn(user);

        // EMG - The method under test is called
        final ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);

        // EMG - Assertions are made
        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());

        Cart actualCart = response.getBody();
        assertNotNull(actualCart);
        assertEquals(cart.getId(), actualCart.getId());
        List<Item> expectedItemsArray = Arrays.asList(new Item[2]);
        for (int i=0; i < expectedItemsArray.size(); i++) {
            expectedItemsArray.set(i, item);
        }
        assertEquals(expectedItemsArray, actualCart.getItems());
        assertEquals(cart.getUser(), actualCart.getUser());
        assertEquals(BigDecimal.valueOf(5.98), actualCart.getTotal());
    }

    @Test
    public void remove_from_cart_user_not_found() throws Exception {
        // EMG - Firstly, a modify cart request is set up
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(0);
        modifyCartRequest.setQuantity(1);

        // EMG - Secondly, a user setup is completed, where the user is null.
        User user = new User();

        when(userRepository.findByUsername("test")).thenReturn(user);

        // EMG - The method under test is called
        final ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);

        // EMG - Assertions are made
        assertNotNull(response);
        assertEquals(404,response.getStatusCodeValue());

    }

    @Test
    public void remove_from_cart_item_not_found() throws Exception {
        // EMG - Firstly, a modify cart request is set up
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(1);

        // EMG - Secondly, a user setup is completed. Please, note that an initial cart is created at user creation
        User user = new User();
        user.setUsername("test");
        Cart cart = new Cart();
        cart.setId((long) 0);
        cart.setUser(user);
        user.setCart(cart);
        user.setId(0);
        user.setPassword("testPassword");

        when(userRepository.findByUsername("test")).thenReturn(user);

        // EMG - Thirdly, an item setup is completed
        Item item = new Item();

        when(itemRepository.findById((long) 0)).thenReturn(java.util.Optional.of(item));

        // EMG - The method under test is called
        final ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        // EMG - Assertions are made
        assertNotNull(response);
        assertEquals(404,response.getStatusCodeValue());

    }
}
