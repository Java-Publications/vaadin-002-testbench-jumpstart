package junit.org.rapidpm.vaadin.ui.components;

import static java.lang.System.out;
import static org.rapidpm.frp.matcher.Case.match;
import static org.rapidpm.frp.matcher.Case.matchCase;
import static org.rapidpm.frp.model.Result.failure;
import static org.rapidpm.frp.model.Result.success;

import java.time.LocalDate;

import org.rapidpm.vaadin.shared.Customer;
import org.rapidpm.vaadin.shared.CustomerStatus;
import org.rapidpm.vaadin.ui.components.CustomerForm;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToLongConverter;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 *
 */
public class TestUI extends UI {

  public static final String TEST_SWITCH_BUTTON = "testSwitchButton";
  public static final String REGISTER_BUTTON = "testRegisterButton";
  public static final String FIRST_NAME = "firstName";
  public static final String LAST_NAME = "lastName";
  public static final String EMAIL = "email";
  public static final String BIRTHDAY = "birthday";
  public static final String CUSTOMER_FORM = "customerForm";
  public static final String ID = "customerID";


  private Customer fromLastEvent;
  private CustomerForm.Registration deleteRegistration;
  private CustomerForm.Registration saveRegistration;
  private final Button register = new Button("register");

  // attributes for testing last Event.
  private final TextField firstName = new TextField("First name");
  private final TextField lastName = new TextField("Last name");
  private final TextField email = new TextField("Email");
  private final NativeSelect<CustomerStatus> status = new NativeSelect<>("Status");
  private final DateField birthday = new DateField("Birthday");
  private final TextField id = new TextField("Customer ID");


  private final Binder<Customer> beanBinder = new Binder<>(Customer.class);

  private final CustomerForm customerForm = new CustomerForm();

  @Override
  protected void init(VaadinRequest request) {
    beanBinder
        .forField(id)
        .withConverter(new StringToLongConverter("Long Values only"))
        .bind(Customer::getId , Customer::setId);

    firstName.setId(FIRST_NAME);
    lastName.setId(LAST_NAME);
    email.setId(EMAIL);
    birthday.setId(BIRTHDAY);
    id.setId(ID);

    customerForm.setId(CUSTOMER_FORM);

    initCustomerValue();

    customerForm.setCustomer(fromLastEvent);

    status.setItems(CustomerStatus.values());

    final CustomerForm.UpdateEvent updateEvenDelete = customer -> {
      fromLastEvent = customer;
      fromLastEvent.setId(- 1L);
      beanBinder.setBean(fromLastEvent);

    };
    deleteRegistration = customerForm.registerDeleteListener(updateEvenDelete);

    final CustomerForm.UpdateEvent updateEvenSave = customer -> {
      fromLastEvent = customer;
      fromLastEvent.setId((fromLastEvent == null) ? 1L : fromLastEvent.getId() + 1);
      beanBinder.setBean(fromLastEvent);

    };
    saveRegistration = customerForm.registerSaveListener(updateEvenSave);


    //make Customer from last Event available
    final VerticalLayout testAttibutes = new VerticalLayout();
    testAttibutes.addComponents(id ,
                                firstName ,
                                lastName ,
                                email ,
                                birthday ,
                                status);
    beanBinder.bindInstanceFields(this);

    // button to make Form visible again
    final Button aSwitch = new Button("switch");
    aSwitch.setId(TEST_SWITCH_BUTTON);
    aSwitch.addClickListener((Button.ClickListener) event -> customerForm.setCustomer(fromLastEvent));


    register.setId(REGISTER_BUTTON);
    register.addClickListener(
        e ->
            match(
                matchCase(() -> failure(" an exceptional behavior...")) ,
                matchCase(() -> deleteRegistration != null &&
                                saveRegistration != null , () -> {
                  final boolean removeDelete = deleteRegistration.remove();
                  final boolean removeSave = saveRegistration.remove();

                  deleteRegistration = null;
                  saveRegistration = null;

                  return (removeDelete && removeSave)
                         ? success("Both registrations are removed")
                         : failure("Mistake removeDelete=" + removeDelete
                                        + " removeSave " + removeSave);
                }) ,
                matchCase(() -> deleteRegistration == null
                                && saveRegistration == null , () -> {

                  deleteRegistration = customerForm.registerDeleteListener(updateEvenDelete);
                  saveRegistration = customerForm.registerSaveListener(updateEvenSave);

                  return (deleteRegistration != null && saveRegistration != null)
                         ? success("Both registrations are done")
                         : failure("Mistake deleteRegistration=" + deleteRegistration
                                        + " saveRegistration " + saveRegistration);
                })
            ).ifPresentOrElse(
                success -> out.println("register Button - success = " + success) ,
                failed -> { throw new RuntimeException(failed); }
            )
    );


    final VerticalLayout layout = new VerticalLayout(customerForm , aSwitch , register , testAttibutes);

    setContent(layout);
  }

  private void initCustomerValue() {
    fromLastEvent = new Customer();
    fromLastEvent.setBirthDay(LocalDate.of(2010 , 10 , 10));
    fromLastEvent.setEmail("xx.xxx@xx.xx");
    fromLastEvent.setFirstName("Max");
    fromLastEvent.setLastName("R");
    fromLastEvent.setStatus(CustomerStatus.ImportedLead);
    fromLastEvent.setId(1L);
  }


  @Override
  public void detach() {
    if (! deleteRegistration.remove()) throw new RuntimeException("DeleteRegistration failed to remove");
    if (! saveRegistration.remove()) throw new RuntimeException("SaveRegistration failed to remove");
    super.detach();
  }
}
