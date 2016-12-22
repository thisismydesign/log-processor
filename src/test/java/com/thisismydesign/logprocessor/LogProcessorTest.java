package com.thisismydesign.logprocessor;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LogProcessorTest {

    private String[] keyWords = new String[]{"should", "contain"};
    private LocalDateTime after = LocalDateTime.of(2000, 1, 1, 1, 1);
    private DateTimeFormatter formatter;
    private String timestampDelimiter = "_";

    private List<String> lines;

    @Before
    public void init() {
        formatter = DateTimeFormatter.ISO_DATE_TIME;
        lines = new ArrayList<>();
    }

    @Test
    public void findIn_WhenContainsKeyWordAndAfterDate_ShouldReturnLine() throws Exception {
        LogProcessor logProcessor = new LogProcessor(keyWords, after, formatter, timestampDelimiter);
        String timeStamp = LocalDateTime.now().format(formatter);
        String containingLine = getTimeStampedLine(timeStamp);
        lines.add("random content");
        lines.add(containingLine);
        lines.add("random content");
        assertEquals(containingLine, logProcessor.findIn(lines));
    }

    @Test
    public void findIn_WhenContainsKeyWordAndBeforeDate_ShouldReturnNull() throws Exception {
        LogProcessor logProcessor = new LogProcessor(keyWords, after, formatter, timestampDelimiter);
        String timeStamp = LocalDateTime.of(1999, 1, 1, 1, 1).format(formatter);
        String containingLine = getTimeStampedLine(timeStamp);
        lines.add("random content");
        lines.add(containingLine);
        lines.add("random content");
        assertNull(logProcessor.findIn(lines));
    }

    @Test
    public void findIn_WhenDoesNotContainKeyWord_ShouldReturnNull() throws Exception {
        LogProcessor logProcessor = new LogProcessor(keyWords);
        lines.add("random content");
        lines.add("random content");
        assertNull(logProcessor.findIn(lines));
    }

    @Test(expected=IllegalArgumentException.class)
    public void findIn_WhenDateTimeIsNotComplete_ShouldThrowException() throws Exception {
        formatter = DateTimeFormatter.ISO_DATE;
        LogProcessor logProcessor = new LogProcessor(keyWords, after, formatter, timestampDelimiter);
        String timeStamp = LocalDateTime.now().format(formatter);
        String containingLine = getTimeStampedLine(timeStamp);
        lines.add(containingLine);
        logProcessor.findIn(lines);
    }

    @Test
    public void findIn_WhenDateTimeIsNotCompleteButTimeStampPrefixIsSet_ShouldReturnLine() throws Exception {
        String timestampPrefix = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE) + "T";
        LogProcessor logProcessor = new LogProcessor(keyWords, after, formatter, timestampDelimiter, timestampPrefix);
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ISO_TIME);
        String containingLine = getTimeStampedLine(timeStamp);
        lines.add(containingLine);
        assertEquals(containingLine, logProcessor.findIn(lines));
    }

    private String getTimeStampedLine(String timeStamp) {
        return String.format("%s%s%s", timeStamp, timestampDelimiter, keyWords[0]);
    }

}