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
        var notDeletedPosts =new ArrayList<Post>();
        for (Post post:posts.values()){
            for(Long key : posts.keySet()){
                if(post.getId() == key && !post.isRemoved()){
                    notDeletedPosts.add(post);
                }
            }
        }
        return notDeletedPosts;
    }

    public Optional<Post> getById(long id) {
        for (Post post : posts.values())
            if (post.getId() == id && post.isRemoved()) {
                notFoundException();
            }
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        long postId = post.getId();
        if (postId  == 0) {
            long id = idCounter.incrementAndGet();
            post.setId(id);
            posts.put(id, post);
        } else if (posts.containsKey(postId)) {
            if(posts.get(postId).isRemoved()){
                notFoundException();
            }
            posts.put(postId, post);
        }
        return post;
    }

    public void removeById(long id) {
        if (!posts.containsKey(id)) {
            notFoundException();
        }
        for (Post post : posts.values()) {
            if (post.getId() == id) {
                post.setRemoved(true);
            }
        }
    }

    private void notFoundException() {
        throw new NotFoundException();
    }
}
