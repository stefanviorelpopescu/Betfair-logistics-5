package org.betfair.logistics.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Getter
public class CompanyInfo {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE;

    private final AtomicLong companyProfit = new AtomicLong(0L);

    private LocalDate currentDate = LocalDate.of(2021, 12, 14);

    public Long advanceDate() {
        currentDate = currentDate.plusDays(1);
        return getMillisFromLocalDate(currentDate);
    }

    public void increaseProfit(Long amount) {
        companyProfit.addAndGet(amount);
    }

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
