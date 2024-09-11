package com.skillstorm.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String location;

    private int capacity;
}
