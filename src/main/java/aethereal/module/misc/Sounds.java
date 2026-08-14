package aethereal.module.misc;

import aethereal.core.Category;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.setting.ModeSetting;
import aethereal.setting.SliderSetting;
import net.minecraft.util.Identifier;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ModuleRegister(name = "Sounds", description = "Воспроизводит выбранные звуки при определённых игровых событиях", category = Category.Misc)
public class Sounds extends Module {
    private final ModeSetting b = new ModeSetting("Звук для воспроизведения", "Тип 1", "Тип 1", "Тип 2", "Тип 3", "Тип 4").a(selected -> {
        a(e(true));
    });
    private final SliderSetting c = new SliderSetting("Громкость воспроизведения", 0.25f, 0.0f, 1.0f, 0.05f);
    private final ExecutorService d = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable, "ClientSound-Thread");
        thread.setDaemon(true);
        return thread;
    });

    public Sounds() {
        a(this.b, this.c);
    }

    public SliderSetting q() {
        return this.c;
    }

    public void d(boolean active) {
        if (m()) {
            a(e(active));
        }
    }

    public void a(String filename) {
        if (filename != null && !filename.isEmpty() && mc.getResourceManager() != null) {
            this.d.execute(() -> {
                try {
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(mc.getResourceManager().open(Identifier.of("delta", "sounds/" + filename))));
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                        FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                        gain.setValue(Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), (float) (20.0d * Math.log10(Math.max(this.c.c().floatValue(), 1.0E-4f))))));
                    }
                    clip.start();
                    clip.addLineListener(event -> {
                        if (event.getType() == LineEvent.Type.STOP) {
                            clip.close();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public String e(boolean active) {
        switch (this.b.c()) {
            case "Тип 1":
                return active ? "enable.wav" : "disable.wav";
            case "Тип 2":
                return active ? "enable1.wav" : "disable1.wav";
            case "Тип 3":
                return active ? "enable2.wav" : "disable2.wav";
            case "Тип 4":
                return active ? "enable3.wav" : "disable3.wav";
            default:
                return null;
        }
    }
}
