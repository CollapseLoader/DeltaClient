package aethereal.network;


import aethereal.config.ConfigProcessor;
import aethereal.lib.json.JSONArray;
import aethereal.lib.json.JSONObject;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class AccountProcessor extends ConfigProcessor<AccountConstructor> {
    @Override

    protected List<AccountConstructor> loadConfig(String json) throws Exception {
        JSONArray jSONArray = new JSONArray(json);
        ArrayList<AccountConstructor> arrayList = new ArrayList<>();
        for (int i = 0; i < jSONArray.a(); i++) {
            JSONObject jSONObjectJ = jSONArray.j(i);
            AccountConstructor accountConstructor = new AccountConstructor(jSONObjectJ.l("name"));
            accountConstructor.a(jSONObjectJ.q("selected"));
            accountConstructor.b(jSONObjectJ.q("favorited"));
            arrayList.add(accountConstructor);
        }
        return arrayList;
    }

    @Override

    protected String saveConfig(List<AccountConstructor> data) throws Exception {
        JSONArray jSONArray = new JSONArray();
        for (AccountConstructor accountConstructor : data) {
            JSONObject jSONObject = new JSONObject();
            if (!(accountConstructor instanceof AccountConstructor)) {
                throw new ClassCastException();
            }
            AccountConstructor accountConstructor2 = accountConstructor;
            jSONObject.c("name", accountConstructor2.b());
            jSONObject.b("selected", accountConstructor2.c());
            jSONObject.b("favorited", accountConstructor2.d());
            jSONArray.a(jSONObject);
        }
        return jSONArray.E(2);
    }

    public AccountConstructor a() {
        return this.d.stream().filter((v0) -> {
            return v0.c();
        }).findFirst().orElse(null);
    }

    public void save() {
        try {
            File file = new File(d(), getConfigFileName());
            file.getParentFile().mkdirs();
            Files.writeString(file.toPath(), saveConfig(this.d));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getConfigFileName() {
        return "accounts.json";
    }
}
