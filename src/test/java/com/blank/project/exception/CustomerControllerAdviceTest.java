package com.blank.project.exception;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;

public class CustomerControllerAdviceTest {
    private static final String UNIQUE_IDENTIFIER = "UNIQUE_IDENTIFIER";
    private static final String DESCRIPTION = "DESCRIPTION";

    @Test
    public void handleHttpRequestMethodNotSupportedException() {
        final String description = "Unexpected system exception ID: " + UNIQUE_IDENTIFIER;
        final HttpRequestMethodNotSupportedException exception = new HttpRequestMethodNotSupportedException(description);
        final CustomerControllerAdvice advice = new CustomerControllerAdvice();

        final ResponseEntity responseEntity = advice.handleHttpRequestMethodNotSupportedException(exception);

        MatcherAssert.assertThat(responseEntity.getStatusCode(), Matchers.is(Matchers.equalTo(HttpStatus.BAD_REQUEST)));
    }

    @Test
    public void handleRuntimeException() {
        final String description = "Unexpected system exception ID: " + UNIQUE_IDENTIFIER;
        final RuntimeException exception = new RuntimeException(description);
        final CustomerControllerAdvice advice = new CustomerControllerAdvice();

        final ResponseEntity responseEntity = advice.handleRuntimeException(exception);

        MatcherAssert.assertThat(responseEntity.getStatusCode(), Matchers.is(Matchers.equalTo(HttpStatus.INTERNAL_SERVER_ERROR)));
    }

    @Test
    public void handleNotFoundException() {
        final NotFoundException exception = new NotFoundException(DESCRIPTION);
        final CustomerControllerAdvice advice = new CustomerControllerAdvice();

        final ResponseEntity responseEntity = advice.handleNotFoundException(exception);

        MatcherAssert.assertThat(responseEntity.getStatusCode(), Matchers.is(Matchers.equalTo(HttpStatus.NOT_FOUND)));
    }
}