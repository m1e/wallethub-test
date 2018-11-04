package com.ef.ip;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockedIpRepository extends CrudRepository<BlockedIp, Long> {

}
