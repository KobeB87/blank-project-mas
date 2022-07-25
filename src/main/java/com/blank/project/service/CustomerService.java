package com.blank.project.service;

import com.blank.project.entity.Customer;
import com.blank.project.exception.NotFoundException;
import com.blank.project.model.CustomerInfo;
import com.blank.project.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(Transactional.TxType.REQUIRED)
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerInfo saveCustomer(final CustomerInfo customerInfo) throws Exception {
        customerInfo.setCustomerId(
                Strings.isBlank(customerInfo.getCustomerId()) ?
                        UUID.randomUUID().toString() : customerInfo.getCustomerId());

        final Customer customer = Customer.builder()
                .customerId(customerInfo.getCustomerId())
                .firstName(customerInfo.getFirstName())
                .lastName(customerInfo.getLastName())
                .build();

        this.customerRepository.save(customer);

        return customerInfo;
    }

    public Page<CustomerInfo> getCustomers(final Pageable pageable) {
        final Page<Customer> customers = this.customerRepository.findAll(pageable);

        final List<CustomerInfo> customerInfos = customers.stream()
                .map(customer -> CustomerInfo.builder()
                        .customerId(customer.getCustomerId())
                        .firstName(customer.getFirstName())
                        .lastName(customer.getLastName())
                        .build())
                .collect(toList());

        return new PageImpl<>(customerInfos, pageable, customers.getTotalElements());
    }

    public CustomerInfo getCustomer(final String customerId) {
        final Customer customer =
                this.customerRepository.findByCustomerId(customerId).orElseThrow(() ->
                        new NotFoundException("Could not find customer with customerId: " + customerId));

        final CustomerInfo customerInfo = CustomerInfo.builder()
                .customerId(customerId)
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .build();

        return customerInfo;
    }

    public CustomerInfo updateCustomer(final String customerId, final CustomerInfo customerInfo) throws Exception {
        final Customer customer =
                this.customerRepository.findByCustomerId(customerId).orElseThrow(() ->
                        new NotFoundException("Could not find customer with customerId: " + customerId));

        customer.setFirstName(customerInfo.getFirstName());
        customer.setLastName(customerInfo.getLastName());
        final Customer customerUpdated = this.customerRepository.save(customer);

        final CustomerInfo customerInfoReturn = CustomerInfo.builder()
                .customerId(customerUpdated.getCustomerId())
                .firstName(customerUpdated.getFirstName())
                .lastName(customerUpdated.getLastName())
                .build();

        return customerInfoReturn;
    }

    public void deleteCustomer(final String customerId) throws Exception {
        final Customer customer =
                this.customerRepository.findByCustomerId(customerId).orElseThrow(() ->
                        new NotFoundException("Could not find customer with customerId: " + customerId));
        this.customerRepository.delete(customer);
    }
}
