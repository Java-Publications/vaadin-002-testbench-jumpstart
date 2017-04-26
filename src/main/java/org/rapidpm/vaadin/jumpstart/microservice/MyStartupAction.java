package org.rapidpm.vaadin.jumpstart.microservice;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.rapidpm.ddi.DI;
import org.rapidpm.ddi.scopes.provided.JVMSingletonInjectionScope;
import org.rapidpm.frp.model.Pair;
import org.rapidpm.microservice.Main;
import org.rapidpm.vaadin.jumpstart.gui.logic.CustomerService;

/**
 * Created by svenruppert on 26.04.17.
 */
public class MyStartupAction implements Main.MainStartupAction {

    @Override
    public void execute(Optional<String[]> args) {
//        DI.registerClassForScope(CustomerService.class, JVMSingletonInjectionScope.class.getSimpleName());
        DI.registerClassForScope(MessageObservable.class, JVMSingletonInjectionScope.class.getSimpleName());
    }


    public static class EventPair extends Pair<String,String>{
        public EventPair(String s, String s2) {
            super(s, s2);
        }
    }

    public static class MessageObservable extends Observable<String, EventPair> {}


    public static class Observable<KEY, VALUE> {

        private final Map<KEY, Consumer<VALUE>> listeners = new ConcurrentHashMap<>();

        public void register(KEY key, Consumer<VALUE> listener) {
            listeners.put(key, listener);
        }

        public void unregister(KEY key) {
            listeners.remove(key);
        }

        public void sendEvent(VALUE event) {
            listeners.values().forEach(listener -> listener.accept(event));
        }

    }
}
