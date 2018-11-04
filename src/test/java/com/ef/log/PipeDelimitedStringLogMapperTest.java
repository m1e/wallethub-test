package com.ef.log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.format.DateTimeParseException;

import static com.ef.utils.DateUtils.parseDate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
public class PipeDelimitedStringLogMapperTest {

    private PipeDelimitedStringLogMapper mapper = new PipeDelimitedStringLogMapper();

    @Test
    public void map_valid_string_success() {

        String validLogRow = "2017-01-01 00:00:11.763|192.168.234.81|\"GET / HTTP/1.1\"|200|\"swcd 1(unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"\n";

        Log expected = Log.builder()
                .ipAddress("192.168.234.81")
                .request("\"GET / HTTP/1.1\"")
                .status(200)
                .userAgent("\"swcd 1(unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"\n")
                .dateTime(parseDate("2017-01-01 00:00:11.763"))
                .build();

        Log actual = mapper.map(validLogRow);

        assertThat(expected, is(actual));
    }

    @Test(expected = IllegalArgumentException.class)
    public void map_row_with_invalid_ip_throws_exception() {

        String rowWithInvalidIp = "2017-01-01 00:00:11.763|192f.168.234.81|\"GET / HTTP/1.1\"|200|\"swcd 1(unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"\n";

        mapper.map(rowWithInvalidIp);
    }


    @Test(expected = DateTimeParseException.class)
    public void map_row_with_invalid_date_throws_exception() {

        String rowWithInvalidDate = "2017-01-01 00:00s:11.763|192.168.234.81|\"GET / HTTP/1.1\"|200|\"swcd 1(unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"\n";

        mapper.map(rowWithInvalidDate);
    }

    @Test(expected = NumberFormatException.class)
    public void map_row_with_invalid_status_throws_exception() {

        String rowWithInvalidStatus = "2017-01-01 00:00:11.763|192.168.234.81|\"GET / HTTP/1.1\"|20s0|\"swcd 1(unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"\n";

        mapper.map(rowWithInvalidStatus);
    }

    @Test(expected = IllegalArgumentException.class)
    public void map_row_with_wrong_number_of_delimited_fields_throws_exception() {

        String rowWith4DelimitedFields = "192.168.234.81|\"GET / HTTP/1.1\"|200|\"swcd 1(unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"\n";

        mapper.map(rowWith4DelimitedFields);
    }
}