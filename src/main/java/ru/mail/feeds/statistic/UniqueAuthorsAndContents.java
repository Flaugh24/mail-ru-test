package ru.mail.feeds.statistic;

import java.util.Set;

public class UniqueAuthorsAndContents {

    private Set<Long> groups;
    private Set<Long> users;
    private Set<Long> groupPhotos;
    private Set<Long> userPhotos;
    private Set<Long> movies;
    private Set<Long> posts;

    public Set<Long> getGroups() {
        return groups;
    }

    public void setGroups(Set<Long> groups) {
        this.groups = groups;
    }

    public Set<Long> getUsers() {
        return users;
    }

    public void setUsers(Set<Long> users) {
        this.users = users;
    }

    public Set<Long> getGroupPhotos() {
        return groupPhotos;
    }

    public void setGroupPhotos(Set<Long> groupPhotos) {
        this.groupPhotos = groupPhotos;
    }

    public Set<Long> getUserPhotos() {
        return userPhotos;
    }

    public void setUserPhotos(Set<Long> userPhotos) {
        this.userPhotos = userPhotos;
    }

    public Set<Long> getMovies() {
        return movies;
    }

    public void setMovies(Set<Long> movies) {
        this.movies = movies;
    }

    public Set<Long> getPosts() {
        return posts;
    }

    public void setPosts(Set<Long> posts) {
        this.posts = posts;
    }
}
