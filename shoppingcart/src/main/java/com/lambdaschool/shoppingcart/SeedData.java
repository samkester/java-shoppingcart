package com.lambdaschool.shoppingcart;

import com.lambdaschool.shoppingcart.models.*;
import com.lambdaschool.shoppingcart.repositories.RoleRepository;
import com.lambdaschool.shoppingcart.repositories.UserRepository;
import com.lambdaschool.shoppingcart.services.CartService;
import com.lambdaschool.shoppingcart.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class SeedData implements CommandLineRunner {
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductService productService;

    @Autowired
    CartService cartService;

    @Transactional

    @Override
    public void run(String... args) throws Exception {
        roleRepository.deleteAll();
        userRepository.deleteAll();
        productService.deleteAll();
        cartService.deleteAll();

        Role r1 = roleRepository.save(new Role("ADMIN"));
        Role r2 = roleRepository.save(new Role("USER"));
        Role r3 = roleRepository.save(new Role("TEST"));

        Product p1 = new Product();
        p1.setName("pen");
        p1.setDescription("makes words");
        p1.setPrice(2.5);
        productService.save(p1);

        Product p2 = new Product();
        p2.setName("pencil");
        p2.setDescription("does math");
        p2.setPrice(1.5);
        productService.save(p2);

        Product p3 = new Product();
        p3.setName("coffee");
        p3.setDescription("everyone needs coffee");
        p3.setPrice(4);
        productService.save(p3);

        User u1 = new User();
        u1.getRoles().add(new UserRole(u1, r1));
        u1.getRoles().add(new UserRole(u1, r2));
        u1.getRoles().add(new UserRole(u1, r3));
        u1.setUsername("barnbarn");
        u1.setPassword("carrots");

        Cart c1 = new Cart();
        c1.setUser(u1);
        c1.getProducts().add((new CartItem(c1, p1, 4, "")));
        c1.getProducts().add((new CartItem(c1, p2, 3, "")));
        c1.getProducts().add((new CartItem(c1, p3, 2, "")));
        u1.getCarts().add(c1);

        userRepository.save(u1);

        User u2 = new User();
        u2.getRoles().add(new UserRole(u2, r2));
        u2.getRoles().add(new UserRole(u2, r3));
        u2.setUsername("cinnamon");
        u2.setPassword("more_carrots");

        Cart c2 = new Cart();
        c2.setUser(u2);
        c2.getProducts().add((new CartItem(c2, p3, 1, "")));
        u2.getCarts().add(c2);

        userRepository.save(u2);

        User u3 = new User();
        u3.getRoles().add(new UserRole(u3, r3));
        u3.setUsername("stumps");
        u3.setPassword("COFFEE");

        Cart c3 = new Cart();
        c3.setUser(u3);
        c3.getProducts().add((new CartItem(c3, p3, 17, "")));
        u3.getCarts().add(c3);

        userRepository.save(u3);
    }
}
