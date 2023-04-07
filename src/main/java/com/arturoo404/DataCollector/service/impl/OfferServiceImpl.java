package com.arturoo404.DataCollector.service.impl;

import com.arturoo404.DataCollector.model.Offer;
import com.arturoo404.DataCollector.repository.OfferRepository;
import com.arturoo404.DataCollector.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    @Override
    public void saveOffer(Offer offer) {
        if (offer != null){
            offerRepository.save(offer);
        }
    }
}
