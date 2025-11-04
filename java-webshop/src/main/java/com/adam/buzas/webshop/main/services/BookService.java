package com.adam.buzas.webshop.main.services;

import com.adam.buzas.webshop.main.dto.BookDataResponseDto;
import com.adam.buzas.webshop.main.model.Book;
import com.adam.buzas.webshop.main.model.Category;
import com.adam.buzas.webshop.main.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ImageCaptionService imageCaptionService;

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

    /**
     * Feldolgozza a feltöltött könyvborító képet, és AI segítségével kinyeri a könyv releváns adatait (cím, szerző, leírás).
     *
     * A metódus meghívja az ImageCaptionService-t, amely a képet továbbítja a Python alapú AI mikroszerviznek.
     * Az AI válaszát a metódus visszaadja a hívónak.
     *
     * Logolja a folyamat indulását és befejezését, valamint hibát dob, ha a kép feldolgozása közben IO kivétel történik.
     *
     * @param file A feltöltött könyvborító képe MultipartFile formátumban.
     * @return BookDataResponseDto Az AI által extrahált könyvadatok (cím, szerző, leírás).
     * @throws RuntimeException Ha a kép feldolgozása során IO hiba történik.
     */
    public BookDataResponseDto getBookDataByImage(MultipartFile file) {
        log.info("Getting book data from image: {}", file.getOriginalFilename());
        try {
            BookDataResponseDto response = imageCaptionService.getDescriptionFromImage(file);
            log.info("Getting book data from image finished for file: {}", file.getOriginalFilename());
            return response;
        } catch (IOException e) {
            log.error("Error getting book data from image: {}", file.getOriginalFilename(), e);
            // Itt lehetne saját exception-t dobni, amit egy global exception handler lekezel
            throw new RuntimeException("Error processing book image", e);
        }
    }
}
