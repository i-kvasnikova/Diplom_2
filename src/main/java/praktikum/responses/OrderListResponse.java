package praktikum.responses;

import lombok.NoArgsConstructor;
import praktikum.models.Order;

import java.util.ArrayList;

@NoArgsConstructor
public class OrderListResponse extends StellarburgersGeneralResponse {
    public ArrayList<Order> orders;
    public int total;
    public int totalToday;
}
