package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookshelfTest {
    private Bookshelf bookshelf;
    private Book book1;
    private Book book2;

    @BeforeEach
    void runBefore() {
        bookshelf = new Bookshelf();
        book1 = new Book("dracula", "stoker", 5, "read");
        book2 = new Book("frankenstein", "shelly", 3, "unread");
    }

    @Test
    void testConstructor() {
        assertEquals(0, bookshelf.getNumBooks());
    }

    @Test
    void testAddBooksNotInShelf() {
        bookshelf.addBook(book1);
        assertEquals(1, bookshelf.getNumBooks());
        bookshelf.addBook(book2);
        assertEquals(2, bookshelf.getNumBooks());
    }

    @Test
    void addBookAlreadyInShelf() {
        bookshelf.addBook(book1);
        assertEquals(1, bookshelf.getNumBooks());
        bookshelf.addBook(book1);
        assertEquals(1, bookshelf.getNumBooks());
    }

    @Test
    void testRemoveBookNotInShelf() {
        bookshelf.addBook(book1);
        bookshelf.addBook(book2);
        bookshelf.removeBook("batman");
        assertEquals(2, bookshelf.getNumBooks());
    }

    @Test
    void testRemoveBookInShelf() {
        bookshelf.addBook(book1);
        bookshelf.addBook(book2);
        bookshelf.removeBook("dracula");
        assertEquals(1, bookshelf.getNumBooks());
        bookshelf.removeBook("frankenstein");
        assertEquals(0, bookshelf.getNumBooks());
    }

}