package org.betfair.logistics.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
@Getter
public class CompanyInfo {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE;

    private Long companyProfit = 0L;

    private LocalDate currentDate = LocalDate.of(2021, 12, 14);

    public LocalDate getLocalDateFromString(String dateAsString) {
        return LocalDate.parse(dateAsString, DATE_FORMATTER);
    }

    public Long getMillisFromLocalDate(LocalDate localDate) {
        return localDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000;
    }

    public Long getCurrentDateAsMillis() {
        return currentDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000;
    }

}
