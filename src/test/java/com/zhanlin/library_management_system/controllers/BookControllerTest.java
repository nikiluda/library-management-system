package com.zhanlin.library_management_system.controllers;

import com.zhanlin.library_management_system.dto.book.BookRequestDto;
import com.zhanlin.library_management_system.exceptions.ErrorCode;
import com.zhanlin.library_management_system.exceptions.ResourceAlreadyExistsException;
import com.zhanlin.library_management_system.exceptions.ResourceNotFoundException;
import com.zhanlin.library_management_system.messages.ApiErrorMessage;
import com.zhanlin.library_management_system.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Test
    void createBook_shouldReturn400_whenRequestIsInvalid() throws Exception {

        mockMvc.perform(post("/api/books")
                        .contentType("application/json")
                        .content("""
                                {
                                    "title": "",
                                    "author": "",
                                    "publicationYear": 1500,
                                    "isbn": "123",
                                    "totalCopies": -1
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.title").value("Validation failed"))
                .andExpect(jsonPath("$.detail")
                        .value("One or more fields are invalid"))
                .andExpect(jsonPath("$.instance").value("/api/books"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }


    @Test
    void getBookById_shouldReturn404_whenBookNotFound() throws Exception {

        when(bookService.getBookById(1L))
                .thenThrow( new ResourceNotFoundException(
                        ErrorCode.BOOK_NOT_FOUND,
                        ApiErrorMessage.BOOK_NOT_FOUND_BY_ID.getMessage(1L)
                ));

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("BOOK_NOT_FOUND"))
                .andExpect(jsonPath("$.instance").value("/api/books/1"));


    }

    @Test
    void createBook_shouldReturn409_whenIsbnAlreadyExists() throws Exception {

        when(bookService.createBook(any(BookRequestDto.class)))
                .thenThrow(new ResourceAlreadyExistsException(
                        ErrorCode.ISBN_ALREADY_EXISTS,
                        ApiErrorMessage.ISBN_ALREADY_EXISTS.getMessage("5050007657")
                ));

        mockMvc.perform(post("/api/books")
                .contentType("application/json")
                .content("""
                            {
                            "title": "Война и мир",
                            "author": "Толстой",
                            "publicationYear": 1863,
                            "isbn": "5050007657",
                            "totalCopies": 1
                            }
                         """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("ISBN_ALREADY_EXISTS"))
                .andExpect(jsonPath("$.instance").value("/api/books"));
    }

    @Test
    void createBook_shouldReturn500_whenUnexpectedExceptionOccurs() throws Exception {

        when(bookService.createBook(any(BookRequestDto.class)))
                .thenThrow(new RuntimeException("Something went wrong"));

        mockMvc.perform(post("/api/books")
                .contentType("application/json")
                .content("""
                          {
                            "title": "Война и мир",
                            "author": "Толстой",
                            "publicationYear": 1863,
                            "isbn": "5050007657",
                            "totalCopies": 1
                          }
                          """))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("INTERNAL_ERROR"))
                .andExpect(jsonPath("$.title").value("Internal server error"))
                .andExpect(jsonPath("$.detail").value("An unexpected error occurred"))
                .andExpect(jsonPath("$.instance").value("/api/books"));

    }
}