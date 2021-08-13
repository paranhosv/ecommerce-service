package com.victorparanhos.ecommerceservice.infra.repositories;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.UnavailableDataException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;

import static com.victorparanhos.ecommerceservice.utils.JsonFileProductGenerator.jsonFileProductsToList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@SpringBootTest
public class JsonFileProductRepositoryTests {

    @Autowired
    private JsonFileProductRepository repository;

    @Mock
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void findAllShouldReturnAllProductsFromJson() throws UnavailableDataException {
        var findAllResult = repository.findAll();
        assertThat(findAllResult)
                .usingRecursiveComparison()
                .isEqualTo(jsonFileProductsToList());
    }

    @Test
    public void findAllShouldThrowUnavailableDataExceptionWhenMappingThrowsIOException() throws IOException {
        given(mapper.readValue(any(InputStream.class), any(JavaType.class)))
                .willThrow(new IOException("Test"));
        ReflectionTestUtils.setField(repository, "mapper", mapper);


        assertThatThrownBy(() ->
            repository.findAll()
        ).isInstanceOf(UnavailableDataException.class);
        then(mapper)
                .should(times(1))
                .readValue(any(InputStream.class), any(JavaType.class));
    }
}
