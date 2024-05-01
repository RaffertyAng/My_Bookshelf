package persistence;

import model.Book;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkBooks(String name, String author, int rating, String status, Book book) {
        assertEquals(name, book.getName());
        assertEquals(author, book.getAuthor());
        assertEquals(rating, book.getRating());
        assertEquals(status, book.getStatus());
    }
}