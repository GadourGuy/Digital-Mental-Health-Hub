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

    @Override
    public void editContent(SubContent subContent) {
        Session session = sessionFactory.openSession();
    try {
        SubContent existingContent = session.get(SubContent.class, subContent.getContentID());

        if (existingContent != null) {
            // 2. Update ONLY the fields allowed to be edited
            existingContent.setContentTitle(subContent.getContentTitle());
            existingContent.setContentURL(subContent.getContentURL());
            existingContent.setType(subContent.getType());
            existingContent.setContentCategory(subContent.getContentCategory());
            existingContent.setDescription(subContent.getDescription()); 
            existingContent.setStatus("pending");            
            existingContent.setEdited(true); 
            
            session.update(existingContent);
            session.beginTransaction().commit();
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        session.close();
    }
    }
    
}
