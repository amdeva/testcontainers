package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;


@Testcontainers
@SpringBootTest
class DemoApplicationTests {

    static Network network = Network.newNetwork();
    @Container
    public static PostgreSQLContainer container = (PostgreSQLContainer) new PostgreSQLContainer("postgres:11.1")
            .withUsername("duke")
            .withPassword("password")
            .withDatabaseName("test")
            .withInitScript("database/init.sql")
            .withNetwork(network);

    @Autowired
    private BookRepository bookRepository;

    // requires Spring Boot >= 2.2.6
    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @Test
    void contextLoads() {

        Book book = new Book();
        book.setName("Testcontainers");

        bookRepository.save(book);

        System.out.println("Context loads!");
    }

    @Test
    void getBooks() {


        List<Book> allBooks = bookRepository.findAll();

        System.out.println("books **************************");
        allBooks.forEach(System.out::println);
    }

}
