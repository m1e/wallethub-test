package com.ef.log;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Entity
public class Log {
    //uuid is used to enable batch inserts for logs
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String ipAddress;
    private String request;
    private Integer status;
    private String userAgent;
    private LocalDateTime dateTime;

}

