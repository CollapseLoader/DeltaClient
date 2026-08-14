package aethereal.discord;

import aethereal.util.UrlValidator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class Activity {
    private final ActivityType a;
    private final String b;
    private final String c;
    private final String d;
    private final ActivityTimestamps e;
    private final ActivityAssets f;
    private final ActivityParty g;
    private final ActivitySecrets h;
    private final List<ActivityButton> i;
    private final Boolean j;

    public Activity(ActivityType type, String state, String details, String url, ActivityTimestamps timestamps, ActivityAssets assets, ActivityParty party, ActivitySecrets secrets, List<ActivityButton> buttons, Boolean instance) {
        this.a = type != null ? type : ActivityType.PLAYING;
        this.b = state;
        this.c = details;
        this.d = url;
        this.e = timestamps;
        this.f = assets;
        this.g = party;
        this.h = secrets;
        this.i = buttons != null ? List.copyOf(buttons) : null;
        this.j = instance;
        if (this.b != null && (this.b.length() < 2 || this.b.length() > 128)) {
            throw new IllegalArgumentException("Activity state must be 2-128 characters, got " + this.b.length());
        }
        if (this.c != null && (this.c.length() < 2 || this.c.length() > 128)) {
            throw new IllegalArgumentException("Activity details must be 2-128 characters, got " + this.c.length());
        }
        if (this.i != null && this.i.size() > 2) {
            throw new IllegalArgumentException("Activity supports a maximum of 2 buttons, got " + this.i.size());
        }
        if (this.a == ActivityType.STREAMING && this.d == null) {
            throw new IllegalArgumentException("Streaming activity type requires a URL");
        }
        if (this.a == ActivityType.STREAMING) {
            UrlValidator.a(this.d, "Streaming URL", -1);
        }
    }

    public ActivityType k() {
        return this.a;
    }

    public String l() {
        return this.b;
    }

    public String m() {
        return this.c;
    }

    public String n() {
        return this.d;
    }

    public ActivityTimestamps o() {
        return this.e;
    }

    public ActivityAssets p() {
        return this.f;
    }

    public ActivityParty q() {
        return this.g;
    }

    public ActivitySecrets r() {
        return this.h;
    }

    public List<ActivityButton> s() {
        return this.i;
    }

    public Boolean t() {
        return this.j;
    }

    public Optional<String> a() {
        return Optional.ofNullable(this.b);
    }

    public Optional<String> b() {
        return Optional.ofNullable(this.c);
    }

    public Optional<String> c() {
        return Optional.ofNullable(this.d);
    }

    public Optional<ActivityTimestamps> d() {
        return Optional.ofNullable(this.e);
    }

    public Optional<ActivityAssets> e() {
        return Optional.ofNullable(this.f);
    }

    public Optional<ActivityParty> f() {
        return Optional.ofNullable(this.g);
    }

    public Optional<ActivitySecrets> g() {
        return Optional.ofNullable(this.h);
    }

    public Optional<List<ActivityButton>> h() {
        return Optional.ofNullable(this.i);
    }

    public Optional<Boolean> i() {
        return Optional.ofNullable(this.j);
    }

    public JsonObject j() {
        JsonObject json = new JsonObject();
        json.addProperty("type", Integer.valueOf(this.a.a()));
        a().ifPresent(s -> {
            json.addProperty("state", s);
        });
        b().ifPresent(d -> {
            json.addProperty("details", d);
        });
        c().ifPresent(u -> {
            json.addProperty("url", u);
        });
        d().ifPresent(t -> {
            json.add("timestamps", t.a());
        });
        e().ifPresent(a2 -> {
            json.add("assets", a2.a());
        });
        f().ifPresent(p -> {
            json.add("party", p.a());
        });
        g().ifPresent(s2 -> {
            json.add("secrets", s2.a());
        });
        h().filter(b -> {
            return !b.isEmpty();
        }).ifPresent(b2 -> {
            JsonArray arr = new JsonArray();
            b2.forEach(btn -> {
                arr.add(btn.a());
            });
            json.add("buttons", arr);
        });
        i().ifPresent(i -> {
            json.addProperty("instance", i);
        });
        return json;
    }

    public static final class a {
        private final List<ActivityButton> i = new ArrayList<>();
        private String b;
        private String c;
        private String d;
        private ActivityTimestamps e;
        private ActivityAssets f;
        private ActivityParty g;
        private ActivitySecrets h;
        private Boolean j;
        private ActivityType a = ActivityType.PLAYING;

        public a a(ActivityType type) {
            this.a = type;
            return this;
        }

        public a a(String state) {
            this.b = state;
            return this;
        }

        public a b(String details) {
            this.c = details;
            return this;
        }

        public a c(String url) {
            this.d = url;
            return this;
        }

        public a a(long epochSeconds) {
            this.e = new ActivityTimestamps(Long.valueOf(epochSeconds), this.e != null ? this.e.c() : null);
            return this;
        }

        public a b(long epochSeconds) {
            this.e = new ActivityTimestamps(this.e != null ? this.e.b() : null, Long.valueOf(epochSeconds));
            return this;
        }

        public a a(ActivityTimestamps timestamps) {
            this.e = timestamps;
            return this;
        }

        public a d(String key) {
            this.f = new ActivityAssets(key, this.f != null ? this.f.c() : null, this.f != null ? this.f.d() : null, this.f != null ? this.f.e() : null);
            return this;
        }

        public a a(String key, String text) {
            this.f = new ActivityAssets(key, text, this.f != null ? this.f.d() : null, this.f != null ? this.f.e() : null);
            return this;
        }

        public a b(String key, String text) {
            this.f = new ActivityAssets(this.f != null ? this.f.b() : null, this.f != null ? this.f.c() : null, key, text);
            return this;
        }

        public a a(ActivityAssets assets) {
            this.f = assets;
            return this;
        }

        public a a(String id, int currentSize, int maxSize) {
            this.g = ActivityParty.a(id, currentSize, maxSize);
            return this;
        }

        public a a(String id, int currentSize, int maxSize, int privacy) {
            this.g = ActivityParty.a(id, currentSize, maxSize, Integer.valueOf(privacy));
            return this;
        }

        public a a(ActivitySecrets secrets) {
            this.h = secrets;
            return this;
        }

        public a c(String label, String url) {
            this.i.add(new ActivityButton(label, url));
            return this;
        }

        public a a(boolean instance) {
            this.j = Boolean.valueOf(instance);
            return this;
        }

        public Activity a() {
            return new Activity(this.a, this.b, this.c, this.d, this.e, this.f, this.g, this.h, this.i.isEmpty() ? null : this.i, this.j);
        }
    }
}
