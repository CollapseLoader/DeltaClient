package aethereal.core;


public record Processor(Architecture a, Vendor b) {

    public boolean c() {
        return Processor.Architecture.BIT_32 == this.a;
    }

    public boolean d() {
        return Processor.Architecture.BIT_64 == this.a;
    }

    public boolean e() {
        return Processor.Vendor.AARCH_64 == this.b;
    }

    public boolean f() {
        return Processor.Vendor.IA_64 == this.b;
    }

    public boolean g() {
        return Processor.Vendor.PPC == this.b;
    }

    public boolean h() {
        return Processor.Vendor.RISC_V == this.b;
    }

    public boolean i() {
        return Processor.Vendor.X86 == this.b;
    }

    public String toString() {
        return this.b.a() + ' ' + this.a.a();
    }

    public enum Architecture {
        BIT_32("32-bit"),
        BIT_64("64-bit"),
        UNKNOWN("Unknown");

        private final String d;

        Architecture(final String label) {
            this.d = label;
        }

        public String a() {
            return this.d;
        }
    }

    public enum Vendor {
        AARCH_64("AArch64"),
        X86("x86"),
        IA_64("IA-64"),
        PPC("PPC"),
        RISC_V("RISC-V"),
        UNKNOWN("Unknown");

        private final String g;

        Vendor(final String label) {
            this.g = label;
        }

        public String a() {
            return this.g;
        }
    }
}
