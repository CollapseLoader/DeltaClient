package aethereal.util;

import aethereal.lib.log4j.Logger;
import aethereal.lib.log4j.StatusLogger;

import java.lang.reflect.Method;
import java.nio.charset.Charset;

public class Base64Util {
    private static final Logger a = StatusLogger.x();
    private static Method b;
    private static Object c;

    static {
        b = null;
        c = null;
        try {
            Class<?> clazz = LoaderUtil.b("java.util.Base64");
            Class<?> encoderClazz = LoaderUtil.b("java.util.Base64.Encoder");
            Method method = clazz.getMethod("getEncoder");
            c = method.invoke(null);
            b = encoderClazz.getMethod("encodeToString", byte[].class);
        } catch (Exception e) {
            try {
                Class<?> clazz2 = LoaderUtil.b("javax.xml.bind.DataTypeConverter");
                b = clazz2.getMethod("printBase64Binary");
            } catch (Exception ex2) {
                a.b("Unable to create a Base64 Encoder", ex2);
            }
        }
    }

    private Base64Util() {
    }

    @Deprecated
    public static String a(final String str) {
        if (str == null) {
            return null;
        }
        byte[] data = str.getBytes(Charset.defaultCharset());
        if (b != null) {
            try {
                return (String) b.invoke(c, (Object) data);
            } catch (Exception ex) {
                throw new LoggingException("Unable to encode String", ex);
            }
        }
        throw new LoggingException("No Encoder, unable to encode string");
    }
}
