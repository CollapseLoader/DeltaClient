package aethereal.friend;


import aethereal.config.ConfigProcessor;
import aethereal.core.EventTarget;
import aethereal.event.BackendEvent;
import aethereal.lib.json.JSONArray;
import aethereal.lib.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FriendProcessor extends ConfigProcessor<FriendConstructor> {
    @Override

    protected List<FriendConstructor> loadConfig(String json) throws Exception {
        JSONArray jSONArray = new JSONArray(json);
        ArrayList<FriendConstructor> arrayList = new ArrayList<>();
        for (int i = 0; i < jSONArray.a(); i++) {
            arrayList.add(new FriendConstructor(jSONArray.j(i).l("name")));
        }
        return arrayList;
    }

    @Override

    protected String saveConfig(List<FriendConstructor> data) throws Exception {
        JSONArray jSONArray = new JSONArray();
        for (FriendConstructor friendConstructor : data) {
            JSONObject jSONObject = new JSONObject();
            jSONObject.c("name", friendConstructor.a());
            jSONArray.a(jSONObject);
        }
        return jSONArray.E(2);
    }

    @Override
    protected String getConfigFileName() {
        return "friends.json";
    }

    @EventTarget
    public void onBackend(BackendEvent event) {
        String minecraft;
        if (event.isReceive() && "friend".equals(event.getPacket().getId())) {
            String payload = event.getPacket().getPayload();
            if ("rename".equals(event.getPacket().getSecurity().extractString(payload, "type")) && (minecraft = event.getPacket().getSecurity().extractString(payload, "minecraft")) != null) {
                b(minecraft);
                unSetup();
            }
        }
    }

    public List<FriendConstructor> a() {
        return new ArrayList<>(this.d);
    }

    public void b(String str) {
        if (!d(str)) {
            this.d.add(new FriendConstructor(str));
        }
    }

    public void c(String str) {
        this.d.removeIf(friend -> friend.a().equalsIgnoreCase(str));
    }

    public boolean d(String name) {
        return this.d.stream().anyMatch(friend -> {
            return friend.a().equalsIgnoreCase(name);
        });
    }

    public void f() {
        this.d.clear();
    }
}
