package praktikum.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    ArrayList<String> ingredients;
    String _id;
    String name;
    String status;
    int number;
    String createdAt;
    String updatedAt;
}
