package com.lambdaschool.shoppingcart.controllers;

import com.lambdaschool.shoppingcart.models.*;
import com.lambdaschool.shoppingcart.services.CartService;
import com.lambdaschool.shoppingcart.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController
{
    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/user", produces = {"application/json"})
    public ResponseEntity<?> listAllCarts()
    {
        User me = userService.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());

        List<Cart> myCarts = cartService.findAllByUserId(me.getUserid());
        return new ResponseEntity<>(myCarts, HttpStatus.OK);
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @GetMapping(value = "/cart/{cartId}",
            produces = {"application/json"})
    public ResponseEntity<?> getCartById(
            @PathVariable
                    Long cartId)
    {
        Cart p = cartService.findCartById(cartId);
        return new ResponseEntity<>(p,
                                    HttpStatus.OK);
    }

    @PostMapping(value = "/create/product/{productid}")
    public ResponseEntity<?> addNewCart(@PathVariable long productid)
    {
        User me = userService.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());

        Product dataProduct = new Product();
        dataProduct.setProductid(productid);

        cartService.save(me, dataProduct);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/cart/{cartid}/product/{productid}")
    public ResponseEntity<?> updateCart(@PathVariable long cartid,
                                        @PathVariable long productid)
    {
        User me = userService.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());

        Cart cart = cartService.findCartById(cartid);

        if(me != cart.getUser()) {
            boolean isAdmin = false;
            for (UserRole ur : me.getRoles()) {
                if (ur.getRole().getName().equalsIgnoreCase("ADMIN")) {
                    isAdmin = true;
                    break;
                }
            }

            if(!isAdmin)
            {
                throw new AccessDeniedException("You do not have permission to modify this cart.");
            }
        }

        Product dataProduct = new Product();
        dataProduct.setProductid(productid);

        cartService.save(cart, dataProduct);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/cart/{cartid}/product/{productid}")
    public ResponseEntity<?> deleteFromCart(@PathVariable long cartid,
                                            @PathVariable long productid)
    {
        User me = userService.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());

        Cart cart = cartService.findCartById(cartid);

        if(me != cart.getUser()) {
            boolean isAdmin = false;
            for (UserRole ur : me.getRoles()) {
                if (ur.getRole().getName().equalsIgnoreCase("ADMIN")) {
                    isAdmin = true;
                    break;
                }
            }

            if(!isAdmin)
            {
                throw new AccessDeniedException("You do not have permission to modify this cart.");
            }
        }

        Product dataProduct = new Product();
        dataProduct.setProductid(productid);

        cartService.delete(cart, dataProduct);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
