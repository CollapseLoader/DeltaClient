package aethereal.network;


import aethereal.lib.misc.RequestAuthenticator;

import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;

public class AuthenticationHandler extends Authenticator {
    static final int a = 5;
    static a b;

    static {
        try {
            b = (a) Class.forName("org.jsoup.helper.RequestAuthHandler").getConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (ClassNotFoundException e) {
            b = new b();
        } catch (Exception e2) {
            throw new IllegalStateException(e2);
        }
    }

    RequestAuthenticator c;
    int d = 0;

    AuthenticationHandler() {
    }

    AuthenticationHandler(RequestAuthenticator auth) {
        this.c = auth;
    }

    @Override
    public final PasswordAuthentication getPasswordAuthentication() {
        AuthenticationHandler delegate = b.a(this);
        if (delegate == null) {
            return null;
        }
        delegate.d++;
        if (delegate.d > 5 || delegate.c == null) {
            return null;
        }
        RequestAuthenticator.a ctx = new RequestAuthenticator.a(getRequestingURL(), getRequestorType(), getRequestingPrompt());
        return delegate.c.a(ctx);
    }

    interface a {
        void a(RequestAuthenticator requestAuthenticator, HttpURLConnection httpURLConnection);

        void a();

        AuthenticationHandler a(AuthenticationHandler authenticationHandler);
    }

    static class b implements a {
        static final ThreadLocal<AuthenticationHandler> a = new ThreadLocal<>();

        static {
            Authenticator.setDefault(new AuthenticationHandler());
        }

        b() {
        }

        @Override
        public void a(RequestAuthenticator auth, HttpURLConnection con) {
            a.set(new AuthenticationHandler(auth));
        }

        @Override
        public void a() {
            a.remove();
        }

        @Override
        public AuthenticationHandler a(AuthenticationHandler helper) {
            return a.get();
        }
    }
}
