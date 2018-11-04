package com.ef.log;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@AllArgsConstructor
@Service
@Transactional
public class LogServiceImpl implements LogService {

    private final LogFetcher logFetcher;
    private final LogRepository logRepository;

    public List<Log> fetch(URI uri) throws IOException {

        checkNotNull(uri);

        return logFetcher.fetch(uri);
    }

    public List<String> findIpsThatExceededThreshold(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Long threshold) {

        checkNotNull(startDate, "startDate must not be null");
        checkNotNull(endDate, "endDate must not be null");
        checkNotNull(threshold);
        checkArgument((threshold >= 0L), "threshold must be a positive number");
        checkArgument(endDate.isAfter(startDate), "startDate must be less than endDate");

        return logRepository.findIpsThatExceededThreshold(startDate, endDate, threshold);
    }

    public void saveAll(Iterable<Log> logRows) {

        checkNotNull(logRows, "logRows must not be null");

        logRepository.saveAll(logRows);
    }
}

