package aethereal.network;

import aethereal.api.Compile;
import aethereal.config.ConfigProcessor;
import aethereal.lib.json.JSONArray;
import aethereal.lib.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AccountProcessor extends ConfigProcessor<AccountConstructor> {
    @Override
    @Compile
    protected List<AccountConstructor> a(String json) throws Exception {
        JSONArray jSONArray = new JSONArray(json);
        ArrayList arrayList = new ArrayList();
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
    @Compile
    protected String a(List<AccountConstructor> data) throws Exception {
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

    @Override
    protected String b() {
        return "accounts.json";
    }
}
