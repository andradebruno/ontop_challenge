package com.ontop.wallet.adapters.out.client.paymentProvider;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ontop.wallet.adapters.in.resource.exception.ServiceApiException;
import com.ontop.wallet.adapters.out.client.paymerProvider.PaymentProviderRestAdapter;
import com.ontop.wallet.application.core.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class PaymentProviderRestAdapterTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PaymentProviderRestAdapter paymentProviderRestAdapter;
    private MockRestServiceServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @Test
    public void testCreatePaymentTransaction_Success() throws Exception {
        ProviderTransactionRequest request = createRequest();
        ProviderTransactionResponse response = createValidResponse();

        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://mockoon.tools.getontop.com:3000/api/v1/payments")))
                .andExpect(method(HttpMethod.POST)).andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(response))
                );

        ProviderTransactionResponse providerTransactionResponse = paymentProviderRestAdapter.createPaymentTransaction(request);
        mockServer.verify();
        assertEquals(TransactionStatus.PROCESSING.getStatus(), providerTransactionResponse.getRequestInfo().getStatus());
    }

    @Test
    public void testCreatePaymentTransaction_ServerError() throws URISyntaxException, JsonProcessingException {
        ProviderTransactionRequest request = createRequest();
        ProviderTransactionResponse response = createValidResponse();

        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://mockoon.tools.getontop.com:3000/api/v1/payments")))
                .andExpect(method(HttpMethod.POST)).andRespond(withServerError()
                        .contentType(MediaType.APPLICATION_JSON)
                );

        ServiceApiException thrown = assertThrows(ServiceApiException.class, () -> {
            paymentProviderRestAdapter.createPaymentTransaction(request);
        });

        assertTrue(thrown.getHttpStatus().is5xxServerError());
        mockServer.verify();

    }

    @Test
    public void testCreatePaymentTransaction_BadRequest() throws URISyntaxException, JsonProcessingException {
        ProviderTransactionRequest request = createRequest();
        ProviderTransactionResponse response = createValidResponse();

        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://mockoon.tools.getontop.com:3000/api/v1/payments")))
                .andExpect(method(HttpMethod.POST)).andRespond(withBadRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                );

        ServiceApiException thrown = assertThrows(ServiceApiException.class, () -> {
            paymentProviderRestAdapter.createPaymentTransaction(request);
        });

        assertTrue(thrown.getHttpStatus().is4xxClientError());
        mockServer.verify();
    }

    private ProviderTransactionRequest createRequest() {
        return ProviderTransactionRequest.builder()
                .userId(1L).name("TONY STARK").amount(BigDecimal.TEN).bankAccount(BankAccount.builder()
                        .currency(Currency.getInstance("USD"))
                        .accountNumber("12345678")
                        .bankAccountId(1L)
                        .routingNumber("987654321")
                        .bankName("Test").build())
                .build();
    }

    private ProviderTransactionResponse createValidResponse() {

        return ProviderTransactionResponse.builder()
                .paymentInfo(PaymentInfo.builder()
                        .providerTransactionId("c2ac97d8-ba48-4864-9864-b13dd9640a43")
                        .amount(BigDecimal.TEN).build())
                .requestInfo(RequestInfo.builder().status("Processing").build()).build();
    }

    private String toJsonString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting object to JSON", e);
        }
    }

}