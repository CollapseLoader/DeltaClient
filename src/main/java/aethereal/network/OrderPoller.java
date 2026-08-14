package aethereal.network;

import aethereal.lib.jsoup.Element;
import aethereal.lib.jsoup.JsoupConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderPoller {
    private final List<String> a = new ArrayList<>();
    private final FunPay c;
    private List<OrderModel> b;

    public OrderPoller(FunPay funPay) {
        this.c = Objects.requireNonNull(funPay);
    }

    public List<OrderModel> b() {
        return this.b;
    }

    public void a() {
        List<OrderModel> current = c();
        if (current == null) {
            return;
        }
        List<OrderModel> previous = this.b;
        this.b = current;
        if (previous == null) {
            current.forEach(order -> {
                this.a.add(order.a());
            });
            return;
        }
        if (this.c.d().b() == null) {
            return;
        }
        for (OrderModel order2 : previous) {
            if (current.stream().noneMatch(active -> {
                return active.a().equals(order2.a());
            })) {
                this.c.d().b().stream().filter(model -> {
                    return model.d().equalsIgnoreCase(order2.d());
                }).filter(model2 -> {
                    return model2.f() != null;
                }).findFirst().ifPresent(model3 -> {
                    model3.a((ChatModel.a) null);
                });
            }
        }
        for (OrderModel order3 : current) {
            if (!this.a.contains(order3.a())) {
                this.a.add(order3.a());
                this.c.d().b().stream().filter(model4 -> {
                    return model4.d().equalsIgnoreCase(order3.d());
                }).filter(model5 -> {
                    return model5.f() == null;
                }).findFirst().ifPresent(model6 -> {
                    if (order3.g() < this.c.f().s().c().floatValue()) {
                        this.c.b(order3.a());
                        this.c.b(model6.b(), "❌ Минимальная сумма заказа — " + this.c.f().s().c().intValue() + "кк. Средства возвращены, приносим извинения!");
                    } else {
                        this.c.b(model6.b(), "👋 Здравствуйте! Это бот выдачи валюты от — delta client!\n\n🧸 Для начала выдачи, пожалуйста, укажите ваш игровой никнейм:\n\nℹ️ Если заказ был создан по ошибке, напишите «Отмена» для возврата средств.");
                        model6.a(ChatModel.a.NICKNAME);
                    }
                });
            }
        }
    }

    public OrderModel a(String buyerName) {
        if (this.b == null) {
            return null;
        }
        return this.b.stream().filter(order -> {
            return order.d().equalsIgnoreCase(buyerName);
        }).findFirst().orElse(null);
    }

    private List<OrderModel> c() {
        List<OrderModel> result = new ArrayList<>();
        try {
            JsoupConnection.e response = this.c.a("https://funpay.com/orders/trade");
            for (Element order : response.j().j(".tc-item")) {
                try {
                    String status = Objects.requireNonNull(order.k(".tc-status")).ac();
                    String category = Objects.requireNonNull(order.k(".order-desc .text-muted")).ac();
                    if (status.equalsIgnoreCase("Оплачен") && category.contains("Валюта")) {
                        String id = Objects.requireNonNull(order.k(".tc-order")).ac();
                        String name = Objects.requireNonNull(order.k(".order-desc > div")).ac();
                        String buyerName = Objects.requireNonNull(order.k(".media-user-name > span")).ac();
                        double price = Double.parseDouble(Objects.requireNonNull(order.k(".tc-price")).af().trim());
                        String unit = Objects.requireNonNull(order.k(".tc-price > span")).ac();
                        int count = 1;
                        String[] sections = name.split(",");
                        if (sections.length > 1) {
                            String section = sections[sections.length - 1].trim();
                            if (section.contains("шт.")) {
                                count = Integer.parseInt(section.split("шт.")[0].trim());
                            }
                        }
                        if (Math.abs((price / ((double) count)) - ((double) this.c.f().r().c().floatValue())) <= 0.010000003747573418d) {
                            result.add(new OrderModel(id, category, name, buyerName, price, unit, count));
                        }
                    }
                } catch (Exception e) {
                }
            }
            return result;
        } catch (Exception e2) {
            return null;
        }
    }
}
