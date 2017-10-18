package junit.org.rapidpm.vaadin.srv;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.rapidpm.vaadin.srv.Customer;
import org.rapidpm.vaadin.srv.CustomerService;

/**
 *
 */
public class CustomerServiceTest {


  @Test
  void test001() {

    final CustomerService instance = CustomerService.getInstance();
    final List<Customer> all = instance.findAll();

    //how to test this ?


  }
}
