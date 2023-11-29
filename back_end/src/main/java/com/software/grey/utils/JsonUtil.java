package com.software.grey.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    private JsonUtil(){}
    public static String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
