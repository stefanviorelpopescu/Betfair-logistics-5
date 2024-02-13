package org.betfair.logistics.service;

import lombok.RequiredArgsConstructor;
import org.betfair.logistics.cache.DestinationCache;
import org.betfair.logistics.dao.converter.DestinationConverter;
import org.betfair.logistics.dao.dto.DestinationDto;
import org.betfair.logistics.dao.entity.Destination;
import org.betfair.logistics.dao.repository.DestinationRepository;
import org.betfair.logistics.dao.repository.OrderRepository;
import org.betfair.logistics.exception.CannotCreateResourceException;
import org.betfair.logistics.exception.ResourceNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.betfair.logistics.dao.converter.DestinationConverter.dtoToModel;

@Service
@RequiredArgsConstructor
public class DestinationService {

    private final DestinationRepository destinationRepository;
    private final OrderRepository orderRepository;
    private final DestinationCache destinationCache;

    public List<DestinationDto> getAllDestinations() {
        List<Destination> destinationModels = destinationCache.findAll();
        return DestinationConverter.modelListToDtoList(destinationModels);
    }

    @Cacheable("destinations")
    public DestinationDto getDestinationById(Long destinationId) throws ResourceNotFoundException {

        Destination destination = destinationCache.findById(destinationId)
                .orElseThrow(() -> new ResourceNotFoundException("Destination not found for id=" + destinationId));

        return DestinationConverter.modelToDto(destination);
    }

    public Long addDestination(DestinationDto destinationDto) throws CannotCreateResourceException {

        validateDestinationDtoForCreate(destinationDto);

//        return destinationRepository.save(dtoToModel(destinationDto)).getId();

        Destination destination = dtoToModel(destinationDto);
        Destination savedDestination = destinationRepository.save(destination);
        destinationCache.updateDestination(savedDestination);

        return destination.getId();
    }

    @CachePut(value = "destinations", key = "#destinationDto.id")
    public DestinationDto updateDestination(DestinationDto destinationDto) throws CannotCreateResourceException {

        validateDestinationDtoForUpdate(destinationDto);

        Destination destination = dtoToModel(destinationDto);
        Destination savedDestination = destinationRepository.save(destination);
        destinationCache.updateDestination(savedDestination);
        return DestinationConverter.modelToDto(savedDestination);
    }

    @CacheEvict("destinations")
    public void deleteDestinationById(Long destinationId) throws ResourceNotFoundException {

        Destination destination = destinationCache.findById(destinationId)
                .orElseThrow(() -> new ResourceNotFoundException("Destination not found for id=" + destinationId));

        orderRepository.findAllByDestination(destination)
                .forEach(orderRepository::removeDestinationFromOrder);

        destinationRepository.deleteById(destinationId);
        destinationCache.deleteDestination(destinationId);
    }

    private void validateDestinationDtoForCreate(DestinationDto destinationDto) throws CannotCreateResourceException {
        if (destinationDto.getId() != null) {
            throw new CannotCreateResourceException("Id must be null!");
        }
        Optional<Destination> foundDestination = destinationCache.findByName(destinationDto.getName());
        if (foundDestination.isPresent()) {
            throw new CannotCreateResourceException("Destination name already exists!");
        }
    }

    private void validateDestinationDtoForUpdate(DestinationDto destinationDto) throws CannotCreateResourceException {

        if (destinationDto.getId() == null) {
            throw new CannotCreateResourceException("Id must be provided!");
        }

        if (!destinationCache.existsById(destinationDto.getId())) {
            throw new CannotCreateResourceException("Id for update does not exist!");
        }

        Optional<Destination> optionalDestination = destinationCache.findByName(destinationDto.getName());
        if (optionalDestination.isPresent()) {
            Destination destinationWithNameFromDto = optionalDestination.get();
            if (!destinationWithNameFromDto.getId().equals(destinationDto.getId())) {
                throw new CannotCreateResourceException(String.format("Name=%s is already present in destination with id=%d",
                        destinationDto.getName(), destinationWithNameFromDto.getId()));
            }
        }
    }
}
