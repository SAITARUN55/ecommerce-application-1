package com.example.demo.controllers;

import java.util.Optional;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	private	static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@PostMapping("/addToCart")
	public ResponseEntity<Cart> addTocart(@Valid @RequestBody ModifyCartRequest request) {
		log.info("Adding to Cart with user name {}, and item id {}", request.getUsername(), request.getItemId());
		User user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			log.error("addToCart request failure. Error with user existence. Cannot add to cart with user name {}, and item id {}", request.getUsername(), request.getItemId());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			log.error("addToCart request failure. Error with item existence. Cannot add to cart with user name {}, and item id {}", request.getUsername(), request.getItemId());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity())
			.forEach(i -> cart.addItem(item.get()));
		cartRepository.save(cart);
		log.info("addToCart request success. Addition to Cart done with user name {}, and item id {}", request.getUsername(), request.getItemId());
		return ResponseEntity.ok(cart);
	}
	
	@PostMapping("/removeFromCart")
	public ResponseEntity<Cart> removeFromcart(@Valid @RequestBody ModifyCartRequest request) {
		log.info("Removing from Cart with user name {}, and item id {}", request.getUsername(), request.getItemId());
		User user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			log.error("removeFromCart request failure. Error with user existence. Cannot remove from cart with user name {}, and item id {}", request.getUsername(), request.getItemId());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			log.error("removeFromCart request failure. Error with item existence. Cannot remove from cart with user name {}, and item id {}", request.getUsername(), request.getItemId());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity())
			.forEach(i -> cart.removeItem(item.get()));
		cartRepository.save(cart);
		log.info("removeFromCart request success. Removal from Cart done with user name {}, and item id {}", request.getUsername(), request.getItemId());
		return ResponseEntity.ok(cart);
	}
		
}
