package com.secj3303.dao.feedback;

import java.util.Collections;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.secj3303.model.Feedback;

@Repository
public class FeedbackDaoHibernate implements FeedbackDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session openSession() {
        return sessionFactory.openSession();
    }

    @Override
    public void saveFeedback(Feedback feedback) {
        try (Session session = openSession()) {
            session.beginTransaction();
            session.save(feedback);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Feedback> getAllFeedbacks() {
        try (Session session = openSession()) {
            // HQL query to get all feedback, ordered by newest first
            String hql = "FROM Feedback f ORDER BY f.dateSubmitted DESC";
            Query<Feedback> query = session.createQuery(hql, Feedback.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<Feedback> getUserFeedback(int userID) {
        Session session = sessionFactory.openSession();
        List<Feedback> list = null;
        try {
            String hql = "FROM Feedback f WHERE f.user.userID = :uid ORDER BY f.dateSubmitted DESC";

            list = session.createQuery(hql, Feedback.class)
                          .setParameter("uid", userID)
                          .list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return list;
    }
}