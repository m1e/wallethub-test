package com.ef.log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static com.ef.utils.DateUtils.parseDate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {FileLogFetcher.class, PipeDelimitedStringLogMapper.class})
public class FileLogFetcherIntegrationTest {

    @Autowired
    private FileLogFetcher fileLogFetcher;

    @Test
    public void fetch_success() throws Exception {

        URI uri = getClass().getClassLoader().getResource("access.log").toURI();

        List<Log> actual = fileLogFetcher.fetch(uri);

        List<Log> expected = Arrays.asList(
                Log.builder()
                        .ipAddress("192.168.234.81")
                        .request("\"GET / HTTP/1.1\"")
                        .status(200)
                        .userAgent("\"swcd 1(unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"")
                        .dateTime(parseDate("2017-01-01 00:00:11.763"))
                        .build(),
                Log.builder()
                        .ipAddress("192.168.234.82")
                        .request("\"GET / HTTP/2.1\"")
                        .status(500)
                        .userAgent("\"swcd 2(unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"")
                        .dateTime(parseDate("2018-02-02 00:00:21.863"))
                        .build(),
                Log.builder()
                        .ipAddress("192.168.234.83")
                        .request("\"GET / HTTP/1.1\"")
                        .status(404)
                        .userAgent("\"swcd 3(unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"")
                        .dateTime(parseDate("2019-03-03 00:00:31.963"))
                        .build()
        );

        assertThat(expected, contains(actual.toArray()));
    }
}