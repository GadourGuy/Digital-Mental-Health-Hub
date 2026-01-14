package com.secj3303.dao.student;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.secj3303.model.ProfessionalRequest;
import com.secj3303.model.User;

@Repository
public class StudentDaoHibernate implements StudentDao {

     @Autowired
    private SessionFactory sessionFactory;

    @Override
    public ProfessionalRequest checkUserRequestExists(int userID) {
        Session session = sessionFactory.openSession();
        ProfessionalRequest request = null;
        try {
            String hql = "FROM ProfessionalRequest r WHERE r.user.userID = :uid";
            request = (ProfessionalRequest) session.createQuery(hql)
                                                   .setParameter("uid", userID)
                                                   .uniqueResult();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return request;
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
    

    @Override
    public int getNumOfStudents() {
        Session session = sessionFactory.openSession();
        int count = 0;
        try {
            // Count rows in 'users' table where the 'role' column is explicitly 'student'
            String sql = "SELECT COUNT(*) FROM users WHERE role = 'student'";

            Number result = (Number) session.createNativeQuery(sql)
                                            .getSingleResult();

            if (result != null) {
                count = result.intValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return count;
    }


    @Override
    public List<User> getAllStudents() {
        Session session = sessionFactory.openSession();
        List<User> list = null;
        try {
            String hql = "SELECT new User(u.userID, u.name, u.email) FROM User u WHERE u.role = 'student'";
            list = session.createQuery(hql, User.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return list;
    }

    @Override
    public void updateProfessionalRequest(ProfessionalRequest professionalRequest) {
        Session session = sessionFactory.openSession();
        
        try {
            session.update(professionalRequest);
            session.beginTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }    
    }
}
