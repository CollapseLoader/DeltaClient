package aethereal.ui.element;


import aethereal.config.ConfigProcessor;
import aethereal.config.ConverterUtil;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.event.ClickEvent;
import aethereal.event.DrawEvent;
import aethereal.lib.json.JSONArray;
import aethereal.lib.json.JSONObject;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.setting.Setting;
import aethereal.util.CursorUtil;
import aethereal.util.MathUtil;
import net.minecraft.client.gui.screen.ChatScreen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DragProcessor extends ConfigProcessor<DragInfo> {
    private final b e = new b();
    private final b f = new b();
    private DragInfo g = null;

    @Override

    protected List<DragInfo> loadConfig(String json) throws Exception {
        JSONArray jSONArray = new JSONArray(json);
        for (int i = 0; i < jSONArray.a(); i++) {
            JSONObject jSONObjectJ = jSONArray.j(i);
            String strL = jSONObjectJ.l("name");
            for (DragInfo dragInfo : e()) {
                if (!(dragInfo instanceof DragInfo)) {
                    throw new ClassCastException();
                }
                DragInfo dragInfo2 = dragInfo;
                if (dragInfo2.getName().equals(strL)) {
                    dragInfo2.setX(jSONObjectJ.f("x"));
                    dragInfo2.setY(jSONObjectJ.f("y"));
                    dragInfo2.setWidth(jSONObjectJ.f("width"));
                    dragInfo2.setHeight(jSONObjectJ.f("height"));
                    if (jSONObjectJ.m("settings")) {
                        dragInfo2.getWidget();
                        JSONObject jSONObjectJ2 = jSONObjectJ.j("settings");
                        for (Setting<?> setting : dragInfo2.getWidget().b()) {
                            if (!(setting instanceof Setting)) {
                                throw new ClassCastException();
                            }
                            Setting<?> setting2 = setting;
                            if (jSONObjectJ2.m(setting2.i())) {
                                ConverterUtil.a(setting2, jSONObjectJ2.a(setting2.i()));
                            }
                        }
                    } else {
                    }
                }
            }
        }
        return new ArrayList<>(e());
    }

    @Override

    protected String saveConfig(List<DragInfo> data) throws Exception {
        JSONArray jSONArray = new JSONArray();
        for (DragInfo dragInfo : data) {
            JSONObject jSONObject = new JSONObject();
            if (!(dragInfo instanceof DragInfo)) {
                throw new ClassCastException();
            }
            DragInfo dragInfo2 = dragInfo;
            jSONObject.c("name", dragInfo2.getName());
            jSONObject.b("x", dragInfo2.getX());
            jSONObject.b("y", dragInfo2.getY());
            jSONObject.b("width", dragInfo2.getWidth());
            jSONObject.b("height", dragInfo2.getHeight());
            dragInfo2.getWidget();
            JSONObject jSONObject2 = new JSONObject();
            for (Setting<?> setting : dragInfo2.getWidget().b()) {
                if (!(setting instanceof Setting)) {
                    throw new ClassCastException();
                }
                Setting<?> setting2 = setting;
                jSONObject2.c(setting2.i(), ConverterUtil.a(setting2));
            }
            jSONObject.c("settings", jSONObject2);
            jSONArray.a(jSONObject);
        }
        return jSONArray.E(2);
    }

    public b getSnapGuideX() {
        return this.e;
    }

    public b getSnapGuideY() {
        return this.f;
    }

    public DragInfo getActiveDragInfo() {
        return this.g;
    }

    @Override
    protected String getConfigFileName() {
        return "drag.json";
    }

    @EventTarget
    public void onClick(ClickEvent event) {
        if (mc.currentScreen instanceof ChatScreen) {
            if (event.b() && event.h() == 0) {
                for (DragInfo dragInfo : e()) {
                    if (dragInfo.getDragStatus() != 2 && MathUtil.a(event.getMouseX(), event.getMouseY(), dragInfo.getClampedX(), dragInfo.getClampedY(), dragInfo.getWidth(), dragInfo.getHeight())) {
                        CursorUtil.a(CursorUtil.a.HAND);
                        this.g = dragInfo;
                        this.g.setOffsetX(event.getMouseX() - ((double) dragInfo.getClampedX()));
                        this.g.setOffsetY(event.getMouseY() - ((double) dragInfo.getClampedY()));
                        break;
                    }
                }
            } else if (event.c() && event.h() == 0) {
                resetDrag();
            } else if (event.d() && this.g != null && event.h() == 0) {
                updateDragPosition((float) (event.getMouseX() - this.g.getOffsetX()), (float) (event.getMouseY() - this.g.getOffsetY()), this.g);
            }
            for (DragInfo dragInfo2 : e()) {
                if (event.h() == 1 && event.b()) {
                    if (MathUtil.a(event.getMouseX(), event.getMouseY(), dragInfo2.getClampedX(), dragInfo2.getClampedY(), dragInfo2.getWidth(), dragInfo2.getHeight())) {
                        dragInfo2.getWidget().a(!dragInfo2.getWidget().g());
                    }
                } else if (event.h() == 0 && event.c() && dragInfo2.getWidget().g()) {
                    for (Element<?> element : dragInfo2.getWidget().c()) {
                        element.onMouseClick(event.getMouseX(), event.getMouseY(), event.h());
                    }
                }
            }
        }
    }

    @EventTarget(a = 4)
    public void onDraw(DrawEvent event) {
        if (event.b()) {
            if (mc.currentScreen instanceof ChatScreen) {
                if (this.g == null && !this.e.a() && !this.f.a()) {
                    resetDrag();
                }
                this.e.a(this.g != null, event.g());
                this.f.a(this.g != null, event.g());
                if (this.e.a() || this.f.a()) {
                    b(event);
                    return;
                }
                return;
            }
            for (DragInfo dragInfo : e()) {
                dragInfo.getWidget().a(false);
            }
            if (this.g != null) {
                resetDrag();
            }
        }
    }

    private void updateDragPosition(float x, float y, DragInfo dragInfo) {
        int status = dragInfo.getDragStatus();
        if (status == 2) {
            this.e.a(null);
            this.f.a(null);
            return;
        }
        boolean onlyY = status == 1;
        if (onlyY) {
            x = dragInfo.getClampedX();
        }
        float x2 = MathUtil.b(x, 0.0f, (mc.getWindow().getFramebufferWidth() / mc.getWindow().calculateScaleFactor(2, mc.forcesUnicodeFont())) - dragInfo.getWidth());
        float y2 = MathUtil.b(y, 0.0f, (mc.getWindow().getFramebufferHeight() / mc.getWindow().calculateScaleFactor(2, mc.forcesUnicodeFont())) - dragInfo.getHeight());
        if (!onlyY) {
            x2 = snapToGuide(a.X, x2, dragInfo);
        } else {
            this.e.a(null);
        }
        float y3 = snapToGuide(a.Y, y2, dragInfo);
        dragInfo.setX(MathUtil.b(x2, 0.0f, (mc.getWindow().getFramebufferWidth() / mc.getWindow().calculateScaleFactor(2, mc.forcesUnicodeFont())) - dragInfo.getWidth()));
        dragInfo.setY(MathUtil.b(y3, 0.0f, (mc.getWindow().getFramebufferHeight() / mc.getWindow().calculateScaleFactor(2, mc.forcesUnicodeFont())) - dragInfo.getHeight()));
    }

    private float snapToGuide(a axis, float pos, DragInfo dragInfo) {
        float f;
        float size = axis.b(dragInfo);
        float[] points = {pos, pos + (size / 2.0f), pos + size};
        b guide = axis == a.X ? this.e : this.f;
        float bestDistance = 25.0f;
        Float bestGuide = null;
        float snappedPos = pos;
        Iterator<Float> it = getGuidePoints(axis, dragInfo).iterator();
        while (it.hasNext()) {
            float guidePos = it.next().floatValue();
            int i = 0;
            while (i < points.length) {
                float distance = Math.abs(points[i] - guidePos);
                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestGuide = Float.valueOf(guidePos);
                    if (i == 0) {
                        f = 0.0f;
                    } else {
                        f = i == 1 ? size / 2.0f : size;
                    }
                    snappedPos = guidePos - f;
                }
                i++;
            }
        }
        if (bestGuide != null && bestDistance < 5.0f) {
            pos = snappedPos;
            guide.a(bestGuide);
        } else {
            guide.a(null);
        }
        return pos;
    }

    private List<Float> getGuidePoints(a axis, DragInfo currentElement) {
        List<Float> guides = new ArrayList<>();
        guides.add(Float.valueOf(0.0f));
        guides.add(Float.valueOf(axis.screenSize() / 2.0f));
        guides.add(Float.valueOf(axis.screenSize()));
        for (DragInfo other : e()) {
            if (other != currentElement && (other.getWidth() != 0.0f || other.getHeight() != 0.0f)) {
                float pos = axis.getPosition(other);
                float size = axis.b(other);
                guides.add(Float.valueOf(pos));
                guides.add(Float.valueOf(pos + (size / 2.0f)));
                guides.add(Float.valueOf(pos + size));
            }
        }
        return guides;
    }

    private void b(DrawEvent event) {
        if (this.e.a()) {
            event.getDraw2DProcessor().a(event.i(), this.e.c().floatValue() - 0.5f, 0.0f, 0.5f, mc.getWindow().getFramebufferHeight() / mc.getWindow().calculateScaleFactor(2, mc.forcesUnicodeFont()), ColorUtil.convertToARGB(255, 255, 255, (int) (this.e.getAnimationUtil().c() * 200.0f)));
        }
        if (this.f.a()) {
            event.getDraw2DProcessor().a(event.i(), 0.0f, this.f.c().floatValue() - 0.5f, mc.getWindow().getFramebufferWidth() / mc.getWindow().calculateScaleFactor(2, mc.forcesUnicodeFont()), 0.5f, ColorUtil.convertToARGB(255, 255, 255, (int) (this.f.getAnimationUtil().c() * 200.0f)));
        }
    }

    private void resetDrag() {
        CursorUtil.a(CursorUtil.a.DEFAULT);
        this.g = null;
        this.f.a(null);
        this.e.a(null);
    }

    enum a {
        X,
        Y;

        float screenSize() {
            return this == X ? Interface.mc.getWindow().getFramebufferWidth() / Interface.mc.getWindow().calculateScaleFactor(2, Interface.mc.forcesUnicodeFont()) : Interface.mc.getWindow().getFramebufferHeight() / Interface.mc.getWindow().calculateScaleFactor(2, Interface.mc.forcesUnicodeFont());
        }

        float getPosition(DragInfo info) {
            return this == X ? info.getClampedX() : info.getClampedY();
        }

        float b(DragInfo info) {
            return this == X ? info.getWidth() : info.getHeight();
        }
    }

    static class b {
        private final AnimationUtil a = new AnimationUtil();
        private Float b = null;
        private boolean c = false;

        b() {
        }

        public AnimationUtil getAnimationUtil() {
            return this.a;
        }

        public Float c() {
            return this.b;
        }

        public boolean d() {
            return this.c;
        }

        void a(Float newPosition) {
            if (newPosition == null) {
                this.c = false;
            } else if (!newPosition.equals(this.b)) {
                this.b = newPosition;
                this.c = true;
            }
        }

        void a(boolean active, float tickDelta) {
            if (this.b != null) {
                boolean should = active && this.c;
                this.a.a(active && this.c);
                this.a.a(0.0f, 1.0f, 0.3f, EasingList.i, tickDelta);
                if (!should && this.a.c() <= 0.0f) {
                    this.b = null;
                }
            }
        }

        boolean a() {
            return this.b != null && this.a.c() > 0.0f;
        }
    }
}
