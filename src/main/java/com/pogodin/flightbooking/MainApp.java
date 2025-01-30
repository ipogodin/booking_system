package com.pogodin.flightbooking;

import com.pogodin.flightbooking.entity.BookingRequest;
import com.pogodin.flightbooking.exception.MalformedRequestException;
import com.pogodin.flightbooking.operations.BookingProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class MainApp {
    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);
    private static final String FILE_NAME = "booking_the_flight.csv";
    private static final Integer MAX_ROWS = 20;
    public static final Integer MAX_SEATS = 8;

    public static void main(String[] args) {
        OperationResult result = processLine(args, MAX_ROWS, MAX_SEATS, FILE_NAME);
        if (!result.isSuccess()) {
            logger.warn(result.getFailureReason());
        }
        System.out.println(result.isSuccess() ? "SUCCESS" : "FAIL");
    }

    private static OperationResult processLine(String[] commandLineRequest, int rowNumber, int seatNumber, String safeFileName){
        BookingProcessor bp = new BookingProcessor(safeFileName, rowNumber, seatNumber);
        try {
            BookingRequest bookingRequest = bp.terminalToBookingRequest(commandLineRequest);
            return bp.processOperation(bookingRequest);
        } catch (MalformedRequestException e) {
            logger.error("Exception on command execution. Command: " + Arrays.toString(commandLineRequest), e);
            return OperationResult.failure(e.getMessage());
        }
    }


}