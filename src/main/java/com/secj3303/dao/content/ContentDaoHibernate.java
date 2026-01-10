package com.secj3303.dao.content;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.secj3303.model.Category;
import com.secj3303.model.SubContent;

@Repository
public class ContentDaoHibernate implements ContentDao{

    @Autowired
    private SessionFactory sessionFactory;

    
    @Override
    public List<Category> getContentCategories() {
        Session session = sessionFactory.openSession();
        List<Category> list = null;
        try {
            list = session.createQuery("from Category", Category.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return list;
    }

    @Override
    public List<SubContent> getAllSubContents() {
        Session session = sessionFactory.openSession();
        List<SubContent> subContents = session.createQuery("from SubContent", SubContent.class).list();
        session.close();
        return subContents;
    }

    @Override
    public List<SubContent> getSubContents(int categoryID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSubContents'");
    }

    @Override
    public SubContent getSubContent(int contentID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSubContent'");
    }

    @Override
    public int getCompletedContentNumbers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCompletedContentNumbers'");
    }

    @Override
    public int GetProfessionalCompletedContent(int professionalID) {
        Session session = sessionFactory.openSession();
        int count = 0;
        try {
            
            String sql = "SELECT COUNT(*) FROM completed_contents WHERE contentID IN " +
                         "(SELECT contentID FROM sub_contents WHERE professionalID = :profId)";

            Number result = (Number) session.createNativeQuery(sql)
                                            .setParameter("profId", professionalID)
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
    public void uploadContent(SubContent uploadedContent) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.persist(uploadedContent);
        tx.commit();
        session.close();
    }

    @Override
    public int getPendingContent(int professionalID) {
        Session session = sessionFactory.openSession();
        int count = 0;
        try {
            
            String sql = "SELECT COUNT(*) FROM sub_contents WHERE professionalID = :profId AND status = false";

            Number result = (Number) session.createNativeQuery(sql)
                                            .setParameter("profId", professionalID)
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
    public int getApprovedContent(int professionalID) {
        Session session = sessionFactory.openSession();
        int count = 0;
        try {
            
            String sql = "SELECT COUNT(*) FROM sub_contents WHERE professionalID = :profId AND status = true";

            Number result = (Number) session.createNativeQuery(sql)
                                            .setParameter("profId", professionalID)
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
    public int getCategoryID(String category) {
        Session session = sessionFactory.openSession();
        int categoryID = (int) session.createQuery("select categoryID from Category where content_title = :category").setParameter("category", category).uniqueResult();
        session.close();
        return categoryID;
    }
    
}
