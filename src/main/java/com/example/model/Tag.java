package com.example.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Tag implements Serializable{

    @Id
    @GeneratedValue
    public Long id;

    public String name;

    public Tag() {}

    public Tag(String name) {
        this.name = name;
    }

}
