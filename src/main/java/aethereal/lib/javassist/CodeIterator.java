package aethereal.lib.javassist;

public class CodeIterator extends javassist.bytecode.CodeIterator {
    public CodeIterator(javassist.bytecode.CodeAttribute ca) {
        super(ca);
    }

    public int d(int pos) {
        return byteAt(pos);
    }

    public int g(int pos) {
        return u16bitAt(pos);
    }

    public int h(int pos) {
        return s32bitAt(pos);
    }
}
