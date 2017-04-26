package org.rapidpm.vaadin.jumpstart.gui.screen.main;

import static org.rapidpm.vaadin.jumpstart.gui.screen.main.MainWindow.sessionID;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.rapidpm.vaadin.jumpstart.gui.generated.main.UserDataInputForm;
import org.rapidpm.vaadin.jumpstart.gui.logic.CustomerService;
import org.rapidpm.vaadin.jumpstart.gui.logic.properties.PropertyService;
import org.rapidpm.vaadin.jumpstart.gui.model.Customer;
import org.rapidpm.vaadin.jumpstart.gui.model.CustomerStatus;
import org.rapidpm.vaadin.jumpstart.microservice.MyStartupAction.EventPair;
import org.rapidpm.vaadin.jumpstart.microservice.MyStartupAction.MessageObservable;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by svenruppert on 26.04.17.
 */
public class UserDataInputFormCustom extends UserDataInputForm {

    @Inject private CustomerService service;
    @Inject private PropertyService propertyService;
    @Inject private MessageObservable messageObservable;

    //    private MainWindow myUI;
    private final Binder<Customer> beanBinder = new Binder<>(Customer.class);
    private final EventPair update = new EventPair(sessionID.get(), "UPDATE");

    private Customer customer;

    public void setCustomer(Customer customer) {
        this.customer = customer;
        beanBinder.setBean(customer);
        // Show delete button for only customers already in the database
        delete.setVisible(customer.isPersisted());
        setVisible(true);
        firstName.selectAll();
    }

    private void delete() {
        service.delete(customer);
        messageObservable.sendEvent(update);
        setVisible(false);
    }

    private void save() {
        service.save(customer);
        messageObservable.sendEvent(update);
        setVisible(false);
    }

    @PostConstruct
    public void postConstruct() {

        firstName.setCaption("First name");
        delete.setCaption("Delete");

        lastName.setCaption("Last name");
        email.setCaption("Email");
        status.setCaption("Status");
        birthDate.setCaption("Birthday");
        save.setCaption("Save");

        setSizeUndefined();
        final HorizontalLayout buttons = new HorizontalLayout(save, delete);
        addComponents(firstName, lastName, email, status, birthDate, buttons);

        status.setItems(CustomerStatus.values());
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        beanBinder.bindInstanceFields(this);

        save.addClickListener(e -> this.save());
        delete.addClickListener(e -> this.delete());
        messageObservable.sendEvent(update);

    }

}
