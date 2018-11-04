CREATE TABLE IF NOT EXISTS log (
  id BINARY(16) NOT NULL PRIMARY KEY,
  ip_address varchar(50) NOT NULL,
  date_time  timestamp NOT NULL,
  request varchar(255) NOT NULL,
  status int(11) NOT NULL,
  user_agent varchar(1024) NOT NULL,
  KEY log__ip_address_idx (ip_address),
  KEY log__date_time_idx (date_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS blocked_ip (
  id bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  address varchar(50) NOT NULL,
  reason varchar(255) NOT NULL,
  KEY blocked_ip__address_idx (address)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;