package org.rapidpm.vaadin.srv;

import java.util.List;

import org.rapidpm.vaadin.shared.Customer;

/**
 *
 */
public interface CustomerService {
  List<Customer> findAll();

  List<Customer> findAll(String stringFilter);

  List<Customer> findAll(String stringFilter , int start , int maxresults);

  long count();

  void delete(Customer value);

  void save(Customer entry);
}
