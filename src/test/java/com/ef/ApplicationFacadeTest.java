package com.ef;

import com.ef.ip.BlockedIp;
import com.ef.ip.BlockedIpService;
import com.ef.log.Log;
import com.ef.log.LogService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ApplicationFacadeTest {

    private static final String BLOCK_REASON_MESSAGE_FORMAT = "IP address '%s' has been blocked as it has exceeded maximum %s requests threshold";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private ApplicationFacade applicationFacade;

    private LogService logService;
    private BlockedIpService blockedIpService;

    @Before
    public void init() {
        applicationFacade = new ApplicationFacade(
                logService = mock(LogService.class),
                blockedIpService = mock(BlockedIpService.class)
        );
    }

    @Test
    public void processLogs() throws Exception {

        URI uri = new URI("file:///test");

        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = from.plusDays(1);
        Long threshold = 100L;

        List<Log> logs = Arrays.asList(
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


        List<BlockedIp> blockedIps = Arrays.asList(
                BlockedIp.builder().address(logs.get(0).getIpAddress())
                        .reason(format(BLOCK_REASON_MESSAGE_FORMAT, logs.get(0).getIpAddress(), threshold))
                        .build(),
                BlockedIp.builder().address(logs.get(1).getIpAddress())
                        .reason(format(BLOCK_REASON_MESSAGE_FORMAT, logs.get(1).getIpAddress(), threshold))
                        .build(),
                BlockedIp.builder().address(logs.get(2).getIpAddress())
                        .reason(format(BLOCK_REASON_MESSAGE_FORMAT, logs.get(2).getIpAddress(), threshold))
                        .build()
        );

        when(logService.fetch(uri)).thenReturn(logs);
        when(logService.findIpsThatExceededThreshold(from, to, threshold)).thenReturn(Arrays.asList(
                logs.get(0).getIpAddress(),
                logs.get(1).getIpAddress(),
                logs.get(2).getIpAddress()
        ));

        applicationFacade.processLogs(uri, from, to, threshold);

        verify(logService, times(1)).fetch(uri);
        verify(logService, times(1)).saveAll(logs);
        verify(blockedIpService, times(1)).saveAll(blockedIps);

    }

    private LocalDateTime parseDate(String date) {
        return LocalDateTime.parse(date, DATE_FORMATTER);
    }

}