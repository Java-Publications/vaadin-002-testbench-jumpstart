package junit.org.rapidpm.vaadin.srv;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.rapidpm.vaadin.shared.Customer;
import org.rapidpm.vaadin.srv.CustomerService;
import org.rapidpm.vaadin.srv.CustomerServiceImpl;

/**
 *
 */
public class CustomerServiceTest {


  @Test
  void test001() {

    final CustomerService instance = CustomerServiceImpl.getInstance();
    final List<Customer> all = instance.findAll();

    Assert.assertEquals(30, all.size());
    //how to test this ?

  }


  @Test
  void test002() {

  }
}
