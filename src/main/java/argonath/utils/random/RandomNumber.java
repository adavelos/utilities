package argonath.utils.random;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomNumber {

    /**
     * Returns an integer in the range: [origin, bound)
     * The class uses a {@link ThreadLocalRandom}. The {@link java.util.Random} class although thread safe, can cause contention if multiple
     * parallel threads use it.
     *
     * @param origin the lower bound (inclusive)
     * @param bound  the upper bound (exclusive)
     * @return a random integer
     */
    public static Integer getInteger(int origin, int bound) {
        int r = ThreadLocalRandom.current().nextInt(origin, bound);
        return r;
    }

    public static Long getLong(Long origin, Long bound) {
        Long r = ThreadLocalRandom.current().nextLong(origin, bound);
        return r;
    }

    public static Double getGaussian() {
        double d = ThreadLocalRandom.current().nextGaussian();
        return d;
    }

    public static boolean throwDice() {
        return RandomNumber.getInteger(0, 2) == 0;
    }

    public static List<Integer> split(Integer total, int size) {
        List<Integer> splitPoints =
                IntStream.rangeClosed(1, total - 1)
                        .boxed().collect(Collectors.toList());
        Collections.shuffle(splitPoints);
        splitPoints.subList(size, splitPoints.size()).clear();
        Collections.sort(splitPoints);
        List<Integer> results = new ArrayList<>();
        int cur = 0;
        for (Integer sp : splitPoints) {
            results.add(sp - cur);
            cur = sp;
        }
        results.add(total - cur);
        return results;
    }

    public static List<BigDecimal> split(BigDecimal total, int size) {
        int moves = 0;
        while (total.intValue() < 100 * size) {
            total = total.movePointRight(1);
            moves++;
        }
        int totalInt = total.intValue();
        BigDecimal remainder = total.subtract(BigDecimal.valueOf(totalInt));
        List<Integer> splitPoints =
                IntStream.rangeClosed(1, totalInt - 1)
                        .boxed().collect(Collectors.toList());
        Collections.shuffle(splitPoints);
        splitPoints.subList(size, splitPoints.size()).clear();
        Collections.sort(splitPoints);
        List<BigDecimal> results = new ArrayList<>();
        int cur = 0;
        for (Integer sp : splitPoints) {
            results.add(BigDecimal.valueOf(sp - cur).movePointLeft(moves));
            cur = sp;
        }
        results.add(BigDecimal.valueOf(totalInt - cur).add(remainder).movePointLeft(moves));
        return results;
    }

}
