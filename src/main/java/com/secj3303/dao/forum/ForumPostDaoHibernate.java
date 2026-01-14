package com.secj3303.dao.forum;

import java.util.Collections;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.secj3303.model.ForumPost;
import com.secj3303.model.PostComment;
import com.secj3303.model.PostLike;
import com.secj3303.model.User;

@Repository
public class ForumPostDaoHibernate implements ForumPostDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session openSession() {
        return sessionFactory.openSession();
    }

    // Helper to load data 
    private void initializePostData(ForumPost p) {
        Hibernate.initialize(p.getUsers());
        Hibernate.initialize(p.getLikes());
        p.getLikes().forEach(like -> Hibernate.initialize(like.getUsers()));
        Hibernate.initialize(p.getComments());
        p.getComments().forEach(comment -> Hibernate.initialize(comment.getUsers()));
    }

    @Override
    public List<ForumPost> getAllPosts() {
        try (Session session = openSession()) {
            Query<ForumPost> query = session.createQuery(
                "FROM ForumPost p WHERE p.users IS NOT NULL ORDER BY p.createdAt DESC", ForumPost.class);
            List<ForumPost> posts = query.list();
            posts.forEach(this::initializePostData);
            return posts;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<ForumPost> getPostsByUserId(int userId) {
        try (Session session = openSession()) {
            Query<ForumPost> query = session.createQuery(
                "FROM ForumPost p WHERE p.users.userID = :uid ORDER BY p.createdAt DESC", ForumPost.class);
            query.setParameter("uid", userId);
            List<ForumPost> posts = query.list();
            posts.forEach(this::initializePostData);
            return posts;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public void createPost(ForumPost post, int userId) {
        try (Session session = openSession()) {
            Transaction tx = session.beginTransaction();
            User dbUser = session.load(User.class, userId);
            post.setUsers(dbUser);
            session.save(post);
            tx.commit();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void toggleLike(int postId, int userId) {
        try (Session session = openSession()) {
            Transaction tx = session.beginTransaction();
            Query<PostLike> query = session.createQuery(
                "FROM PostLike WHERE post.postID = :pid AND users.userID = :uid", PostLike.class);
            query.setParameter("pid", postId);
            query.setParameter("uid", userId);
            
            PostLike existingLike = query.uniqueResult();
            if (existingLike != null) {
                session.delete(existingLike);
            } else {
                User dbUser = session.load(User.class, userId);
                ForumPost post = session.load(ForumPost.class, postId);
                PostLike newLike = new PostLike(dbUser, post);
                session.save(newLike);
            }
            tx.commit();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void addComment(int postId, int userId, String content) {
        try (Session session = openSession()) {
            Transaction tx = session.beginTransaction();
            User dbUser = session.load(User.class, userId);
            ForumPost post = session.load(ForumPost.class, postId);
            PostComment comment = new PostComment(content, dbUser, post);
            session.save(comment);
            tx.commit();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public boolean updatePost(int postId, int userId, String newContent) {
        try (Session session = openSession()) {
            Transaction tx = session.beginTransaction();
            ForumPost post = session.get(ForumPost.class, postId);
            
            if (post != null && post.getUsers().getUserID() == userId) {
                post.setContent(newContent);
                session.update(post);
                tx.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deletePost(int postId, int userId, boolean isAdmin) {
        try (Session session = openSession()) {
            Transaction tx = session.beginTransaction();
            ForumPost post = session.get(ForumPost.class, postId);

            if (post != null) {
                boolean isAuthor = post.getUsers().getUserID() == userId;
                if (isAdmin || isAuthor) {
                    session.delete(post);
                    tx.commit();
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}