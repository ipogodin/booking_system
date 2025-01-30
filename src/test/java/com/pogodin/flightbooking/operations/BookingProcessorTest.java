package com.pogodin.flightbooking.operations;

import com.pogodin.flightbooking.BookingAction;
import com.pogodin.flightbooking.entity.BookingRequest;
import com.pogodin.flightbooking.entity.FlightSeatsMap;
import com.pogodin.flightbooking.OperationResult;
import com.pogodin.flightbooking.exception.MalformedRequestException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingProcessorTest {
    private BookingProcessor bookingProcessor;
    private FileOperations fileOperationsMock;
    private FlightSeatsMap flightSeatsMapMock;
    private final String filePath = "testBooking.csv";

    @BeforeEach
    void setUp() {
        File testStorage = new File(filePath);
        if (testStorage.exists()) {
            testStorage.delete();
        }
        fileOperationsMock = mock(FileOperations.class);
        flightSeatsMapMock = mock(FlightSeatsMap.class);
        bookingProcessor = new BookingProcessor(filePath, 5, 5);
    }

    @AfterEach
    void cleanUp() {
        File testStorage = new File(filePath);
        if (testStorage.exists()) {
            testStorage.delete();
        }
    }

    @Test
    void testProcessOperation_BookSuccess() {
        BookingRequest request = new BookingRequest(BookingAction.BOOK, 'A', 0, 2);
        when(fileOperationsMock.loadBookingMap(filePath)).thenReturn(flightSeatsMapMock);
        when(flightSeatsMapMock.bookSeat('A', 0, 2)).thenReturn(OperationResult.success());

        OperationResult result = bookingProcessor.processOperation(request);
        assertTrue(result.isSuccess(), "Booking should be successful, " + result.getFailureReason());
    }

    @Test
    void testProcessOperation_CancelSuccess() {
        BookingRequest request = new BookingRequest(BookingAction.CANCEL, 'B', 3, 1);
        when(fileOperationsMock.loadBookingMap(filePath)).thenReturn(flightSeatsMapMock);
        when(flightSeatsMapMock.cancelBook('B', 3, 1)).thenReturn(OperationResult.success());

        OperationResult result = bookingProcessor.processOperation(request);
        assertTrue(result.isSuccess(), "Canceling should be successful");
    }

    @Test
    void testProcessOperation_Fail_InvalidAction() {
        BookingRequest request = new BookingRequest(null, 'C', 4, 1);
        OperationResult result = bookingProcessor.processOperation(request);
        assertFalse(result.isSuccess(), "Operation should fail with an unsupported action");
    }

    @Test
    void testSplitOperation_ValidRequest() throws MalformedRequestException {
        BookingRequest request = bookingProcessor.terminalToBookingRequest("BOOK A0 4");
        assertEquals(BookingAction.BOOK, request.action());
        assertEquals('A', request.row());
        assertEquals(0, request.seat());
        assertEquals(4, request.passengers());
    }

    @Test
    void testSplitOperation_InvalidCommandFormat() {
        Exception exception = assertThrows(MalformedRequestException.class, () ->
                bookingProcessor.terminalToBookingRequest("BOOK A0")
        );
        assertTrue(exception.getMessage().contains("Command is not constructed in a desired form"));
    }

    @Test
    void testSplitOperation_InvalidAction() {
        Exception exception = assertThrows(MalformedRequestException.class, () ->
                bookingProcessor.terminalToBookingRequest("FLY A1 2")
        );
        assertTrue(exception.getMessage().contains("Operation FLY is not supported"));
    }

    @Test
    void testSplitOperation_InvalidSeatFormat() {
        Exception exception = assertThrows(MalformedRequestException.class, () ->
                bookingProcessor.terminalToBookingRequest("BOOK 11 3")
        );
        assertTrue(exception.getMessage().contains("The place is defined incorrectly"));
    }

    @Test
    void testSplitOperation_InvalidPassengerNumber() {
        Exception exception = assertThrows(MalformedRequestException.class, () ->
                bookingProcessor.terminalToBookingRequest("BOOK A0 -1")
        );
        assertTrue(exception.getMessage().contains("Consider providing the positive number of customers"));
    }

}