package model;


// represents a book that can go in a bookshelf. Each book has an author, personal rating from the user,
// and read/unread status.
public class Book {
    private String name;
    private String author;
    private double rating;
    private String status;

    public Book(String name, String author, double rating, String status) {
        this.name = name;
        this.author = author;
        if (rating <= 0) {
            this.rating = 0;
        } else if (rating >= 10) {
            this.rating = 10;
        } else {
            this.rating = rating;
        }
        this.status = status;
    }

    //REQUIRES: amount >= 0
    //MODIFIES: this
    //EFFECTS: increases rating by amount. If rating would be increased above 10, rating is set to 10.
    public void increaseRating(double amount) {
        if (rating + amount <= 10) {
            rating = rating + amount;
        } else {
            rating = 10;
        }
    }

    //REQUIRES: amount >= 0
    //MODIFIES: this
    //EFFECTS: decreases rating by amount. If rating would be decreased below 0, rating is set to 0. Logs the event.
    public void decreaseRating(double amount) {
        if (rating - amount >= 0) {
            rating = rating - amount;
        } else {
            rating = 0;
        }
    }

    public void setRating(double rating) {
        this.rating = rating;
        EventLog.getInstance()
                .logEvent(new Event("Rating of " + getName() + " set to " + rating + "."));
    }

    public String getName() {
        return this.name;
    }

    public String getAuthor() {
        return this.author;
    }

    public double getRating() {
        return this.rating;
    }

    public String getStatus() {
        return this.status;
    }

    public String toString() {
        String result;
        result = "Title: " + name + ", Author: " + author + ", Rating: " + rating + ", Status: " + status;
        return result;
    }
}