// src/main/java/org/example/diadp1backend/util/TimestampFormatter.java
package org.example.diadp1backend.util;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimestampFormatter {

    public static String formatTimestamp(Timestamp timestamp) {
        LocalDateTime dateTime = timestamp.toLocalDateTime();
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (dateTime.toLocalDate().equals(today)) {
            return "Hoy, " + dateTime.format(timeFormatter);
        } else if (dateTime.toLocalDate().equals(yesterday)) {
            return "Ayer, " + dateTime.format(timeFormatter);
        } else {
            return dateTime.format(dateFormatter) + ", " + dateTime.format(timeFormatter);
        }
    }
}