package com.blank.project.entity;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class CustomerTest {

    private static final String CUSTOMER_ID = "ABCDEFG12345678910HIJKLMNOP12345";
    private static final String FIRST_NAME = "FIRST_NAME";
    private static final String LAST_NAME = "LAST_NAME";

    @Test
    public void objectCreation() {
        final Customer customer = Customer.builder()
                .customerId(CUSTOMER_ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .build();
        MatcherAssert.assertThat(customer.getCustomerId(), Matchers.is(Matchers.sameInstance(CUSTOMER_ID)));
        MatcherAssert.assertThat(customer.getFirstName(), Matchers.is(Matchers.sameInstance(FIRST_NAME)));
        MatcherAssert.assertThat(customer.getLastName(), Matchers.is(Matchers.sameInstance(LAST_NAME)));
    }
}