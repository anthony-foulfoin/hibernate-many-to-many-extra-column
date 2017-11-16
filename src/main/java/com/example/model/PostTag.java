package com.example.model;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * The join table
 * We **CAN'T** use the default generated entity name "post_tag" because this entity name is already used by the @JoinTable definition in the Post entity,
 * this would throw an exception like "SQL strings added more than once for: post_tag".
 * We must override the entity name and and set whatever else, here "PostTagEntity".
 * Because the entity name is different than the table name, we must set the @Table to map explicitly this entity on this table.
 */
@Entity(name="PostTagEntity")
@Table(name="post_tag")
public class PostTag implements Serializable {

    @EmbeddedId
    public PostTagId postTagId;

    // Our extra column
    public Instant createdDate;

    public PostTag() {}

    public PostTag(Post post, Tag tag) {
        postTagId = new PostTag.PostTagId();
        postTagId.postId = post.id;
        postTagId.tagId = tag.id;
        createdDate = Instant.now();
    }

    @Embeddable
    public static class PostTagId implements Serializable {

        public Long postId;
        public Long tagId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PostTagId postTagId = (PostTagId) o;
            return Objects.equals(postId, postTagId.postId) &&
                    Objects.equals(tagId, postTagId.tagId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(postId, tagId);
        }
    }
}
