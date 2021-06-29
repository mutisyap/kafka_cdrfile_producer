package io.pmutisya.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class MapSerializerUtil {
    public static String serializeMapToString(Map<String, String> map) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(map);
    }

    public static Map<String, String> deserializeMapFromJSONString(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        TypeReference<Map<String, String>> typeReference
                = new TypeReference<Map<String, String>>() {
        };

        return objectMapper.readValue(jsonString, typeReference);
    }

}
