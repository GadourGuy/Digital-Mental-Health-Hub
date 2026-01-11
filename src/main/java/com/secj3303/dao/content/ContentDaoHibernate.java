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

    // For the time being/beginning: we explicitly open/close session
    private Session openSession() {
        return sessionFactory.openSession();
    }
    @Override
    public List<Category> getContentCategories() {
        Session session = openSession();
        List<Category> categories = session.createQuery("from Category").list();
        session.close();
        return categories;
    }

    @Override
    public List<SubContent> getAllSubContents() {
        Session session = openSession();
        List<SubContent> subContents = session.createQuery("from SubContent").list();
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
        Session session = openSession();
        SubContent content = session.get(SubContent.class, contentID);
        session.close();
        return content;
    }

    @Override
    public int getCompletedContentNumbers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCompletedContentNumbers'");
    }

    @Override
    public int GetProfessionalCompletedContent(int professionalID) {
        Session session = openSession();
        Long count = (Long) session.createQuery("select count(*) from CompletedContent where users.userID in (select userID from User where role = 'STUDENT') and contentID in (select contentID from SubContent where professional.userID = :professionalID)").setParameter("professionalID", professionalID).uniqueResult();
        session.close();
        return count.intValue();
    }

    @Override
    public void uploadContent(SubContent uploadedContent) {
        Session session = openSession();
        Transaction tx = session.beginTransaction();
        session.persist(uploadedContent);
        tx.commit();
        session.close();
    }

    @Override
    public int getPendingContent(int professionalID) {
        Session session = openSession();
        Long count = (Long) session.createQuery("select count(*) from SubContent where status = false and professional.userID = :professionalID").setParameter("professionalID", professionalID).uniqueResult();
        session.close();
        return count.intValue();
    }

    @Override
    public int getCategoryID(String category) {
        Session session = openSession();
        int categoryID = (int) session.createQuery("select categoryID from Category where content_title = :category").setParameter("category", category).uniqueResult();
        session.close();
        return categoryID;
    }

    // method for the forum posts updating
    @Override
    public void updateContent(SubContent content) {
        Session session = openSession();
        Transaction tx = session.beginTransaction();
        session.update(content);
        tx.commit();
        session.close();
    }

    // method for the forum posts deletion
    @Override
    public void deleteContent(int contentID) {
        Session session = openSession();
        Transaction tx = session.beginTransaction();
        SubContent content = session.get(SubContent.class, contentID);
        if (content != null) {
            session.delete(content);
        }
        tx.commit();
        session.close();
    }
    
    
}
