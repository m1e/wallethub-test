package com.ef.log;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

public interface LogService {

    List<Log> fetch(URI uri) throws IOException;

    List<String> findIpsThatExceededThreshold(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Long threshold
    );

    void saveAll(Iterable<Log> logs);
}

