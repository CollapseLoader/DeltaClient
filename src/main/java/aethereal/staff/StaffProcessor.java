package aethereal.staff;

import aethereal.api.Compile;
import aethereal.config.ConfigProcessor;
import aethereal.lib.json.JSONArray;
import aethereal.lib.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StaffProcessor extends ConfigProcessor<StaffConstructor> {
    @Override
    @Compile
    protected List<StaffConstructor> a(String json) throws Exception {
        JSONArray jSONArray = new JSONArray(json);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.a(); i++) {
            arrayList.add(new StaffConstructor(jSONArray.j(i).l("name")));
        }
        return arrayList;
    }

    @Override
    @Compile
    protected String a(List<StaffConstructor> data) throws Exception {
        JSONArray jSONArray = new JSONArray();
        for (StaffConstructor staffConstructor : data) {
            JSONObject jSONObject = new JSONObject();
            jSONObject.c("name", staffConstructor.a());
            jSONArray.a(jSONObject);
        }
        return jSONArray.E(2);
    }

    @Override
    protected String b() {
        return "staff.json";
    }

    public List<StaffConstructor> a() {
        return new ArrayList(this.d);
    }

    public void b(String str) {
        if (!d(str)) {
            this.d.add(new StaffConstructor(str));
        }
    }

    public void c(String str) {
        this.d.removeIf(staff -> staff.a().equalsIgnoreCase(str));
    }

    public boolean d(String name) {
        return this.d.stream().anyMatch(staff -> {
            return staff.a().equalsIgnoreCase(name);
        });
    }

    public void f() {
        this.d.clear();
    }
}
