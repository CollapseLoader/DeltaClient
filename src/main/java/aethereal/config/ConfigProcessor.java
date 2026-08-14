package aethereal.config;


import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public abstract class ConfigProcessor<T> extends BaseProcessor {
    protected final File b = new File(mc.runDirectory, "configs");
    protected final File c = new File(new File(mc.runDirectory, "configs"), "general");
    protected final List<T> d = new ArrayList<>();

    protected abstract String getConfigFileName();

    @Override

    public void setup() {
        try {
            List<T> list = this.d;
            if (getConfigFileName() == null) {
                return;
            }
            File fileD = d();
            if (!fileD.exists()) {
                fileD.mkdirs();
            }
            File file = new File(fileD, getConfigFileName());
            String string = file.exists() ? Files.readString(file.toPath()) : "";
            List<T> listA = loadConfig(string.isEmpty() ? "[]" : string);
            if (listA != null) {
                list.clear();
                list.addAll(listA);
            }
            if (string.isEmpty()) {
                Files.writeString(file.toPath(), saveConfig(list));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    protected abstract List<T> loadConfig(String str) throws Exception;


    protected abstract String saveConfig(List<T> list) throws Exception;

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
        if (getConfigFileName() != null) {
            try {
                File file = new File(d(), getConfigFileName());
                Files.writeString(file.toPath(), saveConfig(this.d));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
