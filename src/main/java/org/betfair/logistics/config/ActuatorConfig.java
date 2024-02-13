package org.betfair.logistics.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ActuatorConfig implements InfoContributor {

    private final CompanyInfo companyInfo;

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, String> userDetails = new HashMap<>();
        userDetails.put("Current Date", companyInfo.getCurrentDate().toString());
        userDetails.put("Company Profit", companyInfo.getCompanyProfit().toString());

        builder.withDetail("Company Info", userDetails);
    }
}
