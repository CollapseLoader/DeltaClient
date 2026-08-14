package aethereal.network;

public record OrderModel(String a, String b, String c, String d, double e, String f, int g) {
    public OrderModel {
    }

    public String toString() {
        String strA = a();
        String strB = b();
        String strC = c();
        String strD = d();
        double dE = e();
        String strF = f();
        g();
        return "OrderModel(id=" + strA + ", category=" + strB + ", name=" + strC + ", buyerName=" + strD + ", price="
                + dE + ", unit=" + strA + ", count=" + strF + ")";
    }

    @Override
    public String a() {
        return this.a;
    }

    @Override
    public String b() {
        return this.b;
    }

    @Override
    public String c() {
        return this.c;
    }

    @Override
    public String d() {
        return this.d;
    }

    @Override
    public double e() {
        return this.e;
    }

    @Override
    public String f() {
        return this.f;
    }

    @Override
    public int g() {
        return this.g;
    }
}
