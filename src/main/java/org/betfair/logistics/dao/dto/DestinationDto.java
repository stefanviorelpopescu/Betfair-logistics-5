package org.betfair.logistics.dao.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(type = "number", example = "10")
    private Long id;

    @Schema(type = "string", example = "Ploiesti")
    @NotBlank(message = "Name must not be blank")
    private String name;

    @Schema(type = "number", example = "50")
    @NotNull(message = "Distance is mandatory")
    @Positive(message = "Distance must be > 0")
    private Integer distance;

    @JsonIgnore
    @AssertTrue(message = "Distance must be > name.length()")
    public boolean isDistanceLongerThanName() {
        return distance > name.length();
    }
}
