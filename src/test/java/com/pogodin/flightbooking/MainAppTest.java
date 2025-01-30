package com.pogodin.flightbooking;

import com.pogodin.flightbooking.operations.BookingProcessor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class MainAppTest {
    private static final String FILE_NAME = "test_main_booking.csv";
    private static final int MAX_ROWS = 20;
    private static final int MAX_SEATS = 8;
    private BookingProcessor bookingProcessor;

    @BeforeEach
    void setUp() {
        File testDbFile = new File(FILE_NAME);
        if (testDbFile.exists()) {
            testDbFile.delete();
        }
        bookingProcessor = new BookingProcessor(FILE_NAME, MAX_ROWS, MAX_SEATS);
    }

    @AfterEach
    void cleanUp() {
        File testDbFile = new File(FILE_NAME);
        if (testDbFile.exists()) {
            testDbFile.delete();
        }
    }


    @Test
    void testLargeSequenceOfOperations() {
        //top to bottom seat booking
        assertEquals("SUCCESS", processCommand(new String[]{"BOOK", "A1", "2"}));
        assertEquals("SUCCESS", processCommand(new String[]{"BOOK", "A3", "3"}));
        assertEquals("FAIL", processCommand(new String[]{"BOOK", "A6", "3"}));
        assertEquals("SUCCESS", processCommand(new String[]{"BOOK", "B4", "4"}));
        assertEquals("SUCCESS", processCommand(new String[]{"BOOK", "B1", "3"}));
        assertEquals("FAIL", processCommand(new String[]{"BOOK", "B3", "2"}));
        assertEquals("SUCCESS", processCommand(new String[]{"CANCEL", "B4", "4"}));
        assertEquals("SUCCESS", processCommand(new String[]{"BOOK", "C5", "2"}));
        assertEquals("FAIL", processCommand(new String[]{"BOOK", "C7", "2"}));
        assertEquals("SUCCESS", processCommand(new String[]{"CANCEL", "C5", "2"}));
        assertEquals("SUCCESS", processCommand(new String[]{"BOOK", "D4", "3"}));
        assertEquals("FAIL", processCommand(new String[]{"BOOK", "D7", "2"}));
        assertEquals("SUCCESS", processCommand(new String[]{"CANCEL", "D4", "3"}));
        assertEquals("SUCCESS", processCommand(new String[]{"BOOK", "E2", "4"}));
        assertEquals("FAIL", processCommand(new String[]{"BOOK", "E6", "3"}));
        assertEquals("SUCCESS", processCommand(new String[]{"CANCEL", "E2", "2"}));
        assertEquals("SUCCESS", processCommand(new String[]{"CANCEL", "E4", "1"}));
        assertEquals("SUCCESS", processCommand(new String[]{"BOOK", "E2", "3"}));
        assertEquals("SUCCESS", processCommand(new String[]{"BOOK", "F0", "3"}));
        assertEquals("SUCCESS", processCommand(new String[]{"BOOK", "F4", "3"}));
        assertEquals("SUCCESS", processCommand(new String[]{"BOOK", "F7", "1"}));
        assertEquals("FAIL", processCommand(new String[]{"BOOK", "F8", "1"}));
        assertEquals("SUCCESS", processCommand(new String[]{"BOOK", "G3", "3"}));
        assertEquals("SUCCESS", processCommand(new String[]{"CANCEL", "G3", "2"}));
        assertEquals("SUCCESS", processCommand(new String[]{"BOOK", "G3", "2"}));
        assertEquals("SUCCESS", processCommand(new String[]{"BOOK", "H0", "4"}));
        assertEquals("SUCCESS", processCommand(new String[]{"BOOK", "H4", "3"}));
        assertEquals("FAIL", processCommand(new String[]{"BOOK", "H6", "2"}));
        assertEquals("SUCCESS", processCommand(new String[]{"BOOK", "T5", "3"}));
        assertEquals("FAIL", processCommand(new String[]{"BOOK", "T3", "4"}));
        assertEquals("SUCCESS", processCommand(new String[]{"CANCEL", "T5", "3"}));
        assertEquals("SUCCESS", processCommand(new String[]{"BOOK", "T3", "4"}));
        // repeat used before
        assertEquals("SUCCESS", processCommand(new String[]{"BOOK", "B4", "4"}));
        assertEquals("SUCCESS", processCommand(new String[]{"BOOK", "C5", "3"}));
        assertEquals("SUCCESS", processCommand(new String[]{"BOOK", "D4", "3"}));
        assertEquals("FAIL", processCommand(new String[]{"BOOK", "F0", "1"}));
    }

    private String processCommand(String[] command) {
        OperationResult result = MainApp.processLine(command, MAX_ROWS, MAX_SEATS, FILE_NAME);
        return result.isSuccess() ? "SUCCESS" : "FAIL";
    }
}

