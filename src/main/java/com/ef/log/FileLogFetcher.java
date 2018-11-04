package com.ef.log;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

@AllArgsConstructor
@Component
public class FileLogFetcher implements LogFetcher {

    private final LogMapper<String> fromLineToLogMapper;

    public List<Log> fetch(URI uri) throws IOException {

        checkNotNull(uri);

        return Files.readAllLines(Paths.get(uri))
                .parallelStream()
                .map(fromLineToLogMapper::map)
                .collect(Collectors.toList());
    }

}

