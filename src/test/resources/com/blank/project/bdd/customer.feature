# User feature file

# Feature: describe the user story and global expected behaviors
Feature: Customer functionalities
  This feature contains a list of functionalities related to customer

  Scenario: get a customer detail by customerId
  # Scenario: clear title from feature's expected behavior to identify a test scenario (including non passing cases)

    Given the collection of customers:
    # Given : describe the initial context
      | customerId                       | firstName   | lastName  |
      | ABCDEFG12345678910HIJKLMNOP12345 | John        | Wick      |
      | ABCDEFG12345678910HIJKLMNOP12346 | June        | Wick      |

    When customerId ABCDEFG12345678910HIJKLMNOP12345 is passed in to retrieve the customer details
    # When : describe the action done by user from initial context

    Then The customer detail is retrieved
    # Then : describe the expected behavior based on action done by user
      | customerId                       | firstName   | lastName  |
      | ABCDEFG12345678910HIJKLMNOP12345 | John        | Wick      |
      | ABCDEFG12345678910HIJKLMNOP12346 | June        | Wick      |

    # And : Add a condition to Given, When ou Then clause
    # But : Exclude a condition to Given, When ou Then clause