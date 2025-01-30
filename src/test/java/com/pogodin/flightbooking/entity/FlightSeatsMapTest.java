package com.pogodin.flightbooking.entity;

import com.pogodin.flightbooking.OperationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FlightSeatsMapTest {
    private FlightSeatsMap flightSeatsMap;

    @BeforeEach
    void setUp() {
        flightSeatsMap = new FlightSeatsMap(5, 5); // 5 rows (A-E), 5 seats per row
    }

    @Test
    void testBookSeat_Success() {
        OperationResult result = flightSeatsMap.bookSeat('A', 1, 1);
        assertTrue(result.isSuccess(), "Booking should be successful");
    }

    @Test
    void testBookSeat_Fail_AlreadyOccupied() {
        flightSeatsMap.bookSeat('A', 1, 1);
        OperationResult result = flightSeatsMap.bookSeat('A', 1, 1);
        assertFalse(result.isSuccess(), "Booking should fail as the seat is occupied: " + result.getFailureReason());
    }

    @Test
    void testBookSeat_Fail_OutOfBounds() {
        OperationResult result = flightSeatsMap.bookSeat('F', 1, 1); // Row 'F' does not exist
        assertFalse(result.isSuccess(), "Booking should fail as the row is out of bounds: " + result.getFailureReason());
    }

    @Test
    void testBookMultipleSeats_Success() {
        OperationResult result = flightSeatsMap.bookSeat('A', 1, 2);
        assertTrue(result.isSuccess(), "Booking two seats together should be successful");
    }

    @Test
    void testBookMultipleSeats_Fail_NotEnoughSpace() {
        flightSeatsMap.bookSeat('A', 4, 1);
        OperationResult result = flightSeatsMap.bookSeat('A', 3, 3); // A4 is occupied
        assertFalse(result.isSuccess(), "Booking should fail if not enough space: " + result.getFailureReason());
    }

    @Test
    void testCancelBooking_Success() {
        flightSeatsMap.bookSeat('A', 1, 1);
        OperationResult result = flightSeatsMap.cancelBook('A', 1, 1);
        assertTrue(result.isSuccess(), "Canceling a booked seat should be successful");
    }

    @Test
    void testCancelBooking_Fail_InvalidSeat() {
        OperationResult result = flightSeatsMap.cancelBook('F', 1, 1); // Row 'F' does not exist
        assertFalse(result.isSuccess(), "Canceling should fail if the seat does not exist: " + result.getFailureReason());
    }

    @Test
    void testSeatAvailable_True() {
        assertTrue(flightSeatsMap.seatAvailable('B', 3), "Seat should be available initially");
    }

    @Test
    void testSeatAvailable_False_AfterBooking() {
        flightSeatsMap.bookSeat('C', 2, 1);
        assertFalse(flightSeatsMap.seatAvailable('C', 2), "Seat should be unavailable after booking");
    }

    @Test
    void testBookUnbookRebook_Success() {
        OperationResult bookResult1 = flightSeatsMap.bookSeat('D', 2, 2);
        assertTrue(bookResult1.isSuccess(), "Booking seats D2-D3 should be successful");

        OperationResult cancelResult = flightSeatsMap.cancelBook('D', 2, 2);
        assertTrue(cancelResult.isSuccess(), "Canceling seats D2-D3 should be successful");

        OperationResult bookResult2 = flightSeatsMap.bookSeat('D', 2, 2);
        assertTrue(bookResult2.isSuccess(), "Rebooking seats D2-D3 should be successful");
    }
}