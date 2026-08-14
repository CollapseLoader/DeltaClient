package aethereal.config;


import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public abstract class ConfigProcessor<T> extends BaseProcessor {
    protected final File b = new File(mc.runDirectory, "configs");
    protected final File c = new File(new File(mc.runDirectory, "configs"), "general");
    protected final List<T> d = new ArrayList<>();

    protected abstract String b();

    @Override

    public void setup() {
        try {
            List<T> list = this.d;
            if (b() == null) {
                return;
            }
            File fileD = d();
            if (!fileD.exists()) {
                fileD.mkdirs();
            }
            File file = new File(fileD, b());
            String string = file.exists() ? Files.readString(file.toPath()) : "";
            List<T> listA = a(string.isEmpty() ? "[]" : string);
            if (listA != null) {
                list.clear();
                list.addAll(listA);
            }
            if (string.isEmpty()) {
                Files.writeString(file.toPath(), a(list));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    protected abstract List<T> a(String str) throws Exception;


    protected abstract String a(List<T> list) throws Exception;

    public File c() {
        return this.b;
    }

    public File d() {
        return this.c;
    }

    public List<T> e() {
        return this.d;
    }

    @Override
    public void unSetup() {
        if (b() != null) {
            try {
                File file = new File(d(), b());
                Files.writeString(file.toPath(), a(this.d));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
