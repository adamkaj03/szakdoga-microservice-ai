package com.adam.buzas.webshop.main.restcontrollers;

import com.adam.buzas.webshop.main.converter.JsonStringToObjectConverter;
import com.adam.buzas.webshop.main.dto.BookRequest;
import com.adam.buzas.webshop.main.model.Book;
import com.adam.buzas.webshop.main.model.Category;
import com.adam.buzas.webshop.main.model.ResponseText;
import com.adam.buzas.webshop.main.services.BookService;
import com.adam.buzas.webshop.main.services.CategoryService;
import com.adam.buzas.webshop.main.services.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequestMapping("/api")
@RestController()
@CrossOrigin(origins = {"http://localhost:4200", "https://purple-river-0f0577f03.4.azurestaticapps.net"})
public class BookController {

    @Autowired
    BookService bookService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ImageUploadService imageUploadService;
    @Autowired
    JsonStringToObjectConverter converter;

    @GetMapping("/books")
    public List<Book> getAllBooks(){
       return bookService.getAllBook();
    }

    @GetMapping("/books/id/{id}")
    public Optional<Book> getBookById(@PathVariable("id") int id){
        return bookService.getBookById(id);
    }

    @GetMapping("/books/title/{title}")
    public ResponseEntity<?> getBookByTitle(@PathVariable("title") String title){
        //angularban elkell kódolni a / karaktert és itt kódolom vissza
        String newTitle = title.replaceAll("--", "/");
        Optional<Book> book = bookService.getBookByTitle(newTitle);
        if(book.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseText("There aren't any books in the system with this title!"));
        }
        else {
            return ResponseEntity.ok(book);
        }
    }

    @GetMapping("/books/category/{id}")
    public ResponseEntity<?> getBookByCategory(@PathVariable("id") int id){
        Optional<Category> category = categoryService.getCategory(id);
        if(category.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseText("There aren't any books in the system with this title!"));
        }
        return ResponseEntity.ok(bookService.getBookCategory(category.get()));
    }

    @GetMapping("/books/search/{word}")
    public ResponseEntity<?> getBookBySearchingWord(@PathVariable("word") String word){
        System.out.println("valami");
        List<Book> list = bookService.getBooksBySearchingWord(word);
        if(list.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseText("There aren't any result for this search!"));
        }
        return ResponseEntity.ok(list);
    }



    @PostMapping("/books")
    public ResponseEntity<?> createBook(@RequestParam("bookRequest") String bookRequest, @RequestParam("file") MultipartFile file){
        try {
            BookRequest json = convertStringToRequest(bookRequest);
            String url = imageUploadService.uploadImage(file, file.getOriginalFilename());
            Book book = new Book(json.getTitle(), json.getAuthor(), json.getPublishYear(), json.getPrice(), json.getCategory(), url, json.getDescription());
            if(bookService.hasBookWithTitleAndAuthor(book.getTitle(), book.getAuthor())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseText("Sikertelen felvétel!"));
            }
            bookService.newBook(book);
            return ResponseEntity.ok(new ResponseText("Sikeres felvétel!"));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseText("Sikertelen felvétel!"));
        }

    }

    @PutMapping("/books/{id}")
    public Book updateBook(@PathVariable("id") int id, @RequestBody Book book) {
        book.setId(id);
        return bookService.newBook(book);
    }

    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable("id") int id){
        bookService.deleteBook(id);
    }

    public BookRequest convertStringToRequest(String bookRequest) throws IOException {
        if(bookRequest.charAt(0) == '"' && bookRequest.charAt(bookRequest.length()-1) == '"'){
            bookRequest = bookRequest.substring(1, bookRequest.length()-1);
            bookRequest = bookRequest.replace("\\", "");
        }
        return (BookRequest) converter.convertJsonStringToObject(bookRequest, BookRequest.class);
    }

}
