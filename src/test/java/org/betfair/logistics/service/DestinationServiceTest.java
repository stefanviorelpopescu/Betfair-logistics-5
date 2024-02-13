package org.betfair.logistics.service;

import org.betfair.logistics.cache.DestinationCache;
import org.betfair.logistics.dao.dto.DestinationDto;
import org.betfair.logistics.dao.entity.Destination;
import org.betfair.logistics.dao.repository.DestinationRepository;
import org.betfair.logistics.dao.repository.OrderRepository;
import org.betfair.logistics.exception.ResourceNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest(classes = {DestinationRepository.class, DestinationCache.class, OrderRepository.class, OrderService.class})
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DestinationServiceTest {

//    @MockBean
//    OrderRepository orderRepository;

    @Autowired
    DestinationRepository destinationRepository;

    @Autowired
    DestinationService destinationService;

    @Order(1)
    @Test
    public void testDeleteById_NotFound() {
        //given
        Long idToDelete = destinationService.getAllDestinations().stream()
                .mapToLong(DestinationDto::getId)
                .max()
                .orElse(0) + 1;

        //when
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
                () -> destinationService.deleteDestinationById(idToDelete));

        //then
        assertEquals("Destination not found for id=" + idToDelete, resourceNotFoundException.getMessage());
    }

    @Order(2)
    @Test
    public void testDeleteById_Ok() throws ResourceNotFoundException {
        //given
        Long idToDelete = destinationService.getAllDestinations().stream()
                .mapToLong(DestinationDto::getId)
                .findAny()
                .orElseThrow();
        Optional<Destination> foundDestination = destinationRepository.findById(idToDelete);
        assertTrue(foundDestination.isPresent());

        //when
        destinationService.deleteDestinationById(idToDelete);

        //then
        foundDestination = destinationRepository.findById(idToDelete);
        assertTrue(foundDestination.isEmpty());
    }

}