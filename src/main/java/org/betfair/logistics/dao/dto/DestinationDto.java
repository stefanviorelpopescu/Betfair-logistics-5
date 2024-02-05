package org.betfair.logistics.dao.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DestinationDto {

    private Long id;

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotNull(message = "Distance is mandatory")
    @Positive(message = "Distance must be > 0")
    private Integer distance;

    @JsonIgnore
    @AssertTrue(message = "Distance must be > name.length()")
    public boolean isDistanceLongerThanName() {
        return distance > name.length();
    }
}
