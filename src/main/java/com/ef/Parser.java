package com.ef;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.lang.String.format;

@Slf4j
@SpringBootApplication
public class Parser implements ApplicationRunner {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");

    private static final String START_DATE = "startDate";
    private static final String DURATION = "duration";
    private static final String REQUIRED_ARGUMENT_MISSING_ERROR_MESSAGE = "Required '%s' argument is not defined";
    private static final String THRESHOLD = "threshold";
    private static final String PATH_TO_LOG_FILE = "accesslog";

    @Autowired
    private ApplicationFacade applicationFacade;


    public static void main(String[] args) {
        SpringApplication.run(Parser.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {


        LocalDateTime startDate = getStartDate(args);
        LocalDateTime endDate = getEndDate(args, startDate);
        Long threshold = getThreshold(args);
        URI uri = getUri(args);

        applicationFacade.processLogs(uri, startDate, endDate, threshold);

    }


    private URI getUri(ApplicationArguments args) {
        return args.getOptionValues(PATH_TO_LOG_FILE)
                .stream()
                .map(x -> Paths.get(x))
                .map(Path::toUri)
                .findAny()
                .orElseThrow(() -> new IllegalStateException(requiredArgMissingMessage(PATH_TO_LOG_FILE)));
    }

    private LocalDateTime getStartDate(ApplicationArguments args) {
        return args.getOptionValues(START_DATE)
                .stream()
                .findAny()
                .map(this::parseDate)
                .orElseThrow(() -> new IllegalStateException(requiredArgMissingMessage(START_DATE)));
    }

    private LocalDateTime getEndDate(ApplicationArguments args, LocalDateTime startDate) {
        return args.getOptionValues(DURATION)
                .stream()
                .findAny()
                .map(
                        duration -> {
                            switch (Duration.valueOf(duration)) {
                                case hourly:
                                    return startDate.plusHours(1);
                                case daily:
                                    return startDate.plusDays(1);

                            }
                            return startDate;
                        }
                )
                .orElseThrow(() -> new IllegalStateException(requiredArgMissingMessage(DURATION)));
    }

    private Long getThreshold(ApplicationArguments args) {
        return args.getOptionValues(THRESHOLD)
                .stream()
                .findAny()
                .map(Long::parseLong)
                .orElseThrow(() -> new IllegalStateException(requiredArgMissingMessage(THRESHOLD)));
    }

    private LocalDateTime parseDate(String date) {
        return LocalDateTime.parse(date, DATE_FORMATTER);
    }

    private String requiredArgMissingMessage(String argName) {
        return format(REQUIRED_ARGUMENT_MISSING_ERROR_MESSAGE, argName);
    }

    private enum Duration {

        daily, hourly
    }

}
