package com.ontop.wallet.adapters.out.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

public abstract class RestClient {
    protected RestTemplate restTemplate;

    protected ObjectMapper objectMapper;

    public RestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        objectMapper = new ObjectMapper();
    }

    protected ResponseEntity<String> get(String uri) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        return restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
    }

    protected ResponseEntity<String> post(String uri, String requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        return restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
    }
}
