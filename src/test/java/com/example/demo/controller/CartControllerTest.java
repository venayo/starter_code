package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private static final Logger log = LoggerFactory.getLogger(CartControllerTest.class);

    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void add_to_cart() throws Exception {
        log.info("add_to_cart");
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(2);

        User user = new User();
        user.setPassword("test");
        user.setUsername("test");
        user.setId(1);
        user.setCart(new Cart());

        Item item = new Item();
        item.setId(1l);
        item.setName("Round Widget");
        item.setDescription("A widget that is round");
        item.setPrice(new BigDecimal(2.99));

        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(user);

        when(itemRepository.findById(modifyCartRequest.getItemId())).thenReturn(Optional.of(item));

        final ResponseEntity<Cart> resAddCart = cartController.addTocart(modifyCartRequest);

        assertEquals(200, resAddCart.getStatusCodeValue());
        assertEquals(2, resAddCart.getBody().getItems().size());

    }

    @Test
    public void remove_from_cart() throws Exception {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(2);

        ModifyCartRequest modifyCartITEMNOTFOUNDRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1000);
        modifyCartRequest.setQuantity(2);

        User user = new User();
        user.setPassword("test");
        user.setUsername("test");
        user.setId(1);
        user.setCart(new Cart());

        Item item = new Item();
        item.setId(1l);
        item.setName("Round Widget");
        item.setDescription("A widget that is round");
        item.setPrice(new BigDecimal(2.99));

        user.getCart().addItem(item);
        user.getCart().addItem(item);
        user.getCart().addItem(item);
        user.getCart().addItem(item);
        user.getCart().addItem(item);
        user.getCart().addItem(item);
        user.getCart().addItem(item);

        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(user);

        when(itemRepository.findById(modifyCartRequest.getItemId())).thenReturn(Optional.of(item));

        ResponseEntity<Cart> resRemoveCart = cartController.removeFromcart(modifyCartITEMNOTFOUNDRequest);

        log.debug("resRemoveCart.getStatusCodeValue: " + resRemoveCart.getStatusCodeValue());
        assertEquals(404, resRemoveCart.getStatusCodeValue());

        resRemoveCart = cartController.removeFromcart(modifyCartRequest);

        assertEquals(200, resRemoveCart.getStatusCodeValue());
        assertEquals(5, resRemoveCart.getBody().getItems().size());

    }

}
