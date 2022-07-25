package com.blank.project.repository;

import com.blank.project.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CustomerRepository extends JpaRepository<Customer, String> {

    Optional<Customer> findByCustomerId(final String customerId);
}
