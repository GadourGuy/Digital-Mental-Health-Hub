package com.secj3303.dao.professional;

import com.secj3303.model.SubContent;

public interface ProfessionalDao {
    // to return a list of students
    public int getStudents();

    // to add content to the sub_content table
    public void addContent(SubContent subContent);

}
