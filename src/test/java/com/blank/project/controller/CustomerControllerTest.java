package com.blank.project.controller;

import com.blank.project.entity.Customer;
import com.blank.project.model.CustomerInfo;
import com.blank.project.service.CustomerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {
    private static final String CUSTOMER_ID = "ABCDEFG12345678910HIJKLMNOP12345";

    private static final String FIRST_NAME = "FIRST";

    private static final String LAST_NAME = "LAST";

    private static final CustomerInfo API_CUSTOMER = CustomerInfo.builder()
            .firstName(FIRST_NAME)
            .lastName(LAST_NAME)
            .build();

    private static final CustomerInfo CUSTOMER = CustomerInfo.builder()
            .customerId(CUSTOMER_ID)
            .firstName(FIRST_NAME)
            .lastName(LAST_NAME)
            .build();

    private static final String PATH = "/customers/{customerId}";

    private static final UriComponentsBuilder URI_COMPONENTS_BUILDER = UriComponentsBuilder.fromPath(PATH);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private UriComponentsBuilder uriBuilder;

    @BeforeEach
    public void setUp() {

    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void createCustomer() throws Exception {
        given(this.customerService.saveCustomer(API_CUSTOMER)).willReturn(CUSTOMER);
        given(this.uriBuilder.path(PATH)).willReturn(URI_COMPONENTS_BUILDER);

        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/customers")
                        .content(asJsonString(API_CUSTOMER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value(CUSTOMER.getCustomerId()))
                .andExpect(jsonPath("$.firstName").value(CUSTOMER.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(CUSTOMER.getLastName()));
    }

    @Test
    public void getCustomers() throws Exception {
        final Pageable pageable = PageRequest.of(0, 1);
        final Page<CustomerInfo> pageCustomerInfo = new PageImpl<>(Collections.singletonList(API_CUSTOMER));
        given(this.customerService.getCustomers(pageable)).willReturn(pageCustomerInfo);

        final MvcResult mvcResult = this.mockMvc.perform(
                MockMvcRequestBuilders.get("/customers")
                        .queryParam("pageNumber", "0")
                        .queryParam("pageSize", "1")

        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.size").exists())
                .andExpect(jsonPath("$.data.size", is(1)))
                .andExpect(jsonPath("$.data.number").exists())
                .andExpect(jsonPath("$.data.number", is(0)))
                .andExpect(jsonPath("$.data.totalElements").exists())
                .andExpect(jsonPath("$.data.totalElements", is(1)))
                .andExpect(jsonPath("$.data.totalPages").exists())
                .andExpect(jsonPath("$.data.totalPages", is(1)))
                .andExpect(jsonPath("$.data.content").exists())
                .andExpect(jsonPath("$.data.content[0]").exists())
                .andExpect(jsonPath("$.data.content[0].firstName").exists())
                .andExpect(jsonPath("$.data.content[0].firstName", is(API_CUSTOMER.getFirstName())))
                .andExpect(jsonPath("$.data.content[0].lastName").exists())
                .andExpect(jsonPath("$.data.content[0].lastName", is(API_CUSTOMER.getLastName())))
                .andReturn()
                ;
    }

    @Test
    public void getCustomer() throws Exception {
        given(this.customerService.getCustomer(CUSTOMER_ID)).willReturn(CUSTOMER);

        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/customers/{customerId}", CUSTOMER_ID)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(CUSTOMER.getCustomerId()))
                .andExpect(jsonPath("$.firstName").value(CUSTOMER.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(CUSTOMER.getLastName()))
        ;
    }

    @Test
    public void updateCustomer() throws Exception {
        final CustomerInfo customerInfoNew = CustomerInfo.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .build();
        given(this.customerService.updateCustomer(CUSTOMER_ID, customerInfoNew)).willReturn(CUSTOMER);

        this.mockMvc.perform(
                MockMvcRequestBuilders.put("/customers/{customerId}", CUSTOMER_ID)
                        .content(asJsonString(customerInfoNew))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNoContent())
        ;
    }

    @Test
    public void deleteCustomer() throws Exception {
        doNothing().when(this.customerService).deleteCustomer(CUSTOMER_ID);

        this.mockMvc.perform(
                MockMvcRequestBuilders.delete("/customers/{customerId}", CUSTOMER_ID)
        )
                .andExpect(status().isNoContent())
        ;
    }
}