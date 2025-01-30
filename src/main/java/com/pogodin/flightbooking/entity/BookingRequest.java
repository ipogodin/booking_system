package com.pogodin.flightbooking.entity;

import com.pogodin.flightbooking.BookingAction;

public record BookingRequest(BookingAction action, char row, int seat, int passengers) {

    public static class Builder {
        private BookingAction action;
        private char row;
        private int seat;
        private int passenger;

        public Builder setAction(BookingAction action) {
            this.action = action;
            return this;
        }

        public Builder setRow(char row) {
            this.row = row;
            return this;
        }

        public Builder setSeat(int seat) {
            this.seat = seat;
            return this;
        }

        public Builder setPassenger(int passenger) {
            this.passenger = passenger;
            return this;
        }

        public BookingRequest build() {
            return new BookingRequest(action, row, seat, passenger);
        }
    }
}
