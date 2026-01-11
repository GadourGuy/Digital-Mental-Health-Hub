package com.secj3303.dao.admin;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.secj3303.model.ProfessionalRequest;
import com.secj3303.model.SubContent;
import com.secj3303.model.User;

@Repository
public class AdminDaoHibernate implements AdminDao {
    
    @Autowired
    private SessionFactory sessionFactory;
    

    @Override
    public void changeProfessionalContentStatus(int id, String status, String message) {
        Session session = sessionFactory.openSession();
    try {
        SubContent content = session.get(SubContent.class, id);
        
        if (content != null) {
            content.setStatus(status);
            if (message != null && !message.isEmpty()) {
                content.setRejectionReason(message); 
            }
            session.update(content);
            session.beginTransaction().commit();
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        session.close();
    }
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


    public List<User> getUsersRequesting(List<ProfessionalRequest> professionalRequests) {
        Session session = sessionFactory.openSession();
        List<User> list = null;
        try {
            String sql = "SELECT new User(u.userID, u.name, u.email) " +
                         "FROM User u " +
                         "JOIN ProfessionalRequest pr ON u.userID = pr.user.userID " +
                         "WHERE pr.status = 'pending'";

            list = session.createQuery(sql, User.class).list();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return list;
    }

    
    public void editPendingRequest(int requestID, String status, String rejectionMessage) {
        Session session = sessionFactory.openSession();
        
        try {
            ProfessionalRequest request = session.get(ProfessionalRequest.class, requestID);

            if (request != null) {
                
                request.setStatus(status);
                if ("rejected".equalsIgnoreCase(status) && rejectionMessage != null) {
                    request.setRejectionReason(rejectionMessage);
                    User user = request.getUser();
                     user.setRole("student");
                     session.update(user);
                } else {
                    // Clear message if approved
                    request.setRejectionReason(null); 
                }
                
                // D. If Approved, you might want to update the User role automatically?
                // Optional logic:
                if ("approved".equalsIgnoreCase(status)) {
                     User user = request.getUser();
                     user.setRole("professional"); // Or set isProfessional=true
                     session.update(user);
                }

                session.update(request);
            }

            session.beginTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
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

    @Override
    public List<SubContent> getAllPendingContent() {
        Session session = sessionFactory.openSession();
    List<SubContent> list = null;
    try {
        
        String sql = "FROM SubContent s JOIN FETCH s.professional WHERE s.status = 'pending'";
        
        list = session.createQuery(sql, SubContent.class).list();
        
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        session.close();
    }
    return list;
    }
    
}
