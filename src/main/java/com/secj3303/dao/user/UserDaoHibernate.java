package com.secj3303.dao.user;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.secj3303.model.User;

@Repository
public class UserDaoHibernate implements UserDao {
    
    @Autowired
    private SessionFactory sessionFactory;

    // For the time being/beginning: we explicitly open/close session
    private Session openSession() {
        return sessionFactory.openSession();
    }

    // --- IMPLEMENTED: Required for Login Logic ---
    @Override
    public User getUserByEmail(String email) {
        // We use try-with-resources to ensure the session closes automatically
        try (Session session = openSession()) {
            // HQL: Select the user where the email matches the parameter
            String hql = "FROM User u WHERE u.email = :email";
            
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("email", email);
            
            // returns the found User or null if no user exists
            return query.uniqueResult(); 
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // --- IMPLEMENTED: Required for Signup Logic (Bonus) ---
    @Override
    public void insertUser(User user) {
        try (Session session = openSession()) {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- Unimplemented Stubs ---
    @Override
    public User getUser(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUser'");
    }

    @Override
    public void updateUser(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public void deleteUser(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }

    @Override
    public int getTotalUsers() {
        throw new UnsupportedOperationException("Unimplemented method 'getTotalUsers'");
    }
}