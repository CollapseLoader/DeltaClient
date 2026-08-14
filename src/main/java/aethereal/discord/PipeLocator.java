package aethereal.discord;


import aethereal.lib.log4j.LogManager;
import aethereal.lib.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PipeLocator {

    static final int a = 10;
    private static final Logger b = LogManager.b(PipeLocator.class);
    private static final List<String> c = List.of("XDG_RUNTIME_DIR", "TMPDIR", "TMP", "TEMP");
    private static final Path d = a(System.getenv(), System.getProperty("java.io.tmpdir"), System.getProperty("user.name", ""));

    private PipeLocator() {
    }

    static List<String> a() {
        List<String> paths = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            paths.add(a(i));
        }
        return paths;
    }

    public static List<String> locateAll() {
        return a();
    }

    static String a(int index) {
        switch (Platform.d) {
            case WINDOWS:
                return "\\\\.\\pipe\\discord-ipc-" + index;
            case MACOS:
            case LINUX:
                return d.resolve("discord-ipc-" + index).toString();
            default:
                throw new IncompatibleClassChangeError();
        }
    }

    static Path a(Map<String, String> env, String javaTmpDir, String currentUser) {
        for (String envVar : c) {
            Path candidate = a(env.get(envVar), currentUser);
            if (candidate != null) {
                b.a("Using {} = {} for pipe directory", envVar, candidate);
                return candidate;
            }
        }
        Path javaTmp = a(javaTmpDir, currentUser);
        if (javaTmp != null) {
            b.a("Using java.io.tmpdir = {} for pipe directory", javaTmp);
            return javaTmp;
        }
        Path fallback = a("/tmp", currentUser);
        if (fallback != null) {
            return fallback;
        }
        return Path.of("/tmp").toAbsolutePath().normalize();
    }

    static Path a(String candidate, String currentUser) {
        if (candidate == null || candidate.isBlank()) {
            return null;
        }
        try {
            return a(Path.of(candidate), currentUser);
        } catch (InvalidPathException e) {
            b.a("Ignoring invalid pipe directory path: {}", candidate, e);
            return null;
        }
    }

    static Path a(Path candidate, String currentUser) {
        if (candidate == null) {
            return null;
        }
        try {
            if (Files.isSymbolicLink(candidate) || !Files.isDirectory(candidate, LinkOption.NOFOLLOW_LINKS)) {
                return null;
            }
            Path realPath = candidate.toRealPath(LinkOption.NOFOLLOW_LINKS);
            if (!Files.isDirectory(realPath, LinkOption.NOFOLLOW_LINKS)) {
                return null;
            }
            if (a(realPath) && !b(realPath, currentUser)) {
                b.a("Ignoring pipe directory not owned by trusted user: {}", realPath);
                return null;
            }
            return realPath;
        } catch (IOException e) {
            b.a("Ignoring unusable pipe directory: {}", candidate, e);
            return null;
        }
    }

    private static boolean a(Path path) {
        return path.getFileSystem().supportedFileAttributeViews().contains("posix");
    }

    private static boolean b(Path path, String currentUser) throws IOException {
        if (currentUser == null || currentUser.isBlank()) {
            return true;
        }
        String owner = Files.getOwner(path, LinkOption.NOFOLLOW_LINKS).getName();
        return currentUser.equals(owner) || "root".equals(owner);
    }
}
