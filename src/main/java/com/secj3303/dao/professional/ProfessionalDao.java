package com.secj3303.dao.professional;

import java.util.List;

import com.secj3303.model.SubContent;

public interface ProfessionalDao {

    // to add content to the sub_content table
    public void addContent(SubContent subContent);

    // edit existing content
    public void editContent(SubContent subContent);

    public List<SubContent> getUploadedResources(int professionalID);

}
