package persistence;

import model.Book;
import model.Bookshelf;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads bookshelf data from JSON data stored in a file
public class JsonReader {
    private String source;

    //// EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads bookshelf data from the file and returns it
    public Bookshelf read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseBookshelf(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses bookshelf data from JSON object and returns it
    private Bookshelf parseBookshelf(JSONObject jsonObject) {
        Bookshelf bookshelf = new Bookshelf();
        JSONArray jsonArray = jsonObject.getJSONArray("books");

        for (Object json : jsonArray) {
            JSONObject bookJson = (JSONObject) json;
            String name = bookJson.getString("name");
            String author = bookJson.getString("author");
            double rating = bookJson.getDouble("rating");
            String status = bookJson.getString("status");

            Book book = new Book(name, author, rating, status);
            bookshelf.addBook(book);
        }

        return bookshelf;
    }
}