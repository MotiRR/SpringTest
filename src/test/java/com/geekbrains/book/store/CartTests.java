package com.geekbrains.book.store;

import com.geekbrains.book.store.beans.Cart;
import com.geekbrains.book.store.entities.Book;
import com.geekbrains.book.store.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartTests {

    @Autowired
    private Cart cart;

    @MockBean
    private BookService bookService;

    private Book book;

    @BeforeEach
    public void init() {
        book = new Book();
        book.setId(1L);
        book.setTitle("HP");
        book.setPrice(BigDecimal.valueOf(100));
    }

    @Test
    public void testCartAdd() {
        given(this.bookService.findById(any()))
                .willReturn(book);
        cart.add(1L);
        assertEquals(book, cart.getOrderItems().get(0).getBook());
        cart.add(1L);
        assertEquals(2, cart.getOrderItems().get(0).getCount());
        assertEquals(BigDecimal.valueOf(200), cart.getOrderItems().get(0).getAmount());
    }
}
