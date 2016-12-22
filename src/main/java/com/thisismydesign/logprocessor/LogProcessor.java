package com.thisismydesign.logprocessor;

import com.thisismydesign.stringprocessor.StringProcessor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class LogProcessor {

    private String[] keyWords;
    private LocalDateTime after;
    private DateTimeFormatter formatter;
    private String timestampDelimiter;
    private String timestampPrefix = "";

    public LogProcessor(String[] keyWords, LocalDateTime after, DateTimeFormatter formatter, String timestampDelimiter, String timestampPrefix) {
        this.keyWords = keyWords;
        this.after = after;
        this.formatter = formatter;
        this.timestampDelimiter = timestampDelimiter;
        this.timestampPrefix = timestampPrefix;
    }

    public LogProcessor(String[] keyWords, LocalDateTime after, DateTimeFormatter formatter, String timestampDelimiter) {
        this(keyWords, after, formatter, timestampDelimiter, "");
    }

    public LogProcessor(String[] keyWords) {
        this.keyWords = keyWords;
    }

    public String findIn(List<?> lines) throws IOException {
        Optional<?> found = lines.stream().filter(line -> isTimeStampAfter(after, formatter, timestampDelimiter,
                timestampPrefix, (String) line) && StringProcessor.containsAny(keyWords, (String)line)).findAny();

        return found.isPresent() ? (String) found.get() : null;
    }

    private boolean isTimeStampAfter(LocalDateTime after, DateTimeFormatter formatter, String timestampDelimiter, String timestampPrefix, String line) {
        if (after != null && formatter != null) {
            String[] lineParts = line.split(timestampDelimiter);
            if(lineParts.length > 1) {
                if (!isAfter(timestampPrefix + lineParts[0].trim(), after, formatter)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isAfter(String timestamp, LocalDateTime after, DateTimeFormatter formatter) {
        LocalDateTime lineTimeStamp = LocalDateTime.parse(timestamp, formatter);
        return lineTimeStamp.isAfter(after) || lineTimeStamp.equals(after);
    }
}
