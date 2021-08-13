package com.victorparanhos.ecommerceservice.infra.repositories;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.UnavailableDataException;
import com.victorparanhos.ecommerceservice.infra.entities.JsonFileProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class JsonFileProductRepository implements ProductRepository {
    private static final Logger logger = LoggerFactory.getLogger(JsonFileProductRepository.class);

    private final ObjectMapper mapper = new ObjectMapper();
    private final JavaType listType = mapper.getTypeFactory()
            .constructCollectionType(List.class, JsonFileProduct.class);

    @Override
    public List<JsonFileProduct> findAll() throws UnavailableDataException {
        try {
            InputStream is = new ClassPathResource("data/products.json").getInputStream();
            return mapper.readValue(is, listType);
        } catch (IOException e) {
            String errorMessage =  "Error reading json file";
            logger.error(errorMessage, e);
            throw new UnavailableDataException(errorMessage);
        }
    }
}
