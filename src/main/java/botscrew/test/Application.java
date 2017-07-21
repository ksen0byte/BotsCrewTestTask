package botscrew.test;

import botscrew.test.entity.Book;
import botscrew.test.entity.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

@Component
public class Application implements CommandLineRunner {
    @Autowired
    private BookService bookService;

    private static String[] commands = {"add ", "remove ", "edit ", "all books"};

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String userInput;
        while (!(userInput = scanner.nextLine()).equals("quit")) {
            parseInput(userInput);
        }
        System.out.println("Bye!");
    }

    private void parseInput(String userInput) {
        String response;
        if (userInput.startsWith(commands[0])) {
            response = addBook(userInput);
            System.out.println(response);
        } else if (userInput.startsWith(commands[1])) {
            response = removeBook(userInput);
            System.out.println(response);
        } else if (userInput.startsWith(commands[2])) {
            response = editBook(userInput);
            System.out.println(response);
        } else if (userInput.equals(commands[3])) {
            printAllBooks();
        } else {
            System.err.println("Invalid input!");
        }
    }

    /**
     * Edits book in database.
     *
     * @param s raw user input
     * @return response string, such as "Invalid input!", "No book with such name!"
     * or success message
     */
    private String editBook(String s) {
        try {
            String name = s.substring(s.indexOf(" ") + 1).trim();
            Book book = chooseBook(name);

            if (book == null)
                return "There is no book with such name";

            System.out.println("Enter new name");
            Scanner sc = new Scanner(System.in);
            String newName = sc.nextLine();

            String oldName = book.getName();
            book.setName(newName);
            bookService.saveBook(book);
            return "book " + book.getAuthor() + " \""
                    + oldName + "\" was successfully edited";
        } catch (Exception e) {
            return "Invalid input!";
        }
    }

    /**
     * Removes book from database.
     *
     * @param s raw user input
     * @return response string, such as "Invalid input!", "No book with such name!"
     * or success message
     */
    private String removeBook(String s) {
        try {
            String name = s.substring(s.indexOf(" ") + 1).trim();
            Book book = chooseBook(name);

            if (book == null)
                return "There is no book with such name";

            bookService.removeBook(book);
            return "book " + book.getAuthor() + " \""
                    + book.getName() + "\" was successfully removed";
        } catch (Exception e) {
            return "Invalid input!";
        }
    }

    /**
     * Helps user to choose book from duplicates
     *
     * @param name name of a book
     * @return a book, which user chooses or null, if there are no books
     * with such name.
     */
    private Book chooseBook(String name) {
        List<Book> bookList = bookService.getBookByName(name);
        Book book;
        if (bookList.isEmpty()) {
            return null;
        } else if (bookList.size() != 1) {
            System.out.println("we have few books with such " +
                    "name please choose one by typing a number of book:");
            for (int i = 0; i < bookList.size(); i++) {
                System.out.println((i + 1) + ". " + bookList.get(i).getAuthor() +
                        " \"" + bookList.get(i).getName() + "\"");
            }
            Scanner sc = new Scanner(System.in);
            Integer index = sc.nextInt() - 1; // correcting indices bc our list starts from 0
            book = bookList.get(index);
        } else {
            book = bookList.get(0);
        }
        return book;
    }

    /**
     * Adds book to database.
     *
     * @param s raw user input
     * @return response string, such as "Invalid input!" or success message
     */
    private String addBook(String s) {
        try {
            String author = s.substring(s.indexOf(" ") + 1, s.indexOf("\"")).trim();
            String name = s.substring(s.indexOf("\"") + 1, s.lastIndexOf("\""));
            Book book = new Book(name, author);
            bookService.saveBook(book);
            return "book " + author + " \"" + name +
                    "\" was successfully added";
        } catch (Exception e) {
            return "Invalid input!";
        }
    }

    /**
     * Prints all books from database
     */
    private void printAllBooks() {
        List<Book> allBooks = bookService.getAllBooks();
        allBooks.sort(Comparator.comparing(Book::getName));
        allBooks.forEach(System.out::println);
        if (allBooks.isEmpty())
            System.out.println("Nothing in here!");
    }

}
