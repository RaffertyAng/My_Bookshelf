package ui;

import model.Book;
import model.Bookshelf;
import model.Event;
import model.EventLog;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

//the GUI form of the BookshelfApp
public class BookshelfGUI extends JFrame {

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 400;
    private static final String JSON_STORE = "./data/bookshelf.json";

    private Bookshelf bookshelf;
    private String title;
    private String author;
    private double rating;
    private String status;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private JPanel menu;
    private JPanel inputFieldPanel;
    private JScrollPane scrollPane;
    private JLabel prompt;
    private JLabel message;
    private JLabel bookImage;
    private JTextField inputField;
    private JButton confirmButton;

    //EFFECTS: initializes a new GUI
    public BookshelfGUI() {
        super("Bookshelf");
        initializeGraphics();

        bookshelf = new Bookshelf();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    //EFFECTS: initializes visual elements of GUI
    private void initializeGraphics() {
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        createMenu();
        add(menu, BorderLayout.SOUTH);
        displayInitialMessage();

        bookImage = new JLabel();
        add(bookImage, BorderLayout.WEST);
        displayBookshelfImage();

        setVisible(true);
    }

    //MODIFIES: this
    //EFFECTS: displays the initial message
    private void displayInitialMessage() {
        message = new JLabel("Welcome to your bookshelf. What would you like to do?",SwingConstants.CENTER);
        add(message);
    }

    //EFFECTS: generates the menu of the bookshelf's functions
    private void createMenu() {
        menu = new JPanel(new FlowLayout());

        // Instead of using lambda expressions, I could have overridden the actionPerformed method from ActionListener
        // for all 8 of my functions.
        menu.add(addButton("Add", e -> initializeBook()));
        menu.add(addButton("Remove", e -> doRemoveBook()));
        menu.add(addButton("View", e -> viewBooks()));
        menu.add(addButton("Re-rate", e -> modifyRating()));
        menu.add(addButton("Save", e -> saveBookshelf()));
        menu.add(addButton("Load", e -> loadBookshelf()));
        menu.add(addButton("Quit", e -> quitProgram()));
    }

    //EFFECTS: adds a button to the bookshelf and sets the label and actionListener
    private JButton addButton(String label, ActionListener actionListener) {
        JButton button = new JButton(label);
        button.addActionListener(actionListener);
        button.setPreferredSize(new Dimension(100, 50));
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 15));
        button.setBackground(new Color(200, 200, 200));
        return button;
    }

    // EFFECTS: adds book to bookshelf if book is not already in bookshelf.
    private void initializeBook() {
        clearPanels();
        setName();
    }

    //
    private void quitProgram() {
        EventLog log = EventLog.getInstance();
        for (Event e : log) {
            System.out.println(e);
        }
        System.exit(0);
    }

    //MODIFIES: this
    //EFFECTS:adds book to bookshelf based on the title, author, rating, and status fields
    // and displays appropriate message
    private void doAddBook() {
        remove(message);

        Book newBook = new Book(title, author, rating, status);
        boolean success = bookshelf.addBook(newBook);

        if (success) {
            message = new JLabel("Successfully added the book to bookshelf.",SwingConstants.CENTER);
            add(message);
        } else {
            message = new JLabel("A book with that name is already in the bookshelf.",SwingConstants.CENTER);
            add(message);
        }
        revalidate();
        repaint();
    }

    //MODIFIES: this
    //EFFECTS: sets the title field based on user input
    private void setName() {
        generateInputField("Title: ", e -> {
            title = inputField.getText();
            remove(inputFieldPanel);
            setAuthor();
        });
    }

    //MODIFIES: this
    //EFFECTS: sets the author field based on user input
    private void setAuthor() {
        generateInputField("Author: ", e -> {
            author = inputField.getText();
            remove(inputFieldPanel);
            setRating();
        });
    }

    //MODIFIES: this
    //EFFECTS: sets the rating field based on user input
    private void setRating() {
        generateInputField("Rating (1 - 10)", e -> {
            String ratingText = inputField.getText();

            if (isValidDouble(ratingText)) {
                rating = Double.parseDouble(ratingText);
                remove(inputFieldPanel);
                setStatus();
            } else {
                prompt.setText("Invalid input, entor a rating from 1 - 10:");
                inputField.setText("");
            }
        });
    }

    //EFFECTS: returns true if input is a valid double, else returns false
    private boolean isValidDouble(String input) {
        try {
            double result = Double.parseDouble(input);
            if (result < 0 || result > 10) {
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //MODIFIES: this
    //EFFECTS: sets the status field based on user input
    private void setStatus() {
        generateInputField("read or unread?", e -> {
            String statusText = inputField.getText();
            if (statusText.equals("read") || statusText.equals("unread")) {
                status = statusText;
                remove(inputFieldPanel);
                doAddBook();
            } else {
                prompt.setText("Try again, read or unread?");
                inputField.setText("");
            }
        });
    }

    //MODIFIES: this
    //EFFECTS: removes a book based on user input
    private void doRemoveBook() {
        clearPanels();

        generateInputField("Which book would you like to remove?", e -> {
            boolean success = bookshelf.removeBook(inputField.getText().toLowerCase());
            if (success) {
                message = new JLabel("Successfully removed book from bookshelf.",SwingConstants.CENTER);
                add(message);
            } else {
                message = new JLabel("Book not found in bookshelf.",SwingConstants.CENTER);
                add(message);
            }
            remove(inputFieldPanel);
            revalidate();
            repaint();
        });
    }

    //EFFECTS: displays panel that shows all books
    private void viewBooks() {
        clearPanels();

        JPanel viewPanel = new JPanel();
        viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.Y_AXIS));

        message = new JLabel("Your books:");
        viewPanel.add(message);

        List<Book> books = bookshelf.getBooks();
        for (Book book : books) {
            JLabel bookLabel = new JLabel(book.toString());
            viewPanel.add(bookLabel);
        }

        scrollPane = new JScrollPane(viewPanel);
        add(scrollPane, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    //EFFECTS: prompts the user to choose a book name and calls setNewRating() if book is in the bookshelf
    private void modifyRating() {
        clearPanels();

        generateInputField("Which book would you like to change rating for?", e -> {
            String input = inputField.getText().toLowerCase();
            boolean isInBookshelf = bookshelf.isInBookshelf(input);
            if (isInBookshelf) {
                setNewRating(input);
            } else {
                message = new JLabel("Book not found in bookshelf.",SwingConstants.CENTER);
                add(message);
                remove(inputFieldPanel);
                revalidate();
                repaint();
            }
        });
    }

    //MODIFIES: bookshelf
    //EFFECTS: changes the rating of a book in bookshelf based on user input and moves the book to front
    // of the list
    private void setNewRating(String name) {
        clearPanels();

        generateInputField("New rating (1 - 10): ", e -> {
            String ratingText = inputField.getText();

            if (isValidDouble(ratingText)) {
                double newRating = Double.parseDouble(ratingText);
                for (Book book : bookshelf.getBooks()) {
                    if (book.getName().equals(name)) {
                        bookshelf.moveToFront(book);
                        book.setRating(newRating);
                    }
                }
                remove(inputFieldPanel);
                message = new JLabel("Rating has been changed.",SwingConstants.CENTER);
                add(message);
                revalidate();
                repaint();
            } else {
                prompt.setText("Invalid input, entor a rating from 1 - 10:");
                inputField.setText("");
            }
        });
    }

    // EFFECTS: saves the bookshelf to file
    private void saveBookshelf() {
        clearPanels();

        if (scrollPane != null) {
            remove(scrollPane);
        }
        try {
            jsonWriter.open();
            jsonWriter.write(bookshelf);
            jsonWriter.close();
            message = new JLabel("Saved " + bookshelf.getName() + " to " + JSON_STORE,SwingConstants.CENTER);
            add(message);
        } catch (FileNotFoundException e) {
            message = new JLabel("Unable to write to file: " + JSON_STORE,SwingConstants.CENTER);
            add(message);
        }
        revalidate();
        repaint();
    }

    // MODIFIES: this
    // EFFECTS: loads bookshelf from file
    private void loadBookshelf() {
        clearPanels();

        if (scrollPane != null) {
            remove(scrollPane);
        }
        try {
            bookshelf = jsonReader.read();
            message = new JLabel("Loaded " + bookshelf.getName() + " from " + JSON_STORE,SwingConstants.CENTER);
            add(message);
        } catch (IOException e) {
            message = new JLabel("Unable to read from file: " + JSON_STORE,SwingConstants.CENTER);
            add(message);
        }
        revalidate();
        repaint();
    }

    //MODIFIES: this
    //EFFECTS: prompts user to fill in an input text field and displays a confirm button
    private void generateInputField(String promptText, ActionListener actionListener) {
        inputFieldPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        prompt = new JLabel(promptText);
        inputField = new JTextField(15);
        confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(actionListener);
        inputFieldPanel.add(prompt);
        inputFieldPanel.add(inputField);
        inputFieldPanel.add(confirmButton);
        add(inputFieldPanel);
        revalidate();
        repaint();
    }

    //EFFECTS: removes message, scrollPane, and inputFieldPanel elements
    public void clearPanels() {
        remove(message);
        if (scrollPane != null) {
            remove(scrollPane);
        }
        if (inputFieldPanel != null) {
            remove(inputFieldPanel);
        }
        revalidate();
        repaint();
    }

    //EFFECTS: displays image of a bookshelf
    public void displayBookshelfImage() {
        try {
            ImageIcon bookImageIcon = new ImageIcon(
                    ImageIO.read(new File("./resources/bookshelf-image.jpg"))
                            .getScaledInstance(200, 270, java.awt.Image.SCALE_SMOOTH)
            );

            bookImage.setIcon(bookImageIcon);

            revalidate();
            repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
