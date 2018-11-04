package com.ef;

import com.ef.ip.BlockedIp;
import com.ef.ip.BlockedIpService;
import com.ef.log.Log;
import com.ef.log.LogService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Component
@AllArgsConstructor
@Slf4j
public class ApplicationFacade {

    private static final String BLOCK_REASON_MESSAGE_FORMAT = "IP address '%s' has been blocked as it has exceeded maximum %s requests threshold";

    private final LogService logService;
    private final BlockedIpService blockedIpService;


    @SneakyThrows
    public void processLogs(URI uri, LocalDateTime startDate, LocalDateTime endDate, Long threshold) {

        checkNotNull(uri, "uri must not be null");
        checkNotNull(startDate, "startDate must not be null");
        checkNotNull(endDate, "endDate must not be null");
        checkNotNull(threshold);
        checkArgument(threshold >= 0, "threshold must be a positive number");
        checkArgument(endDate.isAfter(startDate), "startDate must be less than endDate");

        log.info("Logs processing started");

        List<Log> rows = logService.fetch(uri);

        log.info("{} log rows fetched", rows.size());

        logService.saveAll(rows);

        log.info("{} log rows saved", rows.size());

        List<BlockedIp> ipsWithExceededThreshold = logService
                .findIpsThatExceededThreshold(startDate, endDate, threshold)
                .parallelStream()
                .map(ipAddress -> BlockedIp.builder()
                        .address(ipAddress)
                        .reason(format(BLOCK_REASON_MESSAGE_FORMAT, ipAddress, threshold))
                        .build()
                )
                .collect(toList());

        log.info("{} IPs will be blocked", ipsWithExceededThreshold.size());

        log.info("These IPs are {}", ipsWithExceededThreshold.stream().map(BlockedIp::getAddress).collect(toList()));

        blockedIpService.saveAll(ipsWithExceededThreshold);

        log.info("Logs processing has been successfully finished");

    }


}
