package com.secj3303.dao.activity;

import java.util.Collections;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.secj3303.model.ActivityLog;

@Repository
public class ActivityDaoHibernate implements ActivityDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session openSession() {
        return sessionFactory.openSession();
    }

    @Override
    public long getWeeklyCompletedCount(int userId) {
        try (Session session = openSession()) {
            String hql = "SELECT count(a) FROM ActivityLog a WHERE a.user.id = :uid AND a.status = 'COMPLETED'";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("uid", userId);
            Long result = query.uniqueResult();
            return (result != null) ? result : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<ActivityLog> getRecentActivities(int userId) {
        try (Session session = openSession()) {
            // Fetch latest 3 activities
            String hql = "FROM ActivityLog a WHERE a.user.id = :uid ORDER BY a.date DESC";
            Query<ActivityLog> query = session.createQuery(hql, ActivityLog.class);
            query.setParameter("uid", userId);
            query.setMaxResults(3);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}