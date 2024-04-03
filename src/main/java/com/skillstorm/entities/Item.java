package com.skillstorm.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private int volume;
}
