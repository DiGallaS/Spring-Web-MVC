package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {
    private final Map<Long, Post> posts = new ConcurrentHashMap<>(){};
    private final AtomicLong idCounter = new AtomicLong();

    public List<Post> all() {
        return new ArrayList<>(posts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            long id = idCounter.incrementAndGet();
            post.setId(id);
            posts.put(id, post);
        } else if (post.getId() != 0) {
            Long currentId = post.getId();
            posts.put(currentId, post);
        }
        return post;
    }

    public void removeById(long id) {
        if (!posts.containsKey(id)) {
            throw new NotFoundException();
        }
        posts.remove(id);
    }
}
