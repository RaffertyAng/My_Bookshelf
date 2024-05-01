package persistence;

import model.Book;
import model.Bookshelf;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Bookshelf bookshelf = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyBookshelf.json");
        try {
            Bookshelf bookshelf = reader.read();
            assertEquals(0, bookshelf.getNumBooks());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralBookshelf.json");
        try {
            Bookshelf bookshelf = reader.read();
            List<Book> books = bookshelf.getBooks();
            assertEquals(2, books.size());
            checkBooks("dracula", "stoker", 6, "read", books.get(0));
            checkBooks("frankenstein", "shelly", 8, "unread", books.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
