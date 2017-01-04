# LogProcessor

### Utility that supports searching for log entries based on content and optionally after specific timestamp.

### API

#### public LogProcessor(String[] keyWords, LocalDateTime after, DateTimeFormatter formatter, String timestampDelimiter, String timestampPrefix)
Creates LogProcessor object. Constructors with less parameters are available for more simple use-cases. Format of timestamp in log entries should be provided as DateTimeFormatter 'formatter'. The assumption is made that the log entry starts with the timestamp. String 'timestampDelimiter' is the characters after the timestamp and before the actual log entry. String 'timestampPrefix' can be used to extend the timestamp present in the log, this value will be at the beginning of the timestamp before parsing it. (E.g. in some cases the year is not present in the timestamp and therefore it cannot be parsed into a LocalDateTime object. 'timestampPrefix' can be used to add the current year before the timestamp. Its presence should be taken into consideration when providing DateTimeFormatter 'formatter'.)

#### public String findIn(List<?> lines)
Return the first line matching the criteria provided in constructor. Otherwise returns null.
