package com.pogodin.flightbooking.entity;

import com.pogodin.flightbooking.OperationResult;

import java.util.Arrays;

/**
 * Stores the seat map inside the plane
 */
public class FlightSeatsMap {
    // Number of letters in english
    private static final int MAX_FEASIBLE_ROW_NUMBER = 26;

    /**
     * Occupied seats map represented as array
     * First index represents the row. A is zero index, B first, etc.
     * Second index represents the seat in the row. 1 is zero index, 2 is first index, etc.
     * Size of the array settled in the constructor
     */
    private final boolean[][] occupiedMap;

    // rows are from A to Z
    private final char lastRow;
    private final int seatsInARow;

    public FlightSeatsMap(int rowsNumber, int seatsInRow) {
        this.occupiedMap = new boolean[rowsNumber][seatsInRow];
        if (rowsNumber > MAX_FEASIBLE_ROW_NUMBER) {
            throw new IllegalArgumentException("No feasible way to create the plane map with " + rowsNumber + " rows");
        }

        this.lastRow = (char) ('A' + (rowsNumber - 1));
        this.seatsInARow = seatsInRow;
    }

    public FlightSeatsMap(boolean[][] occupiedMap) {
        if (occupiedMap.length == 0 || occupiedMap[0].length == 0) {
            throw new IllegalArgumentException("Occupied map cannot be an empty array");
        }
        this.lastRow = (char) ('A' + (occupiedMap.length));
        this.seatsInARow = occupiedMap[0].length;
        this.occupiedMap = Arrays.stream(occupiedMap).map(boolean[]::clone).toArray(boolean[][]::new);
    }

    /**
     * Book the seat for this flight. Book is made using the seat letter and number, E.G. A1, B2.
     * In case if seat is already occupied, return FAIL.
     * Return SUCCESS in case if seat was no occupied before and exists.
     *
     * @param row        the character representing a row in the plane.
     * @param seat       the number of the seat in the row, starting from 1.
     * @param passengers the number of passengers that would like to book a flight.
     * @return SUCCESS if the seat was booked successfully, FAIL otherwise.
     */
    public OperationResult bookSeat(Character row, int seat, int passengers) {
        int rowIndex = row - 'A';
        for (int i = 0; i < passengers; i++) {
            if (!seatAvailable(row, seat + i)) {
                return OperationResult.failure(
                        "No possible way to seat " + passengers + " customers together at this place and row.");
            }
        }

        for (int i = 0; i < passengers; i++) {
            occupiedMap[rowIndex][seat + i] = true;
        }

        return OperationResult.success();
    }

    /**
     * Set the seat free, ignoring the existent state of the seat. E.G. A1, B2.
     * Used for canceling the booking for this flight.
     *
     * @param row        the character representing a row in the plane
     * @param seat       the number of the seat in the row, starting from 1
     * @param passengers the number of passengers that would like to book a flight
     * @return SUCCESS if the seat exists, FAIL if seat does not exist in this plane map
     */
    public OperationResult cancelBook(Character row, Integer seat, int passengers) {
        int rowIndex = row - 'A';
        for (int i = 0; i < passengers; i++) {
            if (!validSeat(row, seat + i)) {
                return OperationResult.failure(
                        "Not possible to cancel the requested seats." +
                                " Seat " + row + (seat + i) + " does not exist");
            }
        }

        for (int i = 0; i < passengers; i++) {
            occupiedMap[rowIndex][seat + i] = false;
        }

        return OperationResult.success();
    }

    public boolean[][] getSnapshot() {
        return occupiedMap;
    }

    public char getLastRow() {
        return lastRow;
    }

    public int getSeatsInARow() {
        return seatsInARow;
    }

    boolean seatAvailable(Character row, Integer seat) {
        int rowIndex = row - 'A';
        return validSeat(row, seat) && !occupiedMap[rowIndex][seat];
    }

    boolean validSeat(Character row, Integer seat) {
        return row >= 'A' && row < lastRow && seat >= 0 && seat < seatsInARow;
    }

    /**
     * print a row seat map in a visual and readable format
     *
     * @return the plane booking map in visually readable format
     */
    public String toString() {
        StringBuilder sb = new StringBuilder()
                .append("row map in a readable format \n")
                .append(lastRow - 'A').append(" rows and ").append(seatsInARow).append(" seats").append("\n")
                .append(drawPlaneNose(seatsInARow)).append("\n");

        for (int i = 0; i < occupiedMap.length; i++) {
            for (int j = 0; j < seatsInARow; j++) {
                if (occupiedMap[i][j]) {
                    sb.append('\u25A0'); // filled square
                } else {
                    sb.append('\u25A1'); // empty square
                }
            }
            sb.append(' ').append(((char) ('A' + i))).append("\n");
        }
        sb.append("-".repeat(Math.max(0, seatsInARow)));


        return sb.toString();
    }

    private String drawPlaneNose(int width) {
        return " " + "_".repeat(width - 2) + " \n" + "/" +
                " ".repeat(width - 2) + "\\";
    }

}
