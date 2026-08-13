package aethereal.handler;

import aethereal.lib.javassist.*;

public interface ProceedHandler {
    void a(JvstCodeGen jvstCodeGen, Bytecode_2 bytecode_2, ASTList aSTList) throws CompileError;

    void a(JvstTypeChecker jvstTypeChecker, ASTList aSTList) throws CompileError;
}
