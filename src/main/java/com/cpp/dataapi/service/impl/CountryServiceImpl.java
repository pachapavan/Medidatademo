package com.cpp.dataapi.service.impl;

import com.cpp.dataapi.domain.Country;
import com.cpp.dataapi.repository.CountryRepository;
import com.cpp.dataapi.service.CountryService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Country}.
 */
@Service
public class CountryServiceImpl implements CountryService {

    private final Logger log = LoggerFactory.getLogger(CountryServiceImpl.class);

    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public Country save(Country country) {
        log.debug("Request to save Country : {}", country);
        return countryRepository.save(country);
    }

    @Override
    public Optional<Country> partialUpdate(Country country) {
        log.debug("Request to partially update Country : {}", country);

        return countryRepository
            .findById(country.getId())
            .map(existingCountry -> {
                if (country.getCountryName() != null) {
                    existingCountry.setCountryName(country.getCountryName());
                }

                return existingCountry;
            })
            .map(countryRepository::save);
    }

    @Override
    public List<Country> findAll() {
        log.debug("Request to get all Countries");
        return countryRepository.findAll();
    }

    @Override
    public Optional<Country> findOne(String id) {
        log.debug("Request to get Country : {}", id);
        return countryRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Country : {}", id);
        countryRepository.deleteById(id);
    }
}
