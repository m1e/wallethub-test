SELECT `ip_address`, count(`id`) AS `number_of_requests`
FROM `log`
WHERE `date_time` >= '2017-01-01T13:00:00' AND `date_time` < '2017-01-01T14:00:00'
GROUP BY `ip_address`
HAVING `number_of_requests` > 100;
