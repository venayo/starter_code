package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private static final Logger log = LoggerFactory.getLogger(ItemControllerTest.class);

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void get_items_by_name() throws Exception {
        log.info("get_items_by_name");
        List<Item> items = new ArrayList<>();

        Item item2 = new Item();
        item2.setId(2l);
        item2.setName("Square Widget");
        item2.setDescription("A widget that is square");
        item2.setPrice(new BigDecimal(1.99));

        Item item3 = new Item();
        item3.setId(3l);
        item3.setName("Square Widget");
        item3.setDescription("A widget that is square");
        item3.setPrice(new BigDecimal(1.99));


        items.add(item2);
        items.add(item3);

        when(itemRepository.findByName("Square Widget")).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("Square Widget");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());

        response = itemController.getItemsByName("ITEM NOT EXIST");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void get_items() throws Exception {
        log.info("get_items");
        List<Item> items = new ArrayList<>();

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

        Item item3 = new Item();
        item3.setId(3l);
        item3.setName("Square Widget");
        item3.setDescription("A widget that is square");
        item3.setPrice(new BigDecimal(1.99));

        items.add(item1);
        items.add(item2);
        items.add(item3);

        when(itemRepository.findAll()).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(3, response.getBody().size());

    }


    @Test
    public void get_item_by_id() throws Exception {
        log.info("get_item_by_id");

        Item item = new Item();
        item.setId(1l);
        item.setName("Square Widget");
        item.setDescription("A widget that is square");
        item.setPrice(new BigDecimal(1.99));

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        final ResponseEntity<Item> response = itemController.getItemById(1l);

        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getId().longValue());
        assertEquals("Square Widget", response.getBody().getName());
        assertEquals("A widget that is square", response.getBody().getDescription());
        assertEquals(new BigDecimal(1.99), response.getBody().getPrice());
    }

}
