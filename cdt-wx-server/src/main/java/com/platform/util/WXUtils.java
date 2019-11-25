package com.platform.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class WXUtils {

    @Autowired
    private static RestTemplate restTemplate;

    public static String getAccessToken() {


        return null;
    }

    public static void main(String[] args) {
        getAccessToken();

    }

}
