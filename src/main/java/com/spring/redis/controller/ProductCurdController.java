package com.spring.redis.controller;


import com.spring.redis.entity.Product;
import com.spring.redis.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductCurdController {

    private final ProductRepository productRepository;

    @PostMapping
    public String saveProduct(@RequestBody Product product) {
        productRepository.save(product);
        return "Product saved successfully!";
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable String id) {
        return productRepository.findById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteProductById(@PathVariable String id) {
        productRepository.deleteById(id);
        return "Product deleted successfully!";
    }
}