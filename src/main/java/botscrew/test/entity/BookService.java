package botscrew.test.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    /**
     * Loads books from database.
     * @return all books from database.
     */
    public List<Book> getAllBooks() {
        List<Book> list = new ArrayList<>();
        bookRepository.findAll().forEach(list::add);
        return list;
    }

    /**
     * Saves book to the database.
     * @param book simple book created by user.
     */
    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    /**
     *
     * @param name name of a book
     * @return A list of books with the following name.
     */
    public List<Book> getBookByName(String name) {
        return bookRepository.findByName(name);
    }

    /**
     * Removes book from database.
     * @param book valid book to delete
     */
    public void removeBook(Book book) {
        bookRepository.delete(book);
    }
}
