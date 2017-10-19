package org.rapidpm.vaadin.jumpstart.gui.screen.main;

import static org.rapidpm.frp.matcher.Case.match;
import static org.rapidpm.frp.matcher.Case.matchCase;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.rapidpm.frp.model.Result;
import org.rapidpm.vaadin.jumpstart.gui.logic.CustomerService;
import org.rapidpm.vaadin.jumpstart.gui.model.Customer;
import org.rapidpm.vaadin.jumpstart.microservice.MyStartupAction.MessageObservable;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedSession;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by svenruppert on 26.04.17.
 */
public class MainWindow extends VerticalLayout {

  //services
  @Inject private CustomerService service;
  @Inject private MessageObservable messageObservable;

  // UI Components
  @Inject private UserDataInputFormCustom form;

  private Grid<Customer> grid = new Grid<>();
  private TextField filterText = new TextField();

  final Supplier<List<Customer>> findAll = () -> service.findAll(filterText.getValue());
  final Consumer<List<Customer>> fillGrid = (consumer) -> grid.setItems(consumer);

  public static final Supplier<WrappedSession> session = () -> VaadinSession.getCurrent().getSession();
  //    public static final Supplier<String> sessionID = memoize(() -> session.get().getId());
  public static final Supplier<String> sessionID = () -> session.get().getId();
  public static final Supplier<VaadinService> vaadinService = () -> VaadinSession.getCurrent().getService();

  @PostConstruct
  protected void postConstruct() {

    final String key = sessionID.get();
    vaadinService.get()
                 .addSessionDestroyListener((SessionDestroyListener) event -> messageObservable.unregister(key));

    messageObservable
        .register(key ,
                  (value) -> {
                    final String messageKey = value.getT1();
                    final String messageValue = value.getT2();
                    match(
                        matchCase(() -> Result.success(" NO OP for " + value.toString())) ,
                        matchCase(key::isEmpty , () -> Result.failure("epmty event value")) ,
                        matchCase(() -> ! key.equals(messageKey) , () -> Result.failure("wrong session id " + messageKey)) ,
                        matchCase(() -> key.equals(messageKey) && messageValue.equals("UPDATE") , () -> {
                          fillGrid.accept(findAll.get());
                          return Result.success("UPDATE was done " + messageKey);
                        })
                    )
                        .ifPresentOrElse(
                            sucess -> System.out.println("SUCESS : " + sucess) ,
                            fail -> System.out.println("FAIL : " + fail)
                        );
                  });

    filterText.setPlaceholder("filter by name...");
    filterText.addValueChangeListener(e -> fillGrid.accept(findAll.get()));
    filterText.setValueChangeMode(ValueChangeMode.LAZY);

    final Button clearFilterTextBtn = new Button(FontAwesome.TIMES);
    clearFilterTextBtn.setDescription("Clear the current filter");
    clearFilterTextBtn.addClickListener(e -> filterText.clear());

    final CssLayout filtering = new CssLayout();
    filtering.addComponents(filterText , clearFilterTextBtn);
    filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

    final Button addCustomerBtn = new Button("Add new customer");
    addCustomerBtn.addClickListener(e -> {
      grid.asSingleSelect().clear();
      form.setCustomer(new Customer());
    });

    final HorizontalLayout toolbar = new HorizontalLayout(filtering , addCustomerBtn);

    grid.addColumn(Customer::getFirstName).setCaption("First Name");
    grid.addColumn(Customer::getLastName).setCaption("Last Name");
    grid.addColumn(Customer::getEmail).setCaption("Email");
    grid.asSingleSelect().addValueChangeListener(event -> {
      if (event.getValue() == null) {
        form.setVisible(false);
      } else {
        form.setCustomer(event.getValue());
      }
    });

    final HorizontalLayout main = new HorizontalLayout(grid , form);
    main.setSizeFull();
    grid.setSizeFull();
    main.setExpandRatio(grid , 1);

    this.addComponents(toolbar , main);

    // fetch list of Customers from service and assign it to Grid
    fillGrid.accept(findAll.get());

    form.setVisible(false);
  }
}