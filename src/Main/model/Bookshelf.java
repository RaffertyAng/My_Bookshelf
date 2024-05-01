package model;

import java.util.ArrayList;
import java.util.List;


//represents a bookshelf that contains books. Each bookshelf has a list of every book in the bookshelf.
public class Bookshelf {
    private String name;
    private int numBooks;
    private List<Book> books;

    public Bookshelf() {
        this.name = "YOUR BOOKSHELF";
        this.books = new ArrayList<>();
        this.numBooks = 0;
    }

    //EFFECTS: returns books
    public List<Book> getBooks() {
        return books;
    }

    // MODIFIES: this
    // EFFECTS: adds book to bookshelf if book is not already in bookshelf. Logs the event.
    public boolean addBook(Book newBook) {
        boolean alreadyInList = false;

        for (Book book : books) {
            if (book.getName().equals(newBook.getName())) {
                alreadyInList = true;
                break;
            }
        }
        if (!alreadyInList) {
            books.add(newBook);
            numBooks += 1;
            EventLog.getInstance().logEvent(new Event("Added " + newBook.getName() + " to bookshelf."));
            return true;
        } else {
            return false;
        }
    }

    // MODIFIES: this
    // EFFECTS: removes book from bookshelf by name if there is a book with that name in the bookshelf. Logs the event.
    public boolean removeBook(String bookName) {
        Book bookToRemove = null;
        for (Book book : books) {
            if (book.getName().equalsIgnoreCase(bookName)) {
                bookToRemove = book;
                break;
            }
        }
        if (bookToRemove != null) {
            books.remove(bookToRemove);
            numBooks -= 1;
            EventLog.getInstance()
                    .logEvent(new Event("Removed " + bookName + " from bookshelf."));
            return true;
        }
        return false;
    }

    //EFFECTS: returns name of bookshelf
    public String getName() {
        return name;
    }

    //EFFECTS: returns number of books in bookshelf
    public int getNumBooks() {
        return numBooks;
    }

    //EFFECTS: returns true if book is in bookshelf, else returns false
    public boolean isInBookshelf(String name) {
        boolean inList = false;

        for (Book book : getBooks()) {
            if (book.getName().equals(name)) {
                inList = true;
                break;
            }
        }
        return inList;
    }

    //REQUIRES: entry is in list
    //MODIFIES: books
    //EFFECTS: moves the entry to the front of the list
    public void moveToFront(Book entry) {
        books.remove(entry);
        books.add(0, entry);
    }
}