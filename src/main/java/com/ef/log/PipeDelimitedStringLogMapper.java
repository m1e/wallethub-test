package com.ef.log;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.apache.commons.lang3.Validate.notEmpty;

@Component
public class PipeDelimitedStringLogMapper implements LogMapper<String> {

    private static final String IPV_4_PATTERN = "(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
    private static final String IPV_6_PATTERN = "([0-9a-f]{1,4}:){7}([0-9a-f]){1,4}";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final String PIPE_DELIMITER = "\\|";

    public Log map(String line) {

        notEmpty(line);

        String[] lineItems = line.split(PIPE_DELIMITER);

        Optional.of(lineItems)
                .filter(x -> x.length == 5)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Line should represent 5 fields delimited by '|'," +
                                        "but there are only " + lineItems.length + " fields in: \n" + line)
                );

        return Log.builder()
                .dateTime(parseDate(lineItems[0]))
                .ipAddress(validateIp(lineItems[1]))
                .request(lineItems[2])
                .status(parseStatus(lineItems[3]))
                .userAgent(lineItems[4])
                .build();
    }

    private LocalDateTime parseDate(String date) {
        return LocalDateTime.parse(date, DATE_FORMATTER);
    }

    private Integer parseStatus(String status) {
        return Integer.parseInt(status);
    }

    private String validateIp(String ip) {
        return Optional.of(ip)
                .filter(x -> x.matches(IPV_6_PATTERN) || x.matches(IPV_4_PATTERN))
                .orElseThrow(() -> new IllegalArgumentException(ip + " is not a valid IP"));
    }
}

