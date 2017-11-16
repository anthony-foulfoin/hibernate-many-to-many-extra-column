package com.example.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Post implements Serializable {

    @Id
    @GeneratedValue
    public Long id;

    public String title;

    @ManyToMany
    @JoinTable( name = "post_tag",
                joinColumns = @JoinColumn(referencedColumnName = "id", name = "postId"),
                inverseJoinColumns = @JoinColumn(referencedColumnName = "id", name = "tagId"))
    public List<Tag> tags;

    public Post() {}

    public Post(String title) {
        this.title = title;
    }

}
