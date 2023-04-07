package com.arturoo404.DataCollector.model;

import lombok.*;

import javax.persistence.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "offer")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "from_offer")
    private String from;

    @Column(name = "location")
    private String location;

    @Column(name = "link")
    private String link;

    @Column(name = "price")
    private String price;

    @Column(name = "area")
    private String area;

    @Column(name = "number_of_rooms")
    private String numberOfRooms;

    @Column(name = "price_per_square_meter")
    private String pricePerSquareMeter;

    @Column(name = "ad_phone")
    private String adPhone;
}
