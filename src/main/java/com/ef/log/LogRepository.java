package com.ef.log;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface LogRepository extends CrudRepository<Log, UUID> {

    @Query(value =
            " select ipAddress from Log where dateTime between :startDate and :endDate  " +
                    " group by(ipAddress) having count(ipAddress) > :threshold "
    )
    List<String> findIpsThatExceededThreshold(
            @Param(value = "startDate") LocalDateTime startDate,
            @Param(value = "endDate") LocalDateTime endDate,
            @Param(value = "threshold") Long threshold
    );
}

