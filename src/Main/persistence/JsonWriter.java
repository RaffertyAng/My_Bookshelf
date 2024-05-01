package persistence;

import model.Book;
import model.Bookshelf;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// Represents a writer that writes bookshelf data to a JSON file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens the writer; throws FileNotFoundException if the destination file cannot be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes bookshelf data to the file
    public void write(Bookshelf bookshelf) {
        JSONObject json = bookshelfToJson(bookshelf);
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes the writer
    public void close() {
        writer.close();
    }

    // EFFECTS: converts bookshelf to JSON object
    private JSONObject bookshelfToJson(Bookshelf bookshelf) {
        JSONObject json = new JSONObject();
        JSONArray booksArray = new JSONArray();

        for (Book book : bookshelf.getBooks()) {
            JSONObject bookJson = new JSONObject();
            bookJson.put("name", book.getName());
            bookJson.put("author", book.getAuthor());
            bookJson.put("rating", book.getRating());
            bookJson.put("status", book.getStatus());
            booksArray.put(bookJson);
        }

        json.put("books", booksArray);
        return json;
    }

    // MODIFIES: this
    // EFFECTS: writes string to the file
    private void saveToFile(String json) {
        writer.print(json);
    }
}