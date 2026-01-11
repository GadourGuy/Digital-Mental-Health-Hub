package com.secj3303.dao.admin;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.secj3303.model.User;

@Repository
public class AdminDaoHibernate implements AdminDao {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    
    @Override
    public List<User> getAllUsers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllUsers'");
    }

    @Override
    public void updateProfessionalStatus(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateProfessionalStatus'");
    }

    @Override
    public void changeProfessionalContentStatus(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changeProfessionalContentStatus'");
    }

    @Override
    public int getAllProfessionals() {
        Session session = sessionFactory.openSession();
        int count = 0;
        try {
            // Count rows in 'users' table where the 'role' column is explicitly 'student'
            String sql = "SELECT COUNT(*) FROM users WHERE role = 'professional'";

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
    public int getProfessionalRequests() {
        Session session = sessionFactory.openSession();
        int count = 0;
        try {
            // Count rows in 'users' table where the 'role' column is explicitly 'student'
            String sql = "SELECT COUNT(*) FROM users WHERE isProfessional = true";

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
    
}
