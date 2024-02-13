package org.betfair.logistics.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.betfair.logistics.dao.dto.DestinationDto;
import org.betfair.logistics.exception.CannotCreateResourceException;
import org.betfair.logistics.exception.ResourceNotFoundException;
import org.betfair.logistics.service.DestinationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/destinations")
@RequiredArgsConstructor
public class DestinationController {

    private final DestinationService destinationService;

    @GetMapping("/{id}")
    public DestinationDto getDestinationById(@PathVariable(name = "id") Long destinationId) throws ResourceNotFoundException {
        return destinationService.getDestinationById(destinationId);
    }

    @GetMapping
    public List<DestinationDto> getAllDestinations() {
        return destinationService.getAllDestinations();
    }

    @DeleteMapping("/{id}")
    public void deleteDestinationById(@PathVariable(name = "id") Long destinationId) throws ResourceNotFoundException {
        destinationService.deleteDestinationById(destinationId);
    }

    @PostMapping
    public Long createDestination(@RequestBody @Valid DestinationDto destinationDto) throws CannotCreateResourceException {
        return destinationService.addDestination(destinationDto);
    }

    @PutMapping
    public DestinationDto updateDestination(@RequestBody @Valid DestinationDto destinationDto) throws CannotCreateResourceException {
        return destinationService.updateDestination(destinationDto);
    }

}
