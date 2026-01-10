package com.secj3303.dao.professional;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.secj3303.model.SubContent;


@Repository
public class ProfessionalDaoHibernate implements ProfessionalDao{

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public int getStudents() {
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
    public void addContent(SubContent subContent) {
        Session session = sessionFactory.openSession();
        
        try {
            session.beginTransaction();
            session.save(subContent);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public List<SubContent> getUploadedResources(int professionalID) {
        Session session = sessionFactory.openSession();
        List<SubContent> list = null;
        try {
            String sql = "SELECT * FROM sub_contents WHERE professionalID = :profId";
            
            list = session.createNativeQuery(sql, SubContent.class)
                          .setParameter("profId", professionalID)
                          .list();
                          
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return list;
    }
    
}
