package com.adam.buzas.webshop.main.services;

import com.adam.buzas.webshop.main.model.Book;
import com.adam.buzas.webshop.main.model.Category;
import com.adam.buzas.webshop.main.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    public Optional<Book> getBookById(int id){
        return bookRepository.findById(id);
    }
    public List<Book> getAllBook(){
        List<Book> list = new ArrayList<>();
        for(Book k : bookRepository.findAll()){
            list.add(k);
        }
        return list;
    }

    public List<Book> getBookCategory(Category category){
        List<Book> list = new ArrayList<>();
        for(Book k : bookRepository.findKonyvByCategory(category)){
            list.add(k);
        }
        return list;
    }

    public Book newBook(Book book){
        return bookRepository.save(book);
    }

    private List<Book> booksSearch(Iterable<Book> list, String word){
        List<Book> searchedBooks = new ArrayList<>();

        for (Book book : list) {
            if (book.getTitle().toLowerCase().startsWith(word.toLowerCase())) {
                searchedBooks.add(book);
            }
        }

        return searchedBooks;
    }

    public List<Book> getSearchedBooks(String searchingWord) {
        return booksSearch(bookRepository.findAll(), searchingWord);
    }

    public List<Book> getBookCategoryAndSearch(Category category, String searchingWord) {
        return booksSearch(bookRepository.findKonyvByCategory(category), searchingWord);
    }

    public void deleteBook(int id){
        bookRepository.deleteById(id);
    }

    public Optional<Book> getBookByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    public boolean hasBookWithTitleAndAuthor(String title, String author) {
        return !bookRepository.findByTitleAndAuthor(title, author).isEmpty();
    }

    public List<Book> getBooksBySearchingWord(String word){
        List<Book> result = new ArrayList<Book>();
        Iterable<Book> iter = bookRepository.findByTitleStartingWithOrAuthorStartingWith(word, word);
        for (Book book : iter) {
            result.add(book);
        }
        return result;
    }
}
