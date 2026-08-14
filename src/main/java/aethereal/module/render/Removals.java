package aethereal.module.render;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.RemovalsEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;

@ModuleRegister(name = "Removals", description = "Убирает выбранные визуальные эффекты и элементы игры", category = Category.Render)
public class Removals extends Module {
    private final MultiModeSetting b = new MultiModeSetting("Отключённые элементы", new BooleanSetting("Тряска при уроне", true), new BooleanSetting("Скорбоард", false), new BooleanSetting("Боссбар", false), new BooleanSetting("Эффект портала", true), new BooleanSetting("Огонь", true), new BooleanSetting("Обрезка камеры", true), new BooleanSetting("Частицы разрушения", false), new BooleanSetting("Чёрные сердца", true), new BooleanSetting("Частицы погоды", false), new BooleanSetting("Погружение воды/лавы", false), new BooleanSetting("Тошнота", true), new BooleanSetting("Слепота", true), new BooleanSetting("Тыква", true), new BooleanSetting("Свечение", true), new BooleanSetting("Тьма", true));

    public Removals() {
        a(this.b);
    }

    public MultiModeSetting q() {
        return this.b;
    }

    @EventTarget
    public void a(RemovalsEvent event) {
        switch (AnonymousClass1.a[event.b().ordinal()]) {
            case 1:
                event.a(this.b.a("Тряска при уроне").c().booleanValue());
                break;
            case 2:
                event.a(this.b.a("Скорбоард").c().booleanValue());
                break;
            case 3:
                event.a(this.b.a("Боссбар").c().booleanValue());
                break;
            case 4:
                event.a(this.b.a("Эффект портала").c().booleanValue());
                break;
            case 5:
                event.a(this.b.a("Огонь").c().booleanValue());
                break;
            case 6:
                event.a(this.b.a("Обрезка камеры").c().booleanValue());
                break;
            case 7:
                event.a(this.b.a("Частицы разрушения").c().booleanValue());
                break;
            case 8:
                event.a(this.b.a("Погружение воды/лавы").c().booleanValue());
                break;
            case 9:
                event.a(this.b.a("Тошнота").c().booleanValue());
                break;
            case 10:
                event.a(this.b.a("Слепота").c().booleanValue());
                break;
            case 11:
                event.a(this.b.a("Тыква").c().booleanValue());
                break;
            case 12:
                event.a(this.b.a("Частицы погоды").c().booleanValue());
                break;
            case 13:
                event.a(this.b.a("Свечение").c().booleanValue());
                break;
            case InterfaceC0020Opcode.L:
                event.a(this.b.a("Тьма").c().booleanValue());
                break;
            case 15:
                event.a(this.b.a("Чёрные сердца").c().booleanValue());
                break;
        }
    }

    static class AnonymousClass1 {
        static final int[] a = new int[RemovalsEvent.type.values().length];

        static {
            try {
                a[RemovalsEvent.type.HURT_CAM.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                a[RemovalsEvent.type.SCOREBOARD.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                a[RemovalsEvent.type.BOSS_BAR.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                a[RemovalsEvent.type.PORTAL.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                a[RemovalsEvent.type.FIRE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                a[RemovalsEvent.type.CLIP.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                a[RemovalsEvent.type.BREAK_PARTICLES.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                a[RemovalsEvent.type.WATER.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                a[RemovalsEvent.type.NAUSEA.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                a[RemovalsEvent.type.BLINDNESS.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                a[RemovalsEvent.type.PUMPKIN.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                a[RemovalsEvent.type.WEATHER.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                a[RemovalsEvent.type.GLOW.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                a[RemovalsEvent.type.DARKNESS.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                a[RemovalsEvent.type.BLACK_HEARTS.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
        }
    }
}
