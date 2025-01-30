## **Flight Booking System**

### **📌 Description**
The **Flight Booking System** is a command-line application that allows users to **book** and **cancel seats** on a plane with **20 rows (A-T) and 8 seats per row**.

Users can execute booking and cancellation operations using command-line arguments in the following format:

- **Booking a seat:**
  ```
  BOOK D4 3
  ```
    - **BOOK** → Operation type (Booking a seat)
    - **D4** → Seat row and number
    - **3** → Number of passengers willing to sit together in the same row

- **Canceling a seat reservation:**
  ```
  CANCEL C5 2
  ```
    - **CANCEL** → Operation type
    - **C5** → Seat row and number
    - **2** → Number of passengers whose seats will be freed

### **📦 Features**
- Supports **seat reservations** and **cancellations**.
- Ensures **contiguous seating** for group bookings.
- Prevents **double-booking** and **out-of-bounds errors**.
- Saves and loads seat reservations from a **CSV file** (`booking_the_flight.csv`).
- Logs all operations in a `logs/app.log` file near the JAR file.

---

## **🚀 Running the JAR File**
### **1️⃣ Build the JAR File**
If you haven't already built the JAR file, use Maven:
```sh
mvn clean package
```
The compiled JAR will be located in:
```
target/flight-booking-1.0-SNAPSHOT.jar
```

### **2️⃣ Run the JAR with Booking or Cancellation Commands**
To run the JAR, use the following syntax:
```sh
java -jar target/flight-booking-1.0.001.jar BOOK D4 3
```
or
```sh
java -jar target/flight-booking-1.0.001.jar CANCEL C5 2
```

---

## **📝 Logging & Data Storage**
- **All seat bookings are stored in:** `booking_the_flight.csv`.
- **Log messages (errors, warnings, info) are stored in:** `logs/app.log`.
- Logs include details on **failed operations** for debugging purposes.

---

## **✅ Example Usage**
### **Book 3 seats in row D starting from seat 4**
```sh
java -jar target/flight-booking-1.0.001.jar BOOK D4 3
```
✅ **Expected Output:**
```
SUCCESS
```

### **Try to book 5 seats starting from T7 (out of bounds)**
```sh
java -jar target/flight-booking-1.0.001.jar BOOK T7 5
```
❌ **Expected Output:**
```
FAIL
```

### **Cancel a previously booked seat (C5, 2 passengers)**
```sh
java -jar target/flight-booking-1.0.001.jar CANCEL C5 2
```
✅ **Expected Output:**
```
SUCCESS
```

---

## **🔍 Running Unit Tests**
To validate the system's functionality, run the unit tests:
```sh
mvn test
```
This will execute test cases, including:
- **Valid seat bookings**
- **Overlapping reservations**
- **Out-of-bounds errors**
- **Seat cancellations & re-bookings**

---

## **🎯 Summary**
✔ **Command-line flight seat reservation system**  
✔ **Handles booking, cancellation, and error cases**  
✔ **Stores data in CSV and logs operations**  
✔ **Packaged as a runnable JAR**

---