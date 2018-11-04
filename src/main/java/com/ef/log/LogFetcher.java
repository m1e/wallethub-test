package com.ef.log;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public interface LogFetcher {

    List<Log> fetch(URI uri) throws IOException;

}

