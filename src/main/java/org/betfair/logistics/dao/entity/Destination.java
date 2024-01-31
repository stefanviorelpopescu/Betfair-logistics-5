package org.betfair.logistics.dao.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "destinations")
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private Integer distance;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "destination")
    List<Order> orders;

}
