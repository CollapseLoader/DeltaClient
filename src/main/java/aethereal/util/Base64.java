package aethereal.util;


import aethereal.core.InterfaceC0020Opcode;

import java.io.*;
import java.util.zip.GZIPOutputStream;

public class Base64 {
    public static final int a = 0;
    public static final int b = 1;
    public static final int c = 2;
    public static final int d = 8;
    public static final int e = 16;
    public static final int f = 32;
    static final boolean g;
    private static final int h = 76;
    private static final byte i = 61;
    private static final byte j = 10;
    private static final String k = "US-ASCII";
    private static final byte l = -5;
    private static final byte[] m;
    private static final byte[] n;
    private static final byte[] o;
    private static final byte[] p;
    private static final byte[] q;
    private static final byte[] r;

    static {
        g = !Base64.class.desiredAssertionStatus();
        m = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
        n = new byte[]{-9, -9, -9, -9, -9, -9, -9, -9, -9, l, l, -9, -9, l, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, l, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, -9, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, -9, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9};
        o = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
        p = new byte[]{-9, -9, -9, -9, -9, -9, -9, -9, -9, l, l, -9, -9, l, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, l, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, 63, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9};
        q = new byte[]{45, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 95, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122};
        r = new byte[]{-9, -9, -9, -9, -9, -9, -9, -9, -9, l, l, -9, -9, l, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, l, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 0, -9, -9, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, -9, -9, -9, -1, -9, -9, -9, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, -9, -9, -9, -9, 37, -9, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9};
    }

    private Base64() {
    }

    private static byte[] b(int options) {
        if ((options & 16) == 16) {
            return o;
        }
        if ((options & 32) == 32) {
            return q;
        }
        return m;
    }

    public static byte[] c(int options) {
        if ((options & 16) == 16) {
            return p;
        }
        if ((options & 32) == 32) {
            return r;
        }
        return n;
    }

    public static byte[] b(byte[] b4, byte[] threeBytes, int numSigBytes, int options) {
        a(threeBytes, 0, numSigBytes, b4, 0, options);
        return b4;
    }

