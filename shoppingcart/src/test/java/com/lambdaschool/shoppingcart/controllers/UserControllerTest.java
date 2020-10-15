package com.lambdaschool.shoppingcart.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.shoppingcart.ShoppingcartApplication;
import com.lambdaschool.shoppingcart.models.*;
import com.lambdaschool.shoppingcart.services.UserService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ShoppingcartApplication.class)
@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
public class UserControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private List<User> data = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        Role r1 = new Role("ADMIN");
        r1.setRoleid(1);
        Role r2 = new Role("USER");
        r2.setRoleid(2);
        Role r3 = new Role("TEST");
        r3.setRoleid(3);

        Product p1 = new Product();
        p1.setName("pen");
        p1.setDescription("makes words");
        p1.setPrice(2.5);
        p1.setProductid(1);

        Product p2 = new Product();
        p2.setName("pencil");
        p2.setDescription("does math");
        p2.setPrice(1.5);
        p2.setProductid(2);

        Product p3 = new Product();
        p3.setName("coffee");
        p3.setDescription("everyone needs coffee");
        p3.setPrice(4);
        p3.setProductid(3);

        User u1 = new User();
        u1.getRoles().add(new UserRole(u1, r1));
        u1.getRoles().add(new UserRole(u1, r2));
        u1.getRoles().add(new UserRole(u1, r3));
        u1.setUsername("barnbarn");
        u1.setPassword("carrots");
        u1.setUserid(1);

        Cart c1 = new Cart();
        c1.setUser(u1);
        c1.getProducts().add((new CartItem(c1, p1, 4, "")));
        c1.getProducts().add((new CartItem(c1, p2, 3, "")));
        c1.getProducts().add((new CartItem(c1, p3, 2, "")));
        c1.setCartid(1);
        u1.getCarts().add(c1);

        data.add(u1);

        User u2 = new User();
        u2.getRoles().add(new UserRole(u2, r2));
        u2.getRoles().add(new UserRole(u2, r3));
        u2.setUsername("cinnamon");
        u2.setPassword("more_carrots");
        u1.setUserid(2);

        Cart c2 = new Cart();
        c2.setUser(u2);
        c2.getProducts().add((new CartItem(c2, p3, 1, "")));
        c2.setCartid(2);
        u2.getCarts().add(c2);

        data.add(u2);

        User u3 = new User();
        u3.getRoles().add(new UserRole(u3, r3));
        u3.setUsername("stumps");
        u3.setPassword("COFFEE");
        u1.setUserid(3);

        Cart c3 = new Cart();
        c3.setUser(u3);
        c3.getProducts().add((new CartItem(c3, p3, 17, "")));
        c3.setCartid(3);
        u3.getCarts().add(c3);

        data.add(u3);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void listAllUsers() throws Exception {
        String apiUrl = "/users/users";
        Mockito.when(userService.findAll())
                .thenReturn(data);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb)
                .andReturn();
        String tr = r.getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(data);

        assertEquals(er, tr);
    }

    @Test
    public void getUserById() throws Exception {
        String apiUrl = "/users/user/1";
        Mockito.when(userService.findUserById(1))
                .thenReturn(data.get(0));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb)
                .andReturn();
        String tr = r.getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(data.get(0));

        assertEquals(er, tr);
    }
}