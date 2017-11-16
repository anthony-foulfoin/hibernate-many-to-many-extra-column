package com.example;

import com.example.model.Post;
import com.example.model.PostTag;
import com.example.model.Tag;
import com.example.repository.PostRepo;
import com.example.repository.PostTagRepo;
import com.example.repository.TagRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@Transactional
@Rollback
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Autowired
	private PostRepo postRepo;

	@Autowired
	private TagRepo tagRepo;

	@Autowired
	private PostTagRepo postTagRepo;

	@Autowired
	private EntityManager em;

	@Test
	public void create_relation_auto_through_post_entity() {
		Tag tag1 = new Tag("economy");
		Tag tag2 = new Tag("technology");

		tag1 = tagRepo.saveAndFlush(tag1);
		em.detach(tag1);

		tag2 = tagRepo.saveAndFlush(tag2);
		em.detach(tag2);

		Post post = new Post("My awesome post");

		// Tags are attached to the post directly by saving them in the Post tags list
		// The corresponding PostTag entries will be created automatically in the database
		// Because the PostTag are created automatically, the extra column "createdDate" will be null
		post.tags = Arrays.asList(tag1, tag2);

		post = postRepo.saveAndFlush(post);
		em.detach(post);

		// All the entities have been detached to be sure the following queries will perform fresh sql queries, and won't use the session
		Post dbPost = postRepo.findAll().get(0);
		List<PostTag> postTags = postTagRepo.findAll();

		assertEquals(2, postTags.size());
		// No extra column here !
		assertNull(postTags.get(0).createdDate);
		assertNull(postTags.get(1).createdDate);
		assertEquals(2, postTags.size());
		assertEquals(2, dbPost.tags.size());
		assertEquals("economy", dbPost.tags.get(0).name);
		assertEquals("technology", dbPost.tags.get(1).name);
	}

	@Test
	public void create_relation_manually_throug_repo() {
		Tag tag1 = new Tag("economy");
		Tag tag2 = new Tag("technology");

		tag1 = tagRepo.saveAndFlush(tag1);
		em.detach(tag1);

		tag2 = tagRepo.saveAndFlush(tag2);
		em.detach(tag2);

		Post post = new Post("My awesome post");
		post = postRepo.saveAndFlush(post);
		em.detach(post);

		// Tags are attached to the post by creating manually the PostTag join table entries
		// This is allowing us to add extra columns, like the "createdDate"
		PostTag postTag1 = new PostTag(post, tag1);
		postTagRepo.saveAndFlush(postTag1);

		PostTag postTag2 = new PostTag(post, tag2);
		postTagRepo.saveAndFlush(postTag2);

		// All the entities have been detached to be sure the following queries will perform fresh sql queries, and won't use the session
		Post dbPost = postRepo.findAll().get(0);
		List<PostTag> postTags = postTagRepo.findAll();

		assertEquals(2, postTags.size());
		// Extra column
		assertNotNull(postTags.get(0).createdDate);
		assertNotNull(postTags.get(1).createdDate);
		assertEquals(2, dbPost.tags.size());
		assertEquals("economy", dbPost.tags.get(0).name);
		assertEquals("technology", dbPost.tags.get(1).name);
	}

}
