package com.secj3303.dao.Mood;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.secj3303.model.MoodEntry;

@Repository
public class MoodDaoHibernate implements MoodDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session openSession() {
        return sessionFactory.openSession();
    }

    @Override
    public long getWeeklyMoodCount(int userId) {
        try (Session session = openSession()) {
            // FIX: Added date filter to only count moods from the last 7 days
            String hql = "SELECT count(m) FROM MoodEntry m WHERE m.user.userID = :uid AND m.date > :sevenDaysAgo";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("uid", userId);
            
            // Calculate the date 7 days ago
            LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
            query.setParameter("sevenDaysAgo", sevenDaysAgo);
            
            Long result = query.uniqueResult();
            return (result != null) ? result : 0;
        } catch (Exception e) { 
            e.printStackTrace(); 
            return 0; 
        }
    }

    @Override
    public void saveMood(MoodEntry entry) {
        try (Session session = openSession()) {
            session.beginTransaction();
            session.save(entry);
            session.getTransaction().commit();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
    }

    @Override
    public List<MoodEntry> getRecentMoods(int userId) {
        try (Session session = openSession()) {
            // Get last 10 moods
            // Note: Changed m.user.id to m.user.userID to match your User model usually
            String hql = "FROM MoodEntry m WHERE m.user.userID = :uid ORDER BY m.date DESC";
            Query<MoodEntry> query = session.createQuery(hql, MoodEntry.class);
            query.setParameter("uid", userId);
            query.setMaxResults(10);
            return query.list();
        } catch (Exception e) { 
            e.printStackTrace(); 
            return Collections.emptyList(); 
        }
    }

    @Override
    public List<MoodEntry> getUsersMood() {
        Session session = sessionFactory.openSession();
        List<MoodEntry> list = null;
        try {
            String hql = "FROM MoodEntry m WHERE m.id IN " +
                         "(SELECT MAX(m2.id) FROM MoodEntry m2 GROUP BY m2.user)";

            list = session.createQuery(hql, MoodEntry.class).list();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return list;
    }
}