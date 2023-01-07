package com.example.oop_coursework_sem3;

import java.util.Objects;

public class VideoDisc extends Rental {
    private String discName;
    private String genre;
    private Integer releaseYear;
    private String director;

    public VideoDisc(Integer discId ,String discName, String genre, Integer releaseYear, String director) {
        super(discId);
        this.discName = discName;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.director = director;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoDisc videoDisc = (VideoDisc) o;

        return discName.equals(videoDisc.discName) && genre.equals(videoDisc.genre)
                && releaseYear.equals(videoDisc.releaseYear) && director.equals(videoDisc.director);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDiscId(), discName, genre, releaseYear, director);
    }

    public String getDiscName() {
        return discName;
    }

    public void setDiscName(String discName) {
        this.discName = discName;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    @Override
    public String toString() {
        return discName +
                " _ " + genre +
                " _ " + releaseYear +
                " _ " + director +
                " _ ";
    }

}