    private static void a(byte[] source, int srcOffset, int numSigBytes, byte[] destination, int destOffset, int options) {
        byte[] ALPHABET = b(options);
        int inBuff = (numSigBytes > 0 ? (source[srcOffset] << 24) >>> 8 : 0) | (numSigBytes > 1 ? (source[srcOffset + 1] << 24) >>> 16 : 0) | (numSigBytes > 2 ? (source[srcOffset + 2] << 24) >>> 24 : 0);
        switch (numSigBytes) {
            case 1:
                destination[destOffset] = ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 63];
                destination[destOffset + 2] = 61;
                destination[destOffset + 3] = 61;
                break;
            case 2:
                destination[destOffset] = ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 63];
                destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 63];
                destination[destOffset + 3] = 61;
                break;
            case 3:
                destination[destOffset] = ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 63];
                destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 63];
                destination[destOffset + 3] = ALPHABET[inBuff & 63];
                break;
            default:
                break;
        }
    }

    public static String a(byte[] source) {
        String encoded = null;
        try {
            encoded = a(source, 0, source.length, 0);
        } catch (IOException ex) {
            if (!g) {
                throw new AssertionError(ex.getMessage());
            }
        }
        if (g || encoded != null) {
            return encoded;
        }
        throw new AssertionError();
    }

    public static String a(byte[] source, int off, int len, int options) throws IOException {
        byte[] encoded = b(source, off, len, options);
        try {
            return new String(encoded, k);
        } catch (UnsupportedEncodingException e2) {
            return new String(encoded);
        }
    }

    public static byte[] b(byte[] source, int off, int len, int options) throws IOException {
        if (source == null) {
            throw new IllegalArgumentException("Cannot serialize a null array.");
        }
        if (off < 0) {
            throw new IllegalArgumentException("Cannot have negative offset: " + off);
        }
        if (len < 0) {
            throw new IllegalArgumentException("Cannot have length offset: " + len);
        }
        if (off + len > source.length) {
            throw new IllegalArgumentException(String.format("Cannot have offset of %d and length of %d with array of length %d", Integer.valueOf(off), Integer.valueOf(len), Integer.valueOf(source.length)));
        }
        if ((options & 2) != 0) {
            ByteArrayOutputStream baos = null;
            GZIPOutputStream gzos = null;
            a b64os = null;
            try {
                try {
                    baos = new ByteArrayOutputStream();
                    b64os = new a(baos, 1 | options);
                    gzos = new GZIPOutputStream(b64os);
                    gzos.write(source, off, len);
                    gzos.close();
                    if (gzos != null) {
                        try {
                            gzos.close();
                        } catch (Exception e2) {
                        }
                    }
                    if (b64os != null) {
                        try {
                            b64os.close();
                        } catch (Exception e3) {
                        }
                    }
                    if (baos != null) {
                        try {
                            baos.close();
                        } catch (Exception e4) {
                        }
                    }
                    return baos.toByteArray();
                } catch (Throwable th) {
                    if (gzos != null) {
                        try {
                            gzos.close();
                        } catch (Exception e5) {
                            if (b64os != null) {
                                try {
                                    b64os.close();
                                } catch (Exception e6) {
                                    if (baos != null) {
                                        try {
                                            baos.close();
                                        } catch (Exception e7) {
                                            throw th;
                                        }
                                    }
                                    throw th;
                                }
                            }
                            if (baos != null) {
                                baos.close();
                            }
                            throw th;
                        }
                    }
                    if (b64os != null) {
                        b64os.close();
                    }
                    if (baos != null) {
                        baos.close();
                    }
                    throw th;
                }
            } catch (IOException e8) {
                throw e8;
            }
        }
        boolean breakLines = (options & 8) != 0;
        int encLen = ((len / 3) * 4) + (len % 3 > 0 ? 4 : 0);
        if (breakLines) {
            encLen += encLen / 76;
        }
        byte[] outBuff = new byte[encLen];
        int d2 = 0;
        int e9 = 0;
        int len2 = len - 2;
        int lineLength = 0;
        while (d2 < len2) {
            a(source, d2 + off, 3, outBuff, e9, options);
            lineLength += 4;
            if (breakLines && lineLength >= 76) {
                outBuff[e9 + 4] = 10;
                e9++;
                lineLength = 0;
            }
            d2 += 3;
            e9 += 4;
        }
        if (d2 < len) {
            a(source, d2 + off, len - d2, outBuff, e9, options);
            e9 += 4;
        }
        if (e9 <= outBuff.length - 1) {
            byte[] finalOut = new byte[e9];
            System.arraycopy(outBuff, 0, finalOut, 0, e9);
            return finalOut;
        }
        return outBuff;
    }

    public static int b(byte[] source, int srcOffset, byte[] destination, int destOffset, int options) {
        if (source == null) {
            throw new IllegalArgumentException("Source array was null.");
        }
        if (destination == null) {
            throw new IllegalArgumentException("Destination array was null.");
        }
        if (srcOffset < 0 || srcOffset + 3 >= source.length) {
            throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and still process four bytes.", Integer.valueOf(source.length), Integer.valueOf(srcOffset)));
        }
        if (destOffset < 0 || destOffset + 2 >= destination.length) {
            throw new IllegalArgumentException(String.format("Destination array with length %d cannot have offset of %d and still store three bytes.", Integer.valueOf(destination.length), Integer.valueOf(destOffset)));
        }
        byte[] DECODABET = c(options);
        if (source[srcOffset + 2] == 61) {
            destination[destOffset] = (byte) ((((DECODABET[source[srcOffset]] & 255) << 18) | ((DECODABET[source[srcOffset + 1]] & 255) << 12)) >>> 16);
            return 1;
        }
        if (source[srcOffset + 3] == 61) {
            int outBuff = ((DECODABET[source[srcOffset]] & 255) << 18) | ((DECODABET[source[srcOffset + 1]] & 255) << 12) | ((DECODABET[source[srcOffset + 2]] & 255) << 6);
            destination[destOffset] = (byte) (outBuff >>> 16);
            destination[destOffset + 1] = (byte) (outBuff >>> 8);
            return 2;
        }
        int outBuff2 = ((DECODABET[source[srcOffset]] & 255) << 18) | ((DECODABET[source[srcOffset + 1]] & 255) << 12) | ((DECODABET[source[srcOffset + 2]] & 255) << 6) | (DECODABET[source[srcOffset + 3]] & 255);
        destination[destOffset] = (byte) (outBuff2 >> 16);
        destination[destOffset + 1] = (byte) (outBuff2 >> 8);
        destination[destOffset + 2] = (byte) outBuff2;
        return 3;
    }

    public static class a extends FilterOutputStream {
        private final boolean a;
        private int b;
        private byte[] c;
        private final int d;
        private int e;
        private final boolean f;
        private final byte[] g;
        private final boolean h;
        private final int i;
        private final byte[] j;

        public a(OutputStream out) {
            this(out, 1);
        }

        public a(OutputStream out, int options) {
            super(out);
            this.f = (options & 8) != 0;
            this.a = (options & 1) != 0;
            this.d = this.a ? 3 : 4;
            this.c = new byte[this.d];
            this.b = 0;
            this.e = 0;
            this.h = false;
            this.g = new byte[4];
            this.i = options;
            this.j = Base64.c(options);
        }

        @Override
        public void write(int theByte) throws IOException {
            if (this.h) {
                this.out.write(theByte);
                return;
            }
            if (this.a) {
                byte[] bArr = this.c;
                int i = this.b;
                this.b = i + 1;
                bArr[i] = (byte) theByte;
                if (this.b >= this.d) {
                    this.out.write(Base64.b(this.g, this.c, this.d, this.i));
                    this.e += 4;
                    if (this.f && this.e >= 76) {
                        this.out.write(10);
                        this.e = 0;
                    }
                    this.b = 0;
                    return;
                }
                return;
            }
            if (this.j[theByte & InterfaceC0020Opcode.ce] > Base64.l) {
                byte[] bArr2 = this.c;
                int i2 = this.b;
                this.b = i2 + 1;
                bArr2[i2] = (byte) theByte;
                if (this.b >= this.d) {
                    int len = Base64.b(this.c, 0, this.g, 0, this.i);
                    this.out.write(this.g, 0, len);
                    this.b = 0;
                    return;
                }
                return;
            }
            if (this.j[theByte & InterfaceC0020Opcode.ce] != Base64.l) {
                throw new IOException("Invalid character in Base64 data.");
            }
        }

        @Override
        public void write(byte[] theBytes, int off, int len) throws IOException {
            if (this.h) {
                this.out.write(theBytes, off, len);
                return;
            }
            for (int i = 0; i < len; i++) {
                write(theBytes[off + i]);
            }
        }

        public void a() throws IOException {
            if (this.b > 0) {
                if (this.a) {
                    this.out.write(Base64.b(this.g, this.c, this.b, this.i));
                    this.b = 0;
                    return;
                }
                throw new IOException("Base64 input not properly padded.");
            }
        }

        @Override
        public void close() throws IOException {
            a();
            super.close();
            this.c = null;
            this.out = null;
        }
    }
}
