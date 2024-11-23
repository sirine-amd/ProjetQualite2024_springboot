package com.mnb.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.Column;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EntityColumnNameTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testPublisherColumnNames() throws NoSuchFieldException {
        // Iterate over the fields of the Publisher entity class
        for (Field field : Publisher.class.getDeclaredFields()) {
            // Check if the field is annotated with @Column
            if (field.isAnnotationPresent(Column.class)) {
                // Get the column name from the @Column annotation
                Column column = field.getAnnotation(Column.class);
                String columnName = column.name();
                
                System.out.println("Field: " + field.getName() + " => Column Name: " + columnName);

                // Query the database to check if the column exists
                String query = "SELECT column_name FROM information_schema.columns " +
                        "WHERE table_name = 'PUBLISHER' AND column_name = ?";
                String actualColumnName = jdbcTemplate.queryForObject(query, String.class, columnName);

                // Assert that the column exists in the database
                assertNotNull(actualColumnName, "Column " + columnName + " not found in the database.");
            }
        }
    }

    @Test
    void testBookColumnNames() throws NoSuchFieldException {
        // Similar test for the Book entity
        for (Field field : Book.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                String columnName = column.name();

                System.out.println("Field: " + field.getName() + " => Column Name: " + columnName);

                String query = "SELECT column_name FROM information_schema.columns " +
                        "WHERE table_name = 'BOOK' AND column_name = ?";
                String actualColumnName = jdbcTemplate.queryForObject(query, String.class, columnName);

                assertNotNull(actualColumnName, "Column " + columnName + " not found in the database.");
            }
        }
    }

    @Test
    void testAuthorColumnNames() throws NoSuchFieldException {
        // Similar test for the Author entity
        for (Field field : Author.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                String columnName = column.name();

                System.out.println("Field: " + field.getName() + " => Column Name: " + columnName);

                String query = "SELECT column_name FROM information_schema.columns " +
                        "WHERE table_name = 'AUTHOR' AND column_name = ?";
                String actualColumnName = jdbcTemplate.queryForObject(query, String.class, columnName);

                assertNotNull(actualColumnName, "Column " + columnName + " not found in the database.");
            }
        }
    }
}
