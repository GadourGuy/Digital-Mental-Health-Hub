package com.secj3303.dao.professional;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.secj3303.model.ProfessionalRequest;
import com.secj3303.model.SubContent;


@Repository
public class ProfessionalDaoHibernate implements ProfessionalDao{

    @Autowired
    private SessionFactory sessionFactory;

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
            String sql = "SELECT COUNT(*) FROM professional_requests WHERE status = 'pending'";

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

    public List<ProfessionalRequest> getAllPendingProfessionalRequests() {
        Session session = sessionFactory.openSession();
        List<ProfessionalRequest> list = null;
        try {
            String sql = "FROM ProfessionalRequest pr JOIN FETCH pr.user WHERE pr.status = 'pending'";
            list = session.createQuery(sql, ProfessionalRequest.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return list;
    }

    @Override
    public ProfessionalRequest getSingleProfessionalRequest(int requestID) {
        Session session = sessionFactory.openSession();
        ProfessionalRequest request = null;
        try {
            // JOIN FETCH loads the 'user' object immediately with the request.
            // .uniqueResult() returns just one object instead of a list.
            String hql = "FROM ProfessionalRequest pr JOIN FETCH pr.user WHERE pr.requestID = :id";
            
            request = session.createQuery(hql, ProfessionalRequest.class)
                             .setParameter("id", requestID)
                             .uniqueResult();
                             
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return request;
    }
    
    
}
