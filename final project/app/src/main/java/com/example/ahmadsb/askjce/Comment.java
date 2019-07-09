package com.example.ahmadsb.askjce;


/**
 * Created by ahmadsb on 12/31/2017.
 */

public class Comment {

    private  byte[] photo;
    private  String username;
    private String idPost;
    private String comment;
    private String timeComment;
    private String dateComment;



    //class for the database items
    public Comment(String username,String idPost,String comment,String timeComment,String dateComment,byte[]photo)
    {
        this.username = username;
        this.idPost = idPost;
        this.comment = comment;
        this.timeComment = timeComment;
        this.dateComment = dateComment;
        this.photo = photo;

    }

    public String getUsername() {
        return username;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getIdPost() {
        return idPost;
    }

    public String getComment() {
        return comment;
    }

    public String getTimeComment() {
        return timeComment;
    }

    public String getDateComment() {
        return dateComment;
    }
}