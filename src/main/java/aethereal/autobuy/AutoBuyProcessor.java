package aethereal.autobuy;

import aethereal.api.Compile;
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
    @Compile
    protected List<AutoBuyEntry> a(String str) {
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
                if (aVar.b().equals(strL)) {
                    if (jSONObjectJ.m("status")) {
                        aVar.a(jSONObjectJ.b("status"));
                    }
                    if (jSONObjectJ.m("price")) {
                        aVar.a(jSONObjectJ.e("price"));
                    }
                }
            }
        }
        return new ArrayList(this.d);
    }

    @Override
    @Compile
    protected String a(List<AutoBuyEntry> data) {
        JSONArray jSONArray = new JSONArray();
        for (AutoBuyEntry aVar : data) {
            JSONObject jSONObject = new JSONObject();
            if (!(aVar instanceof AutoBuyEntry)) {
                throw new ClassCastException();
            }
            AutoBuyEntry aVar2 = aVar;
            jSONObject.c("name", aVar2.b());
            jSONObject.b("status", aVar2.l());
            jSONObject.b("price", aVar2.k());
            jSONArray.a(jSONObject);
        }
        return jSONArray.E(2);
    }

    @Override
    protected String b() {
        return "autobuy.json";
    }
}
