package aethereal.config;

import aethereal.core.EventManager;

import aethereal.core.Interface;

public abstract class BaseProcessor implements Interface {
    public BaseProcessor() {
        EventManager.a(this);
    }

    public abstract void setup();

    public abstract void unSetup();
}
