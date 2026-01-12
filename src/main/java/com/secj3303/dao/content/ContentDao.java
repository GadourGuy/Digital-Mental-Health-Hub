package com.secj3303.dao.content;

import java.util.List;

import com.secj3303.model.Category;
import com.secj3303.model.SubContent;


public interface ContentDao {

    // return the content categories in the system
    public List<Category> getContentCategories();


    // return all contents uploaded by the professionals
    public List<SubContent> getAllSubContents();

    // return the contents uploaded by the professionals based on category
    public List<SubContent> getSubContents(int categoryID);
    
    // return subcontent info from the db based on its id
    public SubContent getSubContentByID(int contentID);
    
    // returns the total number of contents that has been completed
    public int getCompletedContentNumbers();

    // returns the number of content completions based on the professional id provided
    // it returns the total nuumber of completed content uploaded by the professional in the id
    public int GetProfessionalCompletedContent(int professionalID);

    // get the number of contents where status is false based on the professional id
    public int getPendingContent(int professionalID);

    // here it gets all the pending content not from the professional id
    public int getAllPendingContent();

    // get the number of content where status is true based on profession id
    public int getApprovedContent(int professionalID);


    // for the professional to upload resources.
    public void uploadContent(SubContent uploadedContent);

    // returning the category id from the name
    public int getCategoryID(String category);
    
    // returning the category title from its id
    public String getCategoryTitleByID(int categoryID);

    // delete the content based on its id
    public void deleteContentByID(int contentID);
}
