package praktikum.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@ToString
@AllArgsConstructor
public class OrdersList {
    ArrayList<Order> orders;
    int total;
    int totalToday;
}
