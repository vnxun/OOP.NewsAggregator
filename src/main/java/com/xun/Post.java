package com.xun;

import javafx.fxml.FXMLLoader;

public class Post extends Article{
    public Post(String link, String source, String content, String author, String date, String hashtags){
        super(link, source, content, author, date, hashtags);
    }
    
    @Override
    protected void setCard(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/Post.fxml"));
            card = loader.load();
            PostController controller = loader.getController();
            controller.setData(this);   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
