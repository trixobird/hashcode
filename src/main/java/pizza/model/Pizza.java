package pizza.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@NoArgsConstructor
@Data
@Accessors(chain = true)
public class Pizza {
    private int maxSlices;
    private List<Integer> pizzasSlices;
}
