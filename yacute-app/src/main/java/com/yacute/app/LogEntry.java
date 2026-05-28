package com.yacute.app;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogEntry {
    private final StringProperty dateTime;
    private static final DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LogEntry(LocalDateTime dateTime) {
        this.dateTime = new SimpleStringProperty(dateTime.format(formatter));
    }

    public String getDateTime() {
        return dateTime.get();
    }

    public StringProperty dateTimeProperty() {
        return dateTime;
    }
}
