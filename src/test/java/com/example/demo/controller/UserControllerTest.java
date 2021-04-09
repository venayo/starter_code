package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.swing.text.html.Option;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private static final Logger log = LoggerFactory.getLogger(UserControllerTest.class);

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository",userRepository);
        TestUtils.injectObjects(userController, "cartRepository",cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder",encoder);
    }

    @Test
    public void create_user_happy_path() throws Exception{
        log.info("create_user_happy_path");
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0,u.getId());
        assertEquals("test",u.getUsername());
        assertEquals("thisIsHashed",u.getPassword());

    }

    @Test
    public void find_user_by_id() throws Exception {
        log.info("find_user_by_id");
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());

        when(userRepository.findById(u.getId())).thenReturn(Optional.of(u));

        final ResponseEntity<User> responseFindUser = userController.findById(u.getId());
        assertEquals(200, responseFindUser.getStatusCodeValue());
        assertEquals("test", responseFindUser.getBody().getUsername());
        assertEquals("thisIsHashed", responseFindUser.getBody().getPassword());
    }

    @Test
    public void find_by_user_name() throws Exception {
        log.info("find_by_user_name");
        String name = "test";

        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());



        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());

        when(userRepository.findByUsername(u.getUsername())).thenReturn(u);

        final ResponseEntity<User> responseFindUser = userController.findByUserName(name);
        assertEquals(200, responseFindUser.getStatusCodeValue());
        assertEquals("test", responseFindUser.getBody().getUsername());
        assertEquals("thisIsHashed", responseFindUser.getBody().getPassword());
    }

}
