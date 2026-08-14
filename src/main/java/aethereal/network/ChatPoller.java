package aethereal.network;

import aethereal.lib.jsoup.Element;
import aethereal.lib.jsoup.Jsoup;
import aethereal.util.Marker;
import aethereal.util.MathUtil;
import aethereal.util.ProcessIdUtil;
import aethereal.util.ServerUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatPoller {
    private final FunPay b;
    private String a = Integer.toHexString((int) MathUtil.a(0.0f, 2.1474836E9f));
    private List<ChatModel> c;

    public ChatPoller(FunPay funPay) {
        this.b = Objects.requireNonNull(funPay);
    }

    public List<ChatModel> b() {
        return this.c;
    }

    public void a(List<ChatModel> chatModels) {
        this.c = chatModels;
    }

    public void a() {
        try {
            List<ChatModel> current = c();
            if (!current.isEmpty()) {
                List<ChatModel> previous = this.c;
                this.c = current;
                if (previous != null) {
                    for (ChatModel model : current) {
                        ChatModel old = previous.stream().filter(previousModel -> {
                            return previousModel.b().equals(model.b());
                        }).findFirst().orElse(null);
                        long previousRoomMessageId = old != null ? old.c() : 0L;
                        if (old != null) {
                            model.a(old.f());
                            model.a(old.g());
                            model.a(old.a());
                        }
                        if (model.c() > previousRoomMessageId) {
                            for (JsonElement historyElement : JsonParser.parseString(this.b.c(model.b()).k()).getAsJsonObject().getAsJsonArray("objects")) {
                                JsonObject historyObject = historyElement.getAsJsonObject();
                                if ("chat_node".equalsIgnoreCase(historyObject.get("type").getAsString())) {
                                    for (JsonElement chatMessageElement : historyObject.getAsJsonObject("data").getAsJsonArray("messages")) {
                                        JsonObject chatMessage = chatMessageElement.getAsJsonObject();
                                        if (chatMessage.get("id").getAsLong() > previousRoomMessageId && !chatMessage.get("author").getAsString().equals(this.b.c().a()) && !chatMessage.get("author").getAsString().equals("0")) {
                                            String content = Jsoup.a(chatMessage.get("html").getAsString()).j(".chat-msg-text").stream().findFirst().map(Element::c).orElse("");
                                            String answer = content.replaceAll("[^a-zA-Zа-яА-Я]", "").toLowerCase();
                                            OrderModel order = this.b.e().a(model.d());
                                            if (order != null && model.f() == ChatModel.a.NICKNAME && (answer.equals("отмена") || answer.equals("otmena"))) {
                                                model.a((ChatModel.a) null);
                                                this.b.b(order.a());
                                                this.b.b(model.b(), "↩️ Заказ отменён, средства возвращены!");
                                            } else if (model.f() == ChatModel.a.NICKNAME) {
                                                String nickname = content.replaceAll("[^a-zA-Z0-9_]", "");
                                                model.a(nickname);
                                                model.a(ChatModel.a.CONFIRM_NICKNAME);
                                                this.b.b(model.b(), "✅ Отлично! Давайте проверим данные:\n\n📝 Ваш никнейм: " + nickname + "\n📦 Количество: " + (order != null ? order.g() : 0) + " шт.\n\n✔️ Всё верно? Напишите «Да» или «+»\n✏️ Нужно исправить? Напишите «Нет» или «-»");
                                            } else if (model.f() == ChatModel.a.CONFIRM_NICKNAME) {
                                                if (answer.equals("да") || answer.equals("da") || content.contains(Marker.b)) {
                                                    this.b.b(model.b(), "✅ Отлично! Данные подтверждены.\n\n🟢 Для получения валюты зайдите на Анархию: " + ServerUtil.a.d() + "\n\n⚡ У вас есть 20 секунд для получения валюты, иначе потребуется указать никнейм заново.");
                                                    model.a(ChatModel.a.DELIVERY);
                                                    model.a().b();
                                                } else if (answer.equals("нет") || answer.equals("net") || content.contains(ProcessIdUtil.a)) {
                                                    model.a(ChatModel.a.NICKNAME);
                                                    this.b.b(model.b(), "🧸 Пожалуйста, укажите ваш игровой никнейм:");
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private List<ChatModel> c() throws Exception {
        JsonObject chatBookmarks = new JsonObject();
        chatBookmarks.addProperty("type", "chat_bookmarks");
        chatBookmarks.addProperty("id", this.b.c().a());
        chatBookmarks.addProperty("tag", this.a);
        chatBookmarks.addProperty("data", false);
        JsonArray objects = new JsonArray();
        objects.add(chatBookmarks);
        List<ChatModel> result = new ArrayList<>();
        for (JsonElement element : JsonParser.parseString(this.b.a(objects.toString(), String.valueOf(false)).k()).getAsJsonObject().getAsJsonArray("objects")) {
            JsonObject object = element.getAsJsonObject();
            if ("chat_bookmarks".equalsIgnoreCase(object.get("type").getAsString())) {
                this.a = object.get("tag").getAsString();
                for (Element chat : Jsoup.a(object.getAsJsonObject("data").get("html").getAsString()).j(".contact-item")) {
                    result.add(new ChatModel(chat.b_("data-id") ? chat.a_("data-id") : "", chat.b_("data-node-msg") ? Long.parseLong(chat.a_("data-node-msg")) : 0L, chat.j(".media-user-name").c(), chat.j(".contact-item-message").c()));
                }
            }
        }
        return result;
    }
}
