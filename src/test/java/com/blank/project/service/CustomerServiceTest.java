package com.blank.project.service;

import com.blank.project.entity.Customer;
import com.blank.project.exception.NotFoundException;
import com.blank.project.model.CustomerInfo;
import com.blank.project.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class CustomerServiceTest {

    private static final String CUSTOMER_ID = "ABCDEFG12345678910HIJKLMNOP12345";
    private static final String FIRST_NAME = "FIRST";
    private static final String LAST_NAME = "LAST";

    private static final CustomerInfo API_CUSTOMER = CustomerInfo.builder()
            .customerId(CUSTOMER_ID)
            .firstName(FIRST_NAME)
            .lastName(LAST_NAME)
            .build();

    private static final CustomerInfo API_CUSTOMER_NULL_ID = CustomerInfo.builder()
            .firstName(FIRST_NAME)
            .lastName(LAST_NAME)
            .build();

    private static final Customer CUSTOMER = Customer.builder()
            .customerId(CUSTOMER_ID)
            .firstName(FIRST_NAME)
            .lastName(LAST_NAME)
            .build();

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        //this.customerRepository = this.context.mock(CustomerRepository.class);
    }

    @Test
    public void saveCustomer() throws Exception {
        given(this.customerRepository.save(any(Customer.class))).willReturn(CUSTOMER);

        final CustomerInfo customerInfo = this.customerService.saveCustomer(API_CUSTOMER);

        verify(this.customerRepository).save(any(Customer.class));

        assertThat(customerInfo.getCustomerId(), is(equalTo(API_CUSTOMER.getCustomerId())));
        assertThat(customerInfo.getFirstName(), is(equalTo(API_CUSTOMER.getFirstName())));
        assertThat(customerInfo.getLastName(), is(equalTo(API_CUSTOMER.getLastName())));
    }

    @Test
    public void getCustomers() {
        final Pageable pageable = PageRequest.of(0, 1);
        final Page<Customer> customerInfoPage = new PageImpl<Customer>(Arrays.asList(CUSTOMER), pageable, 1);

        given(this.customerRepository.findAll(any(Pageable.class))).willReturn(customerInfoPage);

        final Page<CustomerInfo> pageCustomerInfo = this.customerService.getCustomers(pageable);

        verify(this.customerRepository).findAll(eq(pageable));
        assertThat(pageCustomerInfo.getTotalElements(), equalTo(1L));
    }

    @Test
    public void getCustomer() {
        given(this.customerRepository.findByCustomerId(CUSTOMER_ID)).willReturn(Optional.of(CUSTOMER));

        final CustomerInfo customerInfo = this.customerService.getCustomer(CUSTOMER_ID);

        verify(this.customerRepository).findByCustomerId(eq(CUSTOMER_ID));

        assertThat(customerInfo.getCustomerId(), is(equalTo(CUSTOMER.getCustomerId())));
        assertThat(customerInfo.getFirstName(), is(equalTo(CUSTOMER.getFirstName())));
        assertThat(customerInfo.getLastName(), is(equalTo(CUSTOMER.getLastName())));
    }

    @Test
    public void getCustomerReturnsEmptyResults() {
        given(this.customerRepository.findByCustomerId("test")).willReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> {
            this.customerService.getCustomer("test");
            verify(this.customerRepository).findByCustomerId(eq("test"));
        });
    }

    @Test
    public void updateCustomer() throws Exception {
        given(this.customerRepository.findByCustomerId(CUSTOMER_ID)).willReturn(Optional.of(CUSTOMER));
        given(this.customerRepository.save(any(Customer.class))).willReturn(CUSTOMER);

        final CustomerInfo customerInfo = this.customerService.updateCustomer(CUSTOMER_ID, API_CUSTOMER);

        verify(this.customerRepository).findByCustomerId(eq(CUSTOMER_ID));
        verify(this.customerRepository).save(any(Customer.class));

        assertThat(customerInfo.getCustomerId(), is(equalTo(CUSTOMER.getCustomerId())));
        assertThat(customerInfo.getFirstName(), is(equalTo(API_CUSTOMER.getFirstName())));
        assertThat(customerInfo.getLastName(), is(equalTo(API_CUSTOMER.getLastName())));
    }

    @Test
    public void updateCustomerReturnsEmptyResults() {
        given(this.customerRepository.findByCustomerId("test")).willReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> {
            this.customerService.updateCustomer("test", API_CUSTOMER);

            verify(this.customerRepository).findByCustomerId(eq("test"));
            verify(this.customerRepository, never()).save(any(Customer.class));

        });
    }

    @Test
    public void deleteCustomer() throws Exception {
        given(this.customerRepository.findByCustomerId(CUSTOMER_ID)).willReturn(Optional.of(CUSTOMER));
        doNothing().when(this.customerRepository).delete(any(Customer.class));

        this.customerService.deleteCustomer(CUSTOMER_ID);

        verify(this.customerRepository).findByCustomerId(eq(CUSTOMER_ID));
        verify(this.customerRepository).delete(any(Customer.class));
    }

    @Test
    public void deleteCustomerReturnsEmptyResults() {
        when(this.customerRepository.findByCustomerId("test")).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> {
            this.customerService.deleteCustomer("test");

            verify(this.customerRepository).findByCustomerId(eq("test"));
            verify(this.customerRepository, never()).delete(any(Customer.class));
        });
    }
}