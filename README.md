# Mapping a many to many association with extra columns using JPA / Hibernate
## The problem
If you want to map a @ManyToMany association with JPA you'll have to use a join table to represent the association.

Hibernate will automatically create the join table with two foreign keys: 

```
public class Post {
...
    @ManyToMany
    @JoinTable( name = "post_tag",
                joinColumns = @JoinColumn(referencedColumnName = "id", name = "postId"),
                inverseJoinColumns = @JoinColumn(referencedColumnName = "id", name = "tagId"))
    public List<Tag> tags;
...
}
```

But what if you want to add extra columns in this join table ? Mapping this association can be tricky.

This project provides an example of spring boot application with unit tests with a solution to this problem.

## The solution

The solution is to create an entity "manually" to represent the association:

```
@Entity(name="PostTagEntity")
@Table(name="post_tag")
public class PostTag implements Serializable {

    @EmbeddedId
    // Our two foreign keys
    // Check the real class for the pk implementation
    public PostTagId id; 
    
    // Our extra column !!
    Instant createdDate;
    ...
}
```

We have added an extra column `createdDate` in the join table.
Note that we have overrided the default `@Entity` name.

We **can't** use the default generated entity name "post_tag" because this entity name is already used by the `@JoinTable` definition in the `Post` entity,
this would throw an exception like "SQL strings added more than once for: post_tag".

We must override the entity name and and set whatever else, here `PostTagEntity`.

Because the entity name is different than the table name, we must set the `@Table` to map explicitly this entity on this table.

Once this is done, we must create the join table entries manually and save them in the database:

```
PostTag postTag = new PostTag();
postTag.id = ... // The join table foreign keys
postTag.createdDate = Instance.now(); // out extra column
save(postTag); // Saved with spring repository.
```

That's all ! The next time you'll get your Post entity, its `tags` list will be automatically filled:

```
Post myPost = findById(...);
myPost.tags; // The tags list is automatically filled
...
```

For more details, please check the implementation and the unit tests.