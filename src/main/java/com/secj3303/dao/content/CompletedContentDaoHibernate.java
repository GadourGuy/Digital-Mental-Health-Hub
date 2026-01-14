package com.secj3303.dao.content;

import java.util.Collections;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.secj3303.model.CompletedContent;

@Repository
public class CompletedContentDaoHibernate implements CompletedContentDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void saveCompletedContent(CompletedContent entry) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(entry);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasUserCompleted(int userId, int contentId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT count(c) FROM CompletedContent c WHERE c.users.userID = :uid AND c.contentID = :cid";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("uid", userId);
            query.setParameter("cid", contentId);
            return query.uniqueResult() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

	@Override
    public List<Integer> getCompletedContentIds(int userId) {
        try (Session session = sessionFactory.openSession()) {
           
            String hql = "SELECT c.contentID FROM CompletedContent c WHERE c.users.userID = :uid";
            Query<Integer> query = session.createQuery(hql, Integer.class);
            query.setParameter("uid", userId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}