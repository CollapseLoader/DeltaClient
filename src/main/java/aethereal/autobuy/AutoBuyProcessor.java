package aethereal.autobuy;


import aethereal.config.ConfigProcessor;
import aethereal.lib.json.JSONArray;
import aethereal.lib.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AutoBuyProcessor extends ConfigProcessor<AutoBuyEntry> {
    public AutoBuyProcessor() {
        this.d.addAll(Arrays.asList(AutoBuyEntry.values()));
    }

    @Override

    protected List<AutoBuyEntry> loadConfig(String str) {
        if (this.d.isEmpty()) {
            this.d.addAll(Arrays.asList(AutoBuyEntry.values()));
        }
        JSONArray jSONArray = new JSONArray(str);
        for (int i = 0; i < jSONArray.a(); i++) {
            JSONObject jSONObjectJ = jSONArray.j(i);
            String strL = jSONObjectJ.l("name");
            for (Object obj : this.d) {
                if (!(obj instanceof AutoBuyEntry aVar)) {
                    throw new ClassCastException();
                }
                if (aVar.getDisplayName().equals(strL)) {
                    if (jSONObjectJ.m("status")) {
                        aVar.setActive(jSONObjectJ.b("status"));
                    }
                    if (jSONObjectJ.m("price")) {
                        aVar.setPrice(jSONObjectJ.e("price"));
                    }
                }
            }
        }
        return new ArrayList<>(this.d);
    }

    @Override

    protected String saveConfig(List<AutoBuyEntry> data) {
        JSONArray jSONArray = new JSONArray();
        for (AutoBuyEntry aVar : data) {
            JSONObject jSONObject = new JSONObject();
            if (!(aVar instanceof AutoBuyEntry)) {
                throw new ClassCastException();
            }
            AutoBuyEntry aVar2 = aVar;
            jSONObject.c("name", aVar2.getDisplayName());
            jSONObject.b("status", aVar2.l());
            jSONObject.b("price", aVar2.k());
            jSONArray.a(jSONObject);
        }
        return jSONArray.E(2);
    }

    @Override
    protected String getConfigFileName() {
        return "autobuy.json";
    }
}
