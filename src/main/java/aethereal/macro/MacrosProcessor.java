package aethereal.macro;


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

    protected List<MacrosConstructor> loadConfig(String json) throws Exception {
        JSONArray jSONArray = new JSONArray(json);
        ArrayList<MacrosConstructor> arrayList = new ArrayList<>();
        for (int i = 0; i < jSONArray.a(); i++) {
            JSONObject jSONObjectJ = jSONArray.j(i);
            arrayList.add(new MacrosConstructor(jSONObjectJ.l("key"), jSONObjectJ.l("command")));
        }
        return arrayList;
    }

    @Override

    protected String saveConfig(List<MacrosConstructor> data) throws Exception {
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
    protected String getConfigFileName() {
        return "macros.json";
    }

    @EventTarget
    public void a(KeyEvent event) {
        if (event.getAction() == 1 && mc.currentScreen == null) {
            for (MacrosConstructor constructor : Delta.getInstance().getModuleProcessor().d().e()) {
                if (KeyUtil.a(event.getKey()) == KeyUtil.a(constructor.a())) {
                    mc.player.networkHandler.sendChatMessage(constructor.b());
                }
            }
        }
    }

    public List<MacrosConstructor> a() {
        return new ArrayList<>(this.d);
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
