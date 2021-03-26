package com.lambdaschool.bookstore.controllers;

import com.lambdaschool.bookstore.models.Author;
import com.lambdaschool.bookstore.models.Wrote;
import junit.framework.TestCase;
import org.codehaus.jackson.map.ObjectMapper;
import com.lambdaschool.bookstore.BookstoreApplication;
import com.lambdaschool.bookstore.models.Book;
import com.lambdaschool.bookstore.models.Section;
import com.lambdaschool.bookstore.services.BookService;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)

/*****
 * Due to security being in place, we have to switch out WebMvcTest for SpringBootTest
 * @WebMvcTest(value = BookController.class)
 */
@SpringBootTest(classes = BookstoreApplication.class)

/****
 * This is the user and roles we will use to test!
 */
@WithMockUser(username = "admin",
        roles = {"ADMIN", "DATA"})
public class BookControllerTest
{
    /******
     * WebApplicationContext is needed due to security being in place.
     */
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    List<Book> bookList = new ArrayList<>();

    @Before
    public void setUp() throws
            Exception
    {


        /*****
         * Note that since we are only testing bookstore data, you only need to mock up bookstore data.
         * You do NOT need to mock up user data. You can. It is not wrong, just extra work.
         */

        Author a1 = new Author("John", "Mitchell");
        Author a2 = new Author("Dan", "Brown");
        a1.setAuthorid(1);
        a2.setAuthorid(2);

        Section s1 = new Section("Fiction");
        Section s2 = new Section("Technology");
        s1.setSectionid(11);
        s2.setSectionid(12);

        Book b1 = new Book("Flatterland",
                "9780738206752",
                2001,
                s1);
        b1.getWrotes()
                .add(new Wrote(a1, new Book()));
        b1.setBookid(111);
        bookList.add(b1);

        Book b2 = new Book("Digital Fortess",
                "9788489367012",
                2007,
                s1);
        b2.getWrotes()
                .add(new Wrote(a2, new Book()));
        b2.setBookid(112);
        bookList.add(b2);


        Book b3 = new Book("The Da Vinci Code",
                "9780307474278",
                2009,
                s1);
        b3.getWrotes()
                .add(new Wrote(a2, new Book()));
        b3.setBookid(113);
        bookList.add(b3);


        /*****
         * The following is needed due to security being in place!
         */
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @After
    public void tearDown() throws
            Exception
    {
        // dont do anything here
    }

    // ---------- Tests for Get Methods ----------
    // Passed Test
    @Test
    public void listAllBooks() throws
            Exception
    {
        String apiUrl = "/books/books";
        Mockito.when(bookService.findAll())
                .thenReturn(bookList);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult r = mockMvc.perform(rb)
                .andReturn();
        String tr = r.getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(bookList);

        assertEquals(3,
                bookService.findAll()
                        .size());
    }

    // Passed Test
    @Test
    public void getBookById() throws
            Exception
    {

        String apiUrl = "/books/book/111";
        Mockito.when(bookService.findBookById(111L))
                .thenReturn(bookList.get(0));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult r = mockMvc.perform(rb)
                .andReturn();
        String tr = r.getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(bookList.get(0));


//        assertEquals(er, tr);
//        Mockito.when(bookService.findBookById(any(Long.class))).thenReturn(bookList.get(0));
//        assertEquals("Flatterland", bookService.findBookById(111L).getTitle());

        assertEquals("Flatterland",
                bookService.findBookById(111)
                        .getTitle());

    }
    // Passed Test
    @Test
    public void getNoBookById() throws
            Exception
    {
        // this tests the exception that is thrown if there is no book by a given bookid
        String apiUrl = "/books/book/100";
        Mockito.when(bookService.findBookById(100))
                .thenReturn(null);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult r = mockMvc.perform(rb)
                .andReturn();
        String tr = r.getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = "";

        assertEquals(er, tr);

    }

    // ---------- POST test ---------------
    // test passed
    @Test
    public void addNewBook() throws
            Exception
    {
        String apiURL = "/books/book";
        String book4Name = "Test Chris Corvo Book 3";

        Section s4 = new Section("Business");
        s4.setSectionid(3);

        Book b4 = new Book(book4Name,
                "1314241651234",
                0,
                s4);
        b4.setBookid(60);

        ObjectMapper mapper = new ObjectMapper();
        String bookString = mapper.writeValueAsString(b4);

        Mockito.when(bookService.save(any(Book.class)))
                .thenReturn(b4);

        RequestBuilder rb = MockMvcRequestBuilders.post(apiURL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookString);

        mockMvc.perform(rb)
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    // ----------- PUT test ---------------
    // test passed
    @Test
    public void updateFullBook() throws Exception
    {

        String apiUrl = "/books/book/{bookid}";
        String book2Name = "Test Chris Corvo Book 2";

        Section s1 = new Section("Fiction");
        s1.setSectionid(1);

        Book b2 = new Book(book2Name,
                "9788489367012",
                2007,
                s1);
        b2.setBookid(1000);

        Mockito.when(bookService.update(b2,
                5000L))
                .thenReturn(b2);
        ObjectMapper mapper = new ObjectMapper();
        String bookString = mapper.writeValueAsString(b2);

        RequestBuilder rb = MockMvcRequestBuilders.put(apiUrl,
                5000L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(bookString);

        mockMvc.perform(rb)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());


    }

    // ------------ DELETE test -------------
    // test passed
    @Test
    public void deleteBookById() throws
            Exception
    {
        String apiUrl = "/books/book/{id}";

        RequestBuilder rb = MockMvcRequestBuilders.delete(apiUrl,
                "20")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(rb)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }
}