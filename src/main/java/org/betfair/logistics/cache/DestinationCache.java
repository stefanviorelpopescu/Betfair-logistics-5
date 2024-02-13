package org.betfair.logistics.cache;

import lombok.RequiredArgsConstructor;
import org.betfair.logistics.dao.entity.Destination;
import org.betfair.logistics.dao.repository.DestinationRepository;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DestinationCache {

    private final DestinationRepository destinationRepository;

    private Map<Long, Destination> destinationsById;

    private void populate() {
        destinationsById = destinationRepository.findAll().stream()
                .collect((Collectors.toMap(Destination::getId, Function.identity())));
    }

    private Map<Long, Destination> getCacheData() {
        if (this.destinationsById == null) {
            populate();
        }
        return this.destinationsById;
    }

    public Optional<Destination> findById(Long destinationId) {
        return Optional.ofNullable(getCacheData().get(destinationId));
    }

    public List<Destination> findAll() {
        return new ArrayList<>(getCacheData().values());
    }

    public boolean existsById(Long destinationId) {
        return getCacheData().containsKey(destinationId);
    }

    public Optional<Destination> findByName(String destinationName) {
        return getCacheData().values().stream()
                .filter(destination -> destination.getName().equals(destinationName))
                .findAny();
    }

    public void updateDestination(Destination destination) {
        getCacheData().put(destination.getId(), destination);
    }

    public void deleteDestination(Long destinationId) {
        getCacheData().remove(destinationId);
    }

}
