package aethereal.discord;


import aethereal.lib.jsoup.Connection;

import java.io.IOException;

@FunctionalInterface
public interface ConnectionFactory {
    Connection create(String str) throws IOException;
}
