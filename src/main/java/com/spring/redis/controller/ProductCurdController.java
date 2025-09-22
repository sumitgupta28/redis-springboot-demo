package com.spring.redis.controller;


import com.spring.redis.entity.Product;
import com.spring.redis.repository.ProductRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Redis Database ", description = "Below APIs Demonstrate how Redis can be used as Database")
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