package com.secj3303.dao.assessment;

import java.util.Collections;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.secj3303.model.AssessmentEntry;

@Repository
public class AssessmentDaoHibernate implements AssessmentDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session openSession() {
        return sessionFactory.openSession();
    }

    @Override
    public void saveAssessment(AssessmentEntry assessment) {
        try (Session session = openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(assessment);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<AssessmentEntry> getAssessmentHistory(int userId) {
        try (Session session = openSession()) {
            // Fetch all assessments for the user, ordered by newest first
            String hql = "FROM AssessmentEntry a WHERE a.user.userID = :uid ORDER BY a.date DESC";
            Query<AssessmentEntry> query = session.createQuery(hql, AssessmentEntry.class);
            query.setParameter("uid", userId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public AssessmentEntry getLastAssessment(int userId) {
        try (Session session = openSession()) {
            // Fetch only the SINGLE most recent assessment
            String hql = "FROM AssessmentEntry a WHERE a.user.userID = :uid ORDER BY a.date DESC";
            Query<AssessmentEntry> query = session.createQuery(hql, AssessmentEntry.class);
            query.setParameter("uid", userId);
            query.setMaxResults(1);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}