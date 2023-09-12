package org.example.webCommunication.utils;

import org.example.model.Sheet;
import org.example.model.SheetsResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapperTest {

    Mapper mapper = new Mapper();

    @Test
    void convertsObjectToJsonCorrectly() {
        SheetsResponse sheetsResponse = new SheetsResponse("some link", List.of(
                new Sheet("sheet-1", List.of(List.of("Hello")))
        ));
        assertEquals(mapper.toJson(sheetsResponse), "{\"submissionUrl\":\"some link\",\"sheets\":[{\"id\":\"sheet-1\",\"data\":[[\"Hello\"]]}]}");
    }

}