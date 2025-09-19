package com.spring.redis.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class Product implements Serializable {
    private String id;
    private String name;
    private double price;


}
