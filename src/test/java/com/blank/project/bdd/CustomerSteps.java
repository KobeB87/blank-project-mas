package com.blank.project.bdd;

import com.blank.project.entity.Customer;
import com.blank.project.model.CustomerInfo;
import com.blank.project.repository.CustomerRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Cucumber step definition.
 */
@Slf4j
public class CustomerSteps extends CucumberBootstrap {

    @Autowired
    private CustomerRepository customerRepository;

    @After
    public void cleanUp() {
        log.info(">>> cleaning up after scenario!");
        customerRepository.deleteAll();
    }

    @AfterStep
    public void afterStep() {
        log.info(">>> AfterStep!");
    }

    @Before
    public void before() {
        log.info(">>> Before scenario!");
    }

    @BeforeStep
    public void beforeStep() {
        log.info(">>> BeforeStep!");
    }

    @Given("^the collection of customers:$")
    public void collection_of_customers(final DataTable dataTable) {
        dataTable.asList(CustomerInfo.class).forEach(customerInfo -> {
            saveCustomer((CustomerInfo)customerInfo);
        });
    }

    @When("^customerId (.+) is passed in to retrieve the customer details$")
    public void get_customer_details_by_id(final String customerId) {
        final ResponseEntity<CustomerInfo> response = testRestTemplate.getForEntity(
                "/customers/" + customerId, CustomerInfo.class, customerId);

        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody().getCustomerId(), is(equalTo(customerId)));
    }

    @Then("^The customer detail is retrieved$")
    public void customer_detail_retrieved(final DataTable dataTable) {
        dataTable.asList(CustomerInfo.class).forEach(customerInfo -> {
            final Optional<Customer> customerOptional =
                    this.customerRepository.findByCustomerId(((CustomerInfo)customerInfo).getCustomerId());
            if (customerOptional.isPresent()){
                assertThat(customerOptional.get().getFirstName(), is(equalTo(((CustomerInfo)customerInfo).getFirstName())));
                assertThat(customerOptional.get().getLastName(), is(equalTo(((CustomerInfo)customerInfo).getLastName())));
            }
        });
    }

    private void saveCustomer(final CustomerInfo customerInfo) {
        this.customerRepository.save(Customer.builder()
                .customerId(customerInfo.getCustomerId())
                .firstName(customerInfo.getFirstName())
                .lastName(customerInfo.getLastName())
                .build());
    }
}
