package aethereal.handler;


import aethereal.core.EventManager;

public class BaseHandler {
    public BaseHandler() {
        if (!getClass().isAnnotationPresent(Handler_2.class)) {
            throw new IllegalStateException("Обработчик " + getClass().getSimpleName() + " должен иметь @Handler!");
        }
        EventManager.a(this);
    }
}
