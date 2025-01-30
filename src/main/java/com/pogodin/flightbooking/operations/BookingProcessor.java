package com.pogodin.flightbooking.operations;

import com.pogodin.flightbooking.BookingAction;
import com.pogodin.flightbooking.entity.BookingRequest;
import com.pogodin.flightbooking.entity.FlightSeatsMap;
import com.pogodin.flightbooking.OperationResult;
import com.pogodin.flightbooking.exception.MalformedRequestException;

import java.io.File;
import java.util.Arrays;

/**
 * contains information on the save-file with the data on the current flight booking
 * the size of the plane is also provided
 */
public class BookingProcessor {

    private final String filePath;
    private final int planeRowsNumber;

    private final int planeRowSize;

    public BookingProcessor(String filePath, int planeRowsNumber, int planeRowSize) {
        this.filePath = filePath;
        this.planeRowsNumber = planeRowsNumber;
        this.planeRowSize = planeRowSize;
    }

    /**
     * Proceeding with the booking operation or cancel operation based on the booking request.
     * Data is either retrieved from the file or, if file does not exists yet, the new one will be created and stored.
     *
     * @param request request contains the booking action, seat number and number of passengers would like
     *                to book seats in a row, one to one starting from the seat number
     * @return operation result with success if the operation succeeds,
     *         failure otherwise with the reason
     */
    public OperationResult processOperation(BookingRequest request) {
        FlightSeatsMap seatMap;
        if (new File(filePath).exists()) {
            seatMap = FileOperations.getInstance().loadBookingMap(filePath);
        } else {
            seatMap = new FlightSeatsMap(planeRowsNumber, planeRowSize);
        }

        OperationResult actionResult;
        if (request.action() == BookingAction.BOOK) {
            actionResult = seatMap.bookSeat(request.row(), request.seat(), request.passengers());
        } else if (request.action() == BookingAction.CANCEL) {
            actionResult = seatMap.cancelBook(request.row(), request.seat(), request.passengers());
        } else {
            return OperationResult.failure("The operation " + request.action() + " not yet supported");
        }

        if (!actionResult.isSuccess()) {
            return actionResult;
        }

        FileOperations.getInstance().saveBookingMap(seatMap, filePath);
        return OperationResult.success();

    }

    /**
     * Verifying the command line operation in the original form of string
     * See the overload of this method for more information on params and outcomes expectations
     *
     */
    BookingRequest terminalToBookingRequest(String commandLineRequest) throws MalformedRequestException {
        if (commandLineRequest.isEmpty()) {
            throw new MalformedRequestException("operation is an empty string");
        }
        return terminalToBookingRequest(commandLineRequest.split(" "));
    }

    /**
     * Verifying if the operation of booking is a correctly constructed operation. The valid operation should contain
     * operation suported by BookingProcessor, like BOOK or CANCEL, as well as seat row and number,
     * as well as number of passengers.
     * Seat should be provided in a form of [LETTER][SEAT NUMBER]. E.G. A1, C5.
     * \b
     * Here is a couple of example of a valid requests:
     * BOOK A0 1
     * CANCEL A0 1
     * BOOK B3 5
     * CANCEL C2 6
     *
     * @param commandBlocks the booking flight operations as string[] array to proceed with
     * @return string array with operation blocks
     */
    public BookingRequest terminalToBookingRequest(String[] commandBlocks) throws MalformedRequestException {

        BookingRequest.Builder brBuilder = new BookingRequest.Builder();
        if (commandBlocks.length != 3) {
            throw new MalformedRequestException("Command is not constructed in a desired form." +
                    Arrays.toString(commandBlocks) + ". Valid form examples: \"BOOK A0 1\" or \"CANCEL A5 3\" ");
        }

        try {
            BookingAction action = BookingAction.valueOf(commandBlocks[0]);
            brBuilder.setAction(action);
        } catch (IllegalArgumentException ex) {
            throw new MalformedRequestException("Operation " + commandBlocks[0] + " is not supported");
        }

        final String place = commandBlocks[1];
        if (place.length() != 2
                || place.charAt(0) < 'A' || place.charAt(0) > 'Z'
                || place.charAt(1) < '0' || place.charAt(1) > '9') {
            throw new MalformedRequestException("The place is defined incorrectly. " +
                    "Cannot proceed an operation on the " + place + " seat.");
        }

        brBuilder.setRow(commandBlocks[1].charAt(0));
        brBuilder.setSeat(commandBlocks[1].charAt(1) - '0');

        final String passengersNumber = commandBlocks[2];
        try {
            int passengersInt = Integer.parseInt(passengersNumber);
            if (passengersInt < 1) {
                throw new MalformedRequestException(
                        "Cannot proceed an operation with " + passengersInt + " number of passengers. " +
                                "Consider providing the positive number of customers");
            }
            brBuilder.setPassenger(passengersInt);
        } catch (NumberFormatException nfe) {
            throw new MalformedRequestException("Cannot proceed an operation with " + passengersNumber +
                    " number of passengers. Consider provide a valid integer", nfe);
        }


        return brBuilder.build();
    }

}
