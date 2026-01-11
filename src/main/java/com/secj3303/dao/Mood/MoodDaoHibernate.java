package com.secj3303.dao.Mood;

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
            String hql = "SELECT count(m) FROM MoodEntry m WHERE m.user.id = :uid";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("uid", userId);
            Long result = query.uniqueResult();
            return (result != null) ? result : 0;
        } catch (Exception e) { e.printStackTrace(); return 0; }
    }

    // --- NEW METHODS ---

    @Override
    public void saveMood(MoodEntry entry) {
        try (Session session = openSession()) {
            session.beginTransaction();
            session.save(entry);
            session.getTransaction().commit();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public List<MoodEntry> getRecentMoods(int userId) {
        try (Session session = openSession()) {
            // Get last 10 moods
            String hql = "FROM MoodEntry m WHERE m.user.id = :uid ORDER BY m.date DESC";
            Query<MoodEntry> query = session.createQuery(hql, MoodEntry.class);
            query.setParameter("uid", userId);
            query.setMaxResults(10);
            return query.list();
        } catch (Exception e) { e.printStackTrace(); return Collections.emptyList(); }
    }
}