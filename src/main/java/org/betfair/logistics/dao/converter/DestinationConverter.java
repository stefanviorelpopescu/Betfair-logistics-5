package org.betfair.logistics.dao.converter;

import org.betfair.logistics.dao.dto.DestinationDto;
import org.betfair.logistics.dao.entity.Destination;

import java.util.List;

public class DestinationConverter {

    public static DestinationDto modelToDto(Destination destination) {
        return DestinationDto.builder()
                .id(destination.getId())
                .name(destination.getName())
                .distance(destination.getDistance())
                .build();
    }

    public static List<DestinationDto> modelListToDtoList(List<Destination> destinations) {
        return destinations.stream()
                    .map(DestinationConverter::modelToDto)
                    .toList();
    }

    public static Destination dtoToModel(DestinationDto destinationDto) {
        return Destination.builder()
                .id(destinationDto.getId())
                .distance(destinationDto.getDistance())
                .name(destinationDto.getName())
                .build();
    }
}
