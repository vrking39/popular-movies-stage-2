package com.example.movieproject1;

public class MovieModel {
    private int id;
    private String title;
    private String poster;
    private String overview;
    private String rating;
    private String release;

    public int getId() { return id;}
    public String getTitle() {return title;}
    public String getPoster() {return poster;}
    public String getOverview() {return overview;}
    public String getRating() {return rating;}
    public String getRelease() {return release;}

    public void setId(int newId) {
        id = newId;
    }
    public void setTitle(String newTitle){
        title = newTitle;
    }
    public void setPoster(String newPoster){
        poster = newPoster;
    }
    public void setOverview(String newOverview){
        overview = newOverview;
    }
    public void setRating(String newRating){
        rating = newRating;
    }
    public void setRelease(String newRelease){
        release = newRelease;
    }
}
