package com.ef.ip;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.google.common.base.Preconditions.checkNotNull;

@AllArgsConstructor
@Service
@Transactional
public class BlockedIpServiceImpl implements BlockedIpService {

    private final BlockedIpRepository blockedIpRepository;

    @Override
    public void saveAll(Iterable<BlockedIp> blockedIps) {

        checkNotNull(blockedIps);

        blockedIpRepository.saveAll(blockedIps);
    }
}
