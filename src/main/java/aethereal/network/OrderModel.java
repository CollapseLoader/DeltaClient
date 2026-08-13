package aethereal.network;


import lombok.Generated;

public record OrderModel(String a, String b, String c, String d, double e, String f, int g) {
    @Generated
    public OrderModel {
    }

    @Generated
    public String toString() {
        String strA = a();
        String strB = b();
        String strC = c();
        String strD = d();
        double dE = e();
        String strF = f();
        g();
        return "OrderModel(id=" + strA + ", category=" + strB + ", name=" + strC + ", buyerName=" + strD + ", price=" + dE + ", unit=" + strA + ", count=" + strF + ")";
    }

    @Override
    @Generated
    public String a() {
        return this.a;
    }

    @Override
    @Generated
    public String b() {
        return this.b;
    }

    @Override
    @Generated
    public String c() {
        return this.c;
    }

    @Override
    @Generated
    public String d() {
        return this.d;
    }

    @Override
    @Generated
    public double e() {
        return this.e;
    }

    @Override
    @Generated
    public String f() {
        return this.f;
    }

    @Override
    @Generated
    public int g() {
        return this.g;
    }
}
