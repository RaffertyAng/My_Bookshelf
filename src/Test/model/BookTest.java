package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookTest {
    private Book testBook;

    @BeforeEach
    void runBefore() {
        testBook = new Book("dracula", "bram stoker", 7.5, "read");
    }

    @Test
    void testConstructor() {
        assertEquals("dracula", testBook.getName());
        assertEquals("bram stoker", testBook.getAuthor());
        assertTrue(testBook.getRating() > 0);
        assertEquals(7.5, testBook.getRating());
        assertEquals("read", testBook.getStatus());
    }

    @Test
    void testConstructorNegativeRating() {
        testBook = new Book("dracula", "bram stoker", -0.5, "unread");
        assertEquals("dracula", testBook.getName());
        assertEquals("bram stoker", testBook.getAuthor());
        assertEquals(0, testBook.getRating());
        assertEquals("unread", testBook.getStatus());
    }

    @Test
    void testConstructorRatingAboveTen() {
        testBook = new Book("dracula", "bram stoker", 10.5, "read");
        assertEquals("dracula", testBook.getName());
        assertEquals("bram stoker", testBook.getAuthor());
        assertEquals(10, testBook.getRating());
        assertEquals("read", testBook.getStatus());
    }

    @Test
    void testIncreaseRating() {
        testBook.increaseRating(2.5);
        assertEquals(10, testBook.getRating());
    }

    @Test
    void testDecreaseRating() {
        testBook.decreaseRating(2.5);
        assertEquals(5, testBook.getRating());
    }

    @Test
    void testIncreaseRatingMultiple() {
        testBook.increaseRating(1.5);
        assertEquals(9, testBook.getRating());
        testBook.increaseRating(1);
        assertEquals(10, testBook.getRating());
    }

    @Test
    void testDecreaseRatingMultiple() {
        testBook.decreaseRating(6);
        assertEquals(1.5, testBook.getRating());
        testBook.decreaseRating(1.5);
        assertEquals(0, testBook.getRating());
    }

    @Test
    void testIncreaseRatingPastTen() {
        testBook.increaseRating(3);
        assertEquals(10, testBook.getRating());
    }

    @Test
    void testDecreaseRatingBelowZero() {
        testBook.decreaseRating(8);
        assertEquals(0, testBook.getRating());
    }
}