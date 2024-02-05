package org.betfair.logistics.service;

import lombok.RequiredArgsConstructor;
import org.betfair.logistics.dao.OrderStatus;
import org.betfair.logistics.dao.converter.DestinationConverter;
import org.betfair.logistics.dao.dto.DestinationDto;
import org.betfair.logistics.dao.entity.Destination;
import org.betfair.logistics.dao.entity.Order;
import org.betfair.logistics.dao.repository.DestinationRepository;
import org.betfair.logistics.dao.repository.OrderRepository;
import org.betfair.logistics.exception.CannotCreateResourceException;
import org.betfair.logistics.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.betfair.logistics.dao.converter.DestinationConverter.dtoToModel;

@Service
@RequiredArgsConstructor
public class DestinationService {

    private final DestinationRepository destinationRepository;
    private final OrderRepository orderRepository;

    public List<DestinationDto> getAllDestinations() {
        List<Destination> destinationModels = destinationRepository.findAll();
        return DestinationConverter.modelListToDtoList(destinationModels);
    }

    public DestinationDto getDestinationById(Long destinationId) throws ResourceNotFoundException {

        Destination destination = destinationRepository.findById(destinationId)
                .orElseThrow(() -> new ResourceNotFoundException("Destination not found for id=" + destinationId));

        return DestinationConverter.modelToDto(destination);
    }

    public Long addDestination(DestinationDto destinationDto) throws CannotCreateResourceException {

        validateDestinationDtoForCreate(destinationDto);

//        return destinationRepository.save(dtoToModel(destinationDto)).getId();

        Destination destination = dtoToModel(destinationDto);
        destinationRepository.save(destination);

        return destination.getId();
    }

    public void updateDestination(DestinationDto destinationDto) throws CannotCreateResourceException {

        validateDestinationDtoForUpdate(destinationDto);

        Destination destination = dtoToModel(destinationDto);
        destinationRepository.save(destination);
    }

    public void deleteDestinationById(Long destinationId) throws ResourceNotFoundException {

        Destination destination = destinationRepository.findById(destinationId)
                .orElseThrow(() -> new ResourceNotFoundException("Destination not found for id=" + destinationId));

        orderRepository.findAllByDestination(destination)
                .forEach(this::removeDestinationFromOrder);

        destinationRepository.deleteById(destinationId);
    }

    private void removeDestinationFromOrder(Order order) {
        order.setDestination(null);
        order.setStatus(OrderStatus.INVALID);
        orderRepository.save(order);
    }

    private void validateDestinationDtoForCreate(DestinationDto destinationDto) throws CannotCreateResourceException {
        if (destinationDto.getId() != null) {
            throw new CannotCreateResourceException("Id must be null!");
        }
        Optional<Destination> foundDestination = destinationRepository.findByName(destinationDto.getName());
        if (foundDestination.isPresent()) {
            throw new CannotCreateResourceException("Destination name already exists!");
        }
    }

    private void validateDestinationDtoForUpdate(DestinationDto destinationDto) throws CannotCreateResourceException {

        if (destinationDto.getId() == null) {
            throw new CannotCreateResourceException("Id must be provided!");
        }

        if (!destinationRepository.existsById(destinationDto.getId())) {
            throw new CannotCreateResourceException("Id for update does not exist!");
        }

        Optional<Destination> optionalDestination = destinationRepository.findByName(destinationDto.getName());
        if (optionalDestination.isPresent()) {
            Destination destinationWithNameFromDto = optionalDestination.get();
            if (!destinationWithNameFromDto.getId().equals(destinationDto.getId())) {
                throw new CannotCreateResourceException(String.format("Name=%s is already present in destination with id=%d",
                        destinationDto.getName(), destinationWithNameFromDto.getId()));
            }
        }
    }
}
