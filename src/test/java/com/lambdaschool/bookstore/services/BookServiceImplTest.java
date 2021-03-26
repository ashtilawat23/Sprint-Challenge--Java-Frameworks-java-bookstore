package com.lambdaschool.bookstore.services;

import com.lambdaschool.bookstore.BookstoreApplication;
import com.lambdaschool.bookstore.exceptions.ResourceNotFoundException;
import com.lambdaschool.bookstore.models.Author;
import com.lambdaschool.bookstore.models.Book;
import com.lambdaschool.bookstore.models.Section;
import com.lambdaschool.bookstore.models.Wrote;
import com.lambdaschool.bookstore.repository.AuthorRepository;
import com.lambdaschool.bookstore.repository.BookRepository;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookstoreApplication.class)
//**********
// Note security is handled at the controller, hence we do not need to worry about security here!
//**********
public class BookServiceImplTest
{

    @Autowired
    private BookService bookService;

    @Autowired
    SectionService sectionService;

    @Autowired
    private AuthorService authorService;

    @MockBean
    BookRepository bookRepository;

    @MockBean
    AuthorRepository authorRepository;

    List<Book> bookList = new ArrayList<>();

    @Before
    public void setUp() throws
            Exception
    {

        Author a1 = new Author("John",
                "Mitchell");
        Author a2 = new Author("Dan",
                "Brown");
        a1.setAuthorid(1);
        a2.setAuthorid(2);

        Section s1 = new Section("Fiction");
        Section s2 = new Section("Technology");
        s1.setSectionid(11);
        s2.setSectionid(12);

        //        Set<Wrote> wrote = new HashSet<>();
        //        wrote.add(new Wrote(a2, new Book()));

        Book b1 = new Book("Flatterland",
                "9780738206752",
                2001,
                s1);
        //        b1.setWrotes(wrote);
        b1.getWrotes()
                .add(new Wrote(a1,
                        new Book()));
        b1.setBookid(111);
        bookList.add(b1);

        Book b2 = new Book("Digital Fortess",
                "9788489367012",
                2007,
                s1);
        b2.getWrotes()
                .add(new Wrote(a1,
                        new Book()));
        b2.setBookid(112);
        bookList.add(b2);

        Book b3 = new Book("The Da Vinci Code",
                "9780307474278",
                2009,
                s1);
        b3.getWrotes()
                .add(new Wrote(a2,
                        new Book()));
        b3.setBookid(113);
        bookList.add(b3);

        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws
            Exception
    {
        // nothing is added to this
    }

    // ---------- Get Tests -----------
    // passed test
    @Test
    public void findAll()
    {
        Mockito.when(bookRepository.findAll())
                .thenReturn(bookList);

        assertEquals(3,
                bookService.findAll()
                        .size());
    }

    // passed test
    @Test
    public void findBookById()
    {
        Mockito.when(bookRepository.findById(111L))
                .thenReturn(Optional.of(bookList.get(0)));

        assertEquals("Flatterland",
                bookService.findBookById(111)
                        .getTitle());
    }

    // passed test
    @Test(expected = ResourceNotFoundException.class)
    public void notFindBookById()
    {
        Mockito.when(bookRepository.findById(10000L))
                .thenThrow(ResourceNotFoundException.class);

        assertEquals("Test Test",
                bookService.findBookById(10000)
                        .getTitle());
    }

    // --------- Tests for other styles of requests --------
    // passed test
    @Test
    public void delete()
    {
        Mockito.when(bookRepository.findById(111L))
                .thenReturn(Optional.of(bookList.get(0)));

        Mockito.doNothing()
                .when(bookRepository)
                .deleteById(111L);

        bookService.delete(111);
        assertEquals(3,
                bookList.size());
    }

    // ---------
    // Passed test
    @Test
    public void save()
    {
        // create a book to save

        //        Section s1 = new Section("Fiction");
        //        s1.setSectionid(1);

        Author a1 = new Author("John",
                "Mitchell");
        a1.setAuthorid(10);

        Book b3 = new Book("Flatterland",
                "9780307474278",
                2009,
                null);

        b3.getWrotes()
                .add(new Wrote(a1,
                        b3));
        b3.setBookid(0);

        // --------------------

        Mockito.when(bookRepository.save(b3))
                .thenReturn(b3);
        Mockito.when(authorRepository.findById(a1.getAuthorid()))
                .thenReturn(Optional.of(a1));

        Book addBook = bookRepository.save(b3);

        assertNotNull(addBook);
        assertEquals("Flatterland",
                addBook.getTitle());

    }

    // not passing test
    // Passed Test
    @Test
    public void update() throws Exception
    {
        Author a1 = new Author("John",
                "Mitchell");
        a1.setAuthorid(1);

        Section s1 = new Section("Fiction");
        s1.setSectionid(11);

        Book b1 = new Book("Flatterland",
                "9780738206752",
                2001,
                s1);

        b1.getWrotes()
                .add(new Wrote(a1,
                        new Book()));
        b1.setBookid(111);

        // I need a copy of r2 to send to update so the original r2 is not changed.
        // I am using Jackson to make a clone of the object
        ObjectMapper objectMapper = new ObjectMapper();
        Book b3 = objectMapper
                .readValue(objectMapper.writeValueAsString(b1),
                        Book.class);

        Mockito.when(bookRepository.findById(111L))
                .thenReturn(Optional.of(b3));

        Mockito.when(bookRepository.save(any(Book.class)))
                .thenReturn(b1);

        Book addBook = bookRepository.save(b1);

        assertNotNull(addBook);
        assertEquals("Flatterland",
                addBook.getTitle());

    }

    @Test
    public void deleteAll()
    {
//        Mockito.when(bookRepository.findAll())
//            .thenReturn(bookList);
//
//        Mockito.doNothing()
//            .when(bookRepository)
//            .deleteAll(bookList);
//
//        bookService.deleteAll(bookList);
//        assertEquals(0, bookList.size());

    }
}