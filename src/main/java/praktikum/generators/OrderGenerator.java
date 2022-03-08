package praktikum.generators;

import praktikum.clients.OrderClient;
import praktikum.responses.OrderListResponse;

import java.util.ArrayList;

public class OrderGenerator {
    private OrderGenerator() { throw new IllegalStateException("Utility class"); }

    public static ArrayList<String> getIngredients() throws Exception {
        OrderListResponse mappedCreateResponse = new OrderClient().getAll().getBody().as(OrderListResponse.class);
        if (mappedCreateResponse.orders.isEmpty()) {
            throw new Exception("Недостаточно данных для генерации списка ингредиентов");
        }
        return mappedCreateResponse.orders.get(0).getIngredients();
    }
}
