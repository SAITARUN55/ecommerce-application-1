package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    // EMG - The user repository, the cart repository, and the bCryptPasswordEncoder are injected
    // into the userController object
    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);

    }

    @Test
    public void create_user_happy_path() throws Exception {
        // EMG - Whenever the "testPassword" value is encountered by bCryptPasswordEncoder.encode,
        // it will return "thisIsHashed". This is a stub.
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("thisIsHashed");
        // EMG - Firstly, a user is created, completing the setup and calling the method under test
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        // EMG - Assertions are made
        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
    }

    @Test
    public void create_user_password_invalid() throws Exception {
        // EMG - Whenever the "testPassword" value is encountered by bCryptPasswordEncoder.encode,
        // it will return "thisIsHashed". This is a stub.
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("thisIsHashed");
        // EMG - Firstly, a user is created, completing the setup and calling the method under test
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("passwd");
        createUserRequest.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        // EMG - Assertions are made, and `bad request` (400) is expected
        assertNotNull(response);
        assertEquals(400,response.getStatusCodeValue());

    }

    @Test
    public void find_user_by_id_happy_path() throws Exception {
        // EMG - Firstly, a user setup is completed. Please, note that an initial cart is created at user creation
        User user = new User();
        user.setUsername("test");
        Cart cart = new Cart();
        cart.setId((long) 0);
        cart.setUser(user);
        user.setCart(cart);
        user.setId(0);
        user.setPassword("testPassword");

        when(userRepository.findById((long) 0)).thenReturn(java.util.Optional.of(user));
        // EMG - Secondly, the method under test is called
        final ResponseEntity<User> response = userController.findById((long) 0);

        // EMG - Finally, assertions are made
        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());

        User actualUser = response.getBody();
        assertNotNull(actualUser);
        assertEquals(0, actualUser.getId());
        assertEquals("test", actualUser.getUsername());
        assertEquals("testPassword", actualUser.getPassword());
        assertEquals(cart, actualUser.getCart());
    }

    @Test
    public void find_user_by_username_happy_path() throws Exception {
        // EMG - Firstly, a user setup is completed. Please, note that an initial cart is created at user creation
        User user = new User();
        user.setUsername("test");
        Cart cart = new Cart();
        cart.setId((long) 0);
        cart.setUser(user);
        user.setCart(cart);
        user.setId(0);
        user.setPassword("testPassword");

        when(userRepository.findByUsername("test")).thenReturn(user);
        // EMG - Secondly, the method under test is called
        final ResponseEntity<User> response = userController.findByUserName("test");

        // EMG - Finally, assertions are made
        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());

        User actualUser = response.getBody();
        assertNotNull(actualUser);
        assertEquals(0, actualUser.getId());
        assertEquals("test", actualUser.getUsername());
        assertEquals("testPassword", actualUser.getPassword());
        assertEquals(cart, actualUser.getCart());
    }

}
