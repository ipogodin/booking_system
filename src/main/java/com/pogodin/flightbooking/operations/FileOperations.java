package com.pogodin.flightbooking.operations;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import com.pogodin.flightbooking.entity.FlightSeatsMap;
import com.pogodin.flightbooking.exception.BookingSaveFileException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileOperations {
    private static FileOperations instance;

    private FileOperations() {}

    public static FileOperations getInstance() {
        if (instance == null) {
            instance = new FileOperations();
        }
        return instance;
    }

    public void saveBookingMap(FlightSeatsMap seatMap, String filePath) {
        boolean[][] seatMappingArr = seatMap.getSnapshot();
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            for (boolean[] row : seatMappingArr) {
                String[] rowAsString = new String[row.length];
                for (int i = 0; i < row.length; i++) {
                    rowAsString[i] = row[i] ? "1" : "0"; // Convert double to String
                }
                writer.writeNext(rowAsString); // Write the row to the CSV
            }
        } catch (IOException e) {
            throw new BookingSaveFileException(
                    "Exception during the attempt on saving the seat map booking: " + filePath);
        }
    }

    public FlightSeatsMap loadBookingMap(String filePath) {
        List<boolean[]> dataList = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                boolean[] row = new boolean[nextLine.length];
                for (int i = 0; i < nextLine.length; i++) {
                    row[i] = nextLine[i].equals("1");
                }
                dataList.add(row);
            }
        } catch (IOException | CsvValidationException e) {
            throw new BookingSaveFileException(
                    "Exception on reading the booking data. File might be corrupted or missed: " + filePath, e);
        }
        boolean[][] seatingMapArray = dataList.toArray(new boolean[0][]);
        if (seatingMapArray.length == 0) {
            throw new BookingSaveFileException(
                    "Booking file loaded no booking data. Verify file is not empty: " + filePath);
        }
        return new FlightSeatsMap(seatingMapArray);
    }
}
