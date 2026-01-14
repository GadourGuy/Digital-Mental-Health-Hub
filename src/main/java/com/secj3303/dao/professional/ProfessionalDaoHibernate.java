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

    
    
}
