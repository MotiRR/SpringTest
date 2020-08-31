package com.geekbrains.book.store;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geekbrains.book.store.controllers.rest.BookRestController;
import com.geekbrains.book.store.entities.Book;
import com.geekbrains.book.store.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class BookRestControllerTests {

    private static final String url = "/api/v1/books/";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    private Book book;

    @BeforeEach
    public void init() {
        book = new Book();
        book.setId(1L);
        book.setTitle("HP");
    }

    @Test
    public void testGetBookById() throws Exception {
        given(this.bookService.findById(1L))
                .willReturn(book);
        this.mvc.perform(get(url + "{id}", 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testGetBookAll() throws Exception {
        List<Book> list = new ArrayList<>();
        list.add(book);
        given(this.bookService.findAll())
                .willReturn(list);
        this.mvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    public void testCreateNewBook() throws Exception {
        given(this.bookService.saveOrUpdate(Mockito.any())).willReturn(book);
        //Mockito.when(repository.save(Mockito.any())).thenReturn(book);
        this.mvc.perform(post(url)
                .content(objectMapper.writeValueAsString(book))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(book)));
    }

    @Test
    public void testModifyBook() throws Exception {
        given(this.bookService.saveOrUpdate(Mockito.any())).willReturn(book);
        given(this.bookService.existsById(book.getId())).willReturn(true);
        //Mockito.when(repository.save(Mockito.any())).thenReturn(book);
        this.mvc.perform(put(url)
                .content(objectMapper.writeValueAsString(book))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(book)));
    }

}
