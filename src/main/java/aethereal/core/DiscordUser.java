package aethereal.core;


import aethereal.util.JsonUtils;
import aethereal.util.ObjectUtils;
import aethereal.util.StringUtils;
import com.google.gson.JsonObject;

import java.util.Objects;
import java.util.Optional;

public record DiscordUser(String id, String username, String discriminator, String globalName, String avatar,
                          boolean bot) {
    public DiscordUser(String id, String username, String discriminator, String globalName, String avatar, boolean bot) {
        this.id = id;
        this.username = username;
        this.discriminator = StringUtils.e(discriminator, "0");
        this.globalName = globalName;
        this.avatar = avatar;
        this.bot = bot;
    }

    public static DiscordUser fromJson(JsonObject json) {
        return new DiscordUser(JsonUtils.a(json, "id", "0"), JsonUtils.a(json, "username", "Unknown"), JsonUtils.a(json, "discriminator", "0"), JsonUtils.a(json, "global_name").orElse(null), JsonUtils.a(json, "avatar").orElse(null), JsonUtils.a(json, "bot", false));
    }

    /**
     * @deprecated Use {@link #fromJson(JsonObject)}
     */
    @Deprecated
    public static DiscordUser a(JsonObject json) {
        return fromJson(json);
    }

    /**
     * @deprecated Use {@link #id ()}
     */
    @Deprecated
    public String k() {
        return id();
    }

    /**
     * @deprecated Use {@link #username ()}
     */
    @Deprecated
    public String l() {
        return username();
    }

    /**
     * @deprecated Use {@link #discriminator ()}
     */
    @Deprecated
    public String m() {
        return discriminator();
    }

    /**
     * @deprecated Use {@link #globalName ()}
     */
    @Deprecated
    public String n() {
        return globalName();
    }

    /**
     * @deprecated Use {@link #avatar ()}
     */
    @Deprecated
    public String o() {
        return avatar();
    }

    /**
     * @deprecated Use {@link #bot ()}
     */
    @Deprecated
    public boolean p() {
        return bot();
    }

    public Optional<String> getGlobalNameOptional() {
        return Optional.ofNullable(this.globalName);
    }

    /**
     * @deprecated Use {@link #getGlobalNameOptional()}
     */
    @Deprecated
    public Optional<String> a() {
        return getGlobalNameOptional();
    }

    public Optional<String> getAvatarUrl() {
        return Optional.ofNullable(this.avatar);
    }

    /**
     * @deprecated Use {@link #getAvatarUrl()}
     */
    @Deprecated
    public Optional<String> b() {
        return getAvatarUrl();
    }

    public long getSnowflakeId() {
        try {
            return Long.parseLong(this.id);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("User ID is not a numeric Discord snowflake: " + this.id, e);
        }
    }

    /**
     * @deprecated Use {@link #getSnowflakeId()}
     */
    @Deprecated
    public long c() {
        return getSnowflakeId();
    }

    public String getDisplayName() {
        return ObjectUtils.a(StringUtils.w(this.globalName), this.username);
    }

    /**
     * @deprecated Use {@link #getDisplayName()}
     */
    @Deprecated
    public String d() {
        return getDisplayName();
    }

    public String getEffectiveName() {
        return this.username;
    }

    /**
     * @deprecated Use {@link #getEffectiveName()}
     */
    @Deprecated
    public String e() {
        return getEffectiveName();
    }

    public String getTag() {
        return "0".equals(this.discriminator) ? this.username : this.username + "#" + this.discriminator;
    }

    /**
     * @deprecated Use {@link #getTag()}
     */
    @Deprecated
    public String f() {
        return getTag();
    }

    public Optional<String> getCdnAvatarUrl() {
        return getAvatarUrl().map(a -> {
            String ext = a.startsWith("a_") ? "gif" : "png";
            return "https://cdn.discordapp.com/avatars/" + this.id + "/" + a + "." + ext;
        });
    }

    /**
     * @deprecated Use {@link #getCdnAvatarUrl()}
     */
    @Deprecated
    public Optional<String> g() {
        return getCdnAvatarUrl();
    }

    public String getDefaultAvatarUrl() {
        int index;
        try {
            if ("0".equals(this.discriminator)) {
                index = (int) ((getSnowflakeId() >> 22) % 6);
            } else {
                index = Integer.parseInt(this.discriminator) % 5;
            }
        } catch (RuntimeException e) {
            index = 0;
        }
        return "https://cdn.discordapp.com/embed/avatars/" + index + ".png";
    }

    /**
     * @deprecated Use {@link #getDefaultAvatarUrl()}
     */
    @Deprecated
    public String h() {
        return getDefaultAvatarUrl();
    }

    public String getBestAvatarUrl() {
        return getCdnAvatarUrl().orElseGet(this::getDefaultAvatarUrl);
    }

    /**
     * @deprecated Use {@link #getBestAvatarUrl()}
     */
    @Deprecated
    public String i() {
        return getBestAvatarUrl();
    }

    public String getAsMention() {
        return "<@" + this.id + ">";
    }

    /**
     * @deprecated Use {@link #getAsMention()}
     */
    @Deprecated
    public String j() {
        return getAsMention();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DiscordUser u)) {
            return false;
        }
        return Objects.equals(this.id, u.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    @Override
    public String toString() {
        return "User:" + getTag() + "(" + this.id + ")";
    }
}
