package aethereal.event;


import aethereal.core.Event;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Fog;
import net.minecraft.client.render.FogShape;
import net.minecraft.world.biome.Biome;

public class AmbienceEvent {

    public static class c extends Event {
        private long time;

        public c(long time) {
            this.time = time;
        }

        public long getTime() {
            return this.time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }

    public static class a extends Event {
        private float red;
        private float green;
        private float blue;
        private float alpha;

        public a(float red, float green, float blue, float alpha) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
        }

        public float getRed() {
            return this.red;
        }

        public void setRed(float red) {
            this.red = red;
        }

        public float getGreen() {
            return this.green;
        }

        public void setGreen(float green) {
            this.green = green;
        }

        public float getBlue() {
            return this.blue;
        }

        public void setBlue(float blue) {
            this.blue = blue;
        }

        public float getAlpha() {
            return this.alpha;
        }

        public void setAlpha(float alpha) {
            this.alpha = alpha;
        }
    }

    public static class b extends Event {
        private Camera camera;
        private float viewDistance;
        private Fog fog;

        public b(Camera camera, float viewDistance, Fog fog) {
            this.camera = camera;
            this.viewDistance = viewDistance;
            this.fog = fog;
        }

        public void setViewDistance(float viewDistance) {
            this.viewDistance = viewDistance;
        }

        public void setFog(Fog fog) {
            this.fog = fog;
        }

        public Camera getCamera() {
            return this.camera;
        }

        public void setCamera(Camera camera) {
            this.camera = camera;
        }

        public float c() {
            return this.viewDistance;
        }

        public Fog d() {
            return this.fog;
        }

        public void a(float start, float end, FogShape shape, float red, float green, float blue, float alpha) {
            this.fog = new Fog(start, end, shape, red, green, blue, alpha);
        }
    }

    public static class d extends Event {
        private final aethereal.event.AmbienceEvent.d.type type;
        private float value;
        private Biome.Precipitation precipitation;

        public d(aethereal.event.AmbienceEvent.d.type type, float value) {
            this.type = type;
            this.value = value;
        }

        public d(aethereal.event.AmbienceEvent.d.type type, Biome.Precipitation value) {
            this.type = type;
            this.precipitation = value;
        }

        public aethereal.event.AmbienceEvent.d.type b() {
            return this.type;
        }

        public float c() {
            return this.value;
        }

        public void a(float floatValue) {
            this.value = floatValue;
        }

        public Biome.Precipitation precipitation() {
            return this.precipitation;
        }

        public void a(Biome.Precipitation precipitationValue) {
            this.precipitation = precipitationValue;
        }

        public enum type {
            RAIN_GRADIENT,
            THUNDER_GRADIENT,
            PRECIPITATION_PARTICLES,
            PRECIPITATION
        }
    }
}
