package aethereal.network;

import aethereal.discord.Session;
import aethereal.lib.jsoup.Document;
import aethereal.lib.jsoup.Element;
import aethereal.lib.jsoup.Jsoup;
import aethereal.lib.jsoup.JsoupConnection;
import aethereal.module.misc.FunDeliver;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Map;

public class FunPay {
    private final Session a = new Session();
    private final ChatPoller b = new ChatPoller(this);
    private final OrderPoller c = new OrderPoller(this);
    private final FunDeliver d;

    public FunPay(FunDeliver deliver) {
        this.d = deliver;
    }

    public Session c() {
        return this.a;
    }

    public ChatPoller d() {
        return this.b;
    }

    public OrderPoller e() {
        return this.c;
    }

    public FunDeliver f() {
        return this.d;
    }

    public boolean a() {
        if (this.a.a() != null) {
            return true;
        }
        try {
            JsoupConnection.e response = Jsoup.b("https://funpay.com/").c("golden_key", this.d.q().c()).a(5000).e();
            Map<String, String> cookies = response.cookies();
            Document document = response.j();
            Element usernameElement = document.k(".user-link-name");
            if (usernameElement == null) {
                return false;
            }
            JsonObject appData = JsonParser.parseString(document.g().a_("data-app-data")).getAsJsonObject();
            this.a.a(appData.get("userId").getAsString());
            this.a.e(appData.get("csrf-token").getAsString());
            this.a.c(cookies.get("PHPSESSID"));
            this.a.d(cookies.get("golden_seal"));
            this.a.b(usernameElement.ac());
            this.b.a(null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void b() {
        this.b.a();
        this.c.a();
    }

    public JsoupConnection.e a(String objectsJson, String requestJson) throws Exception {
        return Jsoup.b("https://funpay.com/runner/")
                .c(Map.of("golden_key", this.d.q().c(), "PHPSESSID", this.a.c(), "golden_seal", this.a.d()))
                .b(Map.of("accept", "*/*", "content-type", "application/x-www-form-urlencoded; charset=UTF-8",
                        "x-requested-with", "XMLHttpRequest"))
                .a(JsoupConnection.c.POST)
                .a(Map.of("objects", objectsJson == null ? "" : objectsJson, "request",
                        requestJson == null ? "" : requestJson, "csrf_token", this.a.e() == null ? "" : this.a.e()))
                .c(true).a(5000).e();
    }

    public JsoupConnection.e a(String url) throws Exception {
        return Jsoup.b(url).c(Map.of("golden_key", this.d.q().c(), "PHPSESSID", this.a.c(), "golden_seal", this.a.d()))
                .b(Map.of("accept", "*/*", "x-requested-with", "XMLHttpRequest")).a(JsoupConnection.c.POST).c(true).a(5000)
                .e();
    }

    public void b(String orderId) {
        try {
            Element form = Jsoup
                    .b("https://funpay.com/orders/" + (orderId.startsWith("#") ? orderId.substring(1) : orderId) + "/")
                    .c(Map.of("golden_key", this.d.q().c(), "PHPSESSID", this.a.c(), "golden_seal", this.a.d())).a(5000)
                    .c().k(".modal-refund form");
            if (form == null) {
                return;
            }
            JsoupConnection refund = Jsoup
                    .b(form.a_("action").startsWith("http") ? form.a_("action")
                            : "https://funpay.com" + form.a_("action"))
                    .c(Map.of("golden_key", this.d.q().c(), "PHPSESSID", this.a.c(), "golden_seal", this.a.d()))
                    .a(JsoupConnection.c.POST).c(true).a(5000);
            form.select("input[type=hidden]").forEach(node -> {
                refund.a(node.attr("name"), node.attr("value"));
            });
            refund.a("csrf_token", this.a.e()).e();
        } catch (Exception e) {
        }
    }

    public JsoupConnection.e c(String nodeId) throws Exception {
        JsonObject objects = new JsonObject();
        objects.addProperty("type", "chat_node");
        objects.addProperty("id", nodeId);
        objects.addProperty("tag", "00000000");
        JsonObject data = new JsonObject();
        data.addProperty("node", nodeId);
        data.addProperty("last_message", -1);
        data.addProperty("content", "");
        objects.add("data", data);
        JsonArray objectsArray = new JsonArray();
        objectsArray.add(objects);
        return a(objectsArray.toString(), String.valueOf(false));
    }

    public void b(String nodeId, String content) {
        try {
            JsonObject request = new JsonObject();
            request.addProperty("action", "chat_message");
            JsonObject data = new JsonObject();
            data.addProperty("node", nodeId);
            data.addProperty("last_message", -1);
            data.addProperty("content", content);
            request.add("data", data);
            JsonObject objects = new JsonObject();
            objects.addProperty("type", "chat_node");
            objects.addProperty("id", nodeId);
            objects.addProperty("tag", "00000000");
            JsonObject objData = new JsonObject();
            objData.addProperty("node", nodeId);
            objData.addProperty("last_message", -1);
            objData.addProperty("content", "");
            objects.add("data", objData);
            JsonArray objectsArray = new JsonArray();
            objectsArray.add(objects);
            a(objectsArray.toString(), request.toString());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
