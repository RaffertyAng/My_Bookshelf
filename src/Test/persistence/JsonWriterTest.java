package persistence;

import model.Book;
import model.Bookshelf;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Bookshelf bookshelf = new Bookshelf();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWorkroom() {
        try {
            Bookshelf bookshelf = new Bookshelf();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyBookshelf.json");
            writer.open();
            writer.write(bookshelf);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyBookshelf.json");
            bookshelf = reader.read();
            assertEquals(0, bookshelf.getNumBooks());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            Bookshelf bookshelf = new Bookshelf();
            bookshelf.addBook(new Book("batman", "wayne", 5, "read"));
            bookshelf.addBook(new Book("superman", "kent", 2, "unread"));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralBookshelf.json");
            writer.open();
            writer.write(bookshelf);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralBookshelf.json");
            bookshelf = reader.read();
            List<Book> books = bookshelf.getBooks();
            assertEquals(2, books.size());
            checkBooks("batman", "wayne", 5, "read", books.get(0));
            checkBooks("superman", "kent", 2, "unread", books.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}