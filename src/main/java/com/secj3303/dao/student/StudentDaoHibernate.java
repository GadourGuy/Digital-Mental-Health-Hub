package com.secj3303.dao.student;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.secj3303.model.ProfessionalRequest;

@Repository
public class StudentDaoHibernate implements StudentDao {

     @Autowired
    private SessionFactory sessionFactory;

    @Override
    public boolean checkUserRequestExists(int userID) {
        Session session = sessionFactory.openSession();
        boolean exists = false;
        try {
            // HQL Query to count rows where the user ID matches
            String hql = "SELECT COUNT(r) FROM ProfessionalRequest r WHERE r.user.userID = :uid";
            
            Long count = (Long) session.createQuery(hql)
                                       .setParameter("uid", userID)
                                       .uniqueResult();
            
            
            exists = (count != null && count > 0);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return exists;
    }

    @Override
    public void addProfessionalRequest(ProfessionalRequest professionalRequest) {
        Session session = sessionFactory.openSession();
        
        
        try {
            
            session.save(professionalRequest);
            String hql = "UPDATE User u SET u.isProfessional = true WHERE u.userID = :uid";
            
            session.createQuery(hql)
                   .setParameter("uid", professionalRequest.getUser().getUserID())
                   .executeUpdate();
        
            session.beginTransaction().commit();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
