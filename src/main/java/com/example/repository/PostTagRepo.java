package com.example.repository;

import com.example.model.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepo extends JpaRepository<PostTag, PostTag.PostTagId> {
}
