package com.example.marko.myholidays;

/**
 * Created by marko on 02/08/2016.
 */
public class Holiday {

    private int id;
    private String title;
    private String place;
    private String date;
    private String image;

    public Holiday(int _id, String _title, String _place, String _date, String _image){
        this.id = _id;
        this.title = _title;
        this.place = _place;
        this.date = _date;
        this.image = _image;
    }
    public Holiday( String _title, String _place, String _date, String _image){
        this.title = _title;
        this.place = _place;
        this.date = _date;
        this.image = _image;
    }

    public Holiday () {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
