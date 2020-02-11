package pizza;

import core.IProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pizza.model.Pizza;

import java.util.ArrayList;
import java.util.List;

public class PizzaProcessor implements IProcessor {

    Logger logger = LoggerFactory.getLogger(PizzaProcessor.class);

    @Override
    public Object process(Object obj) {
        Pizza pizza = (Pizza) obj;
        ArrayList<List<Integer>> permutations = new ArrayList<>();
        heapPermutation(pizza.getPizzasSlices(), pizza.getPizzasSlices().size(), permutations);

        int maxSlices = 0;
        List<Integer> maxPremonition = new ArrayList<>();
        for (List<Integer> premonition : permutations) {
            int sum = 0;
            for (int i = 0; i < premonition.size(); i++) {
                Integer integer = premonition.get(i);
                sum += integer;
                if (sum == pizza.getMaxSlices()) {
                    return premonition.subList(0, i + 1);
                } else if (sum < pizza.getMaxSlices() && sum > maxSlices) {
                    maxSlices = sum;
                    maxPremonition = premonition.subList(0, i + 1);
                } else if (sum > pizza.getMaxSlices()) {
                    break;
                }
            }
        }

        return maxPremonition;
    }

    //Generating permutation using Heap Algorithm
    void heapPermutation(List<Integer> a, int size, List<List<Integer>> permutations) {
        // if size becomes 1 then prints the obtained
        // permutation
        if (size == 1) {
            permutations.add(new ArrayList<>(a));
        }

        for (int i = 0; i < size; i++) {
            heapPermutation(a, size - 1, permutations);

            // if size is odd, swap first and last
            // element
            if (size % 2 == 1) {
                int temp = a.get(0);
                a.set(0, a.get(size - 1));
                a.set(size - 1, temp);
            }

            // If size is even, swap ith and last
            // element
            else {
                int temp = a.get(i);
                a.set(i, a.get(size - 1));
                a.set(size - 1, temp);
            }
        }
    }
}
