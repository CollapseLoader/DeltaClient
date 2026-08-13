package aethereal.macro;

import aethereal.api.Compile;
import aethereal.config.ConfigProcessor;
import aethereal.core.Delta;
import aethereal.core.EventTarget;
import aethereal.event.KeyEvent;
import aethereal.lib.json.JSONArray;
import aethereal.lib.json.JSONObject;
import aethereal.util.KeyUtil;

import java.util.ArrayList;
import java.util.List;

public class MacrosProcessor extends ConfigProcessor<MacrosConstructor> {
    @Override
    @Compile
    protected List<MacrosConstructor> a(String json) throws Exception {
        JSONArray jSONArray = new JSONArray(json);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.a(); i++) {
            JSONObject jSONObjectJ = jSONArray.j(i);
            arrayList.add(new MacrosConstructor(jSONObjectJ.l("key"), jSONObjectJ.l("command")));
        }
        return arrayList;
    }

    @Override
    @Compile
    protected String a(List<MacrosConstructor> data) throws Exception {
        JSONArray jSONArray = new JSONArray();
        for (MacrosConstructor macrosConstructor : data) {
            JSONObject jSONObject = new JSONObject();
            if (!(macrosConstructor instanceof MacrosConstructor)) {
                throw new ClassCastException();
            }
            MacrosConstructor macrosConstructor2 = macrosConstructor;
            jSONObject.c("key", macrosConstructor2.a());
            jSONObject.c("command", macrosConstructor2.b());
            jSONArray.a(jSONObject);
        }
        return jSONArray.E(2);
    }

    @Override
    protected String b() {
        return "macros.json";
    }

    @EventTarget
    public void a(KeyEvent event) {
        if (event.d() == 1 && aM_.currentScreen == null) {
            for (MacrosConstructor constructor : Delta.h().d().d().e()) {
                if (KeyUtil.a(event.b()) == KeyUtil.a(constructor.a())) {
                    aM_.player.networkHandler.sendChatMessage(constructor.b());
                }
            }
        }
    }

    public List<MacrosConstructor> a() {
        return new ArrayList(this.d);
    }

    public void a(String str, String str2) {
        this.d.add(new MacrosConstructor(str, str2));
    }

    public void b(String str) {
        this.d.removeIf(macro -> macro.a().equals(str));
    }

    public void f() {
        this.d.clear();
    }
}
