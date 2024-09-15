package argonath.random;

import java.util.concurrent.ThreadLocalRandom;

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

    public static Short getShort(Short origin, Short bound) {
        Short r = (short) ThreadLocalRandom.current().nextInt(origin, bound);
        return r;
    }

    public static Byte getByte(Byte origin, Byte bound) {
        Byte r = (byte) ThreadLocalRandom.current().nextInt(origin, bound);
        return r;
    }

    public static Character getCharacter(Character origin, Character bound) {
        Character r = (char) ThreadLocalRandom.current().nextInt(origin, bound);
        return r;
    }

    public static Long getLong(Long origin, Long bound) {
        Long r = ThreadLocalRandom.current().nextLong(origin, bound);
        return r;
    }

    public static Double getDouble(Double origin, Double bound) {
        Double r = ThreadLocalRandom.current().nextDouble(origin, bound);
        return r;
    }

    public static Float getFloat(Float origin, Float bound) {
        Float r = ThreadLocalRandom.current().nextFloat(origin, bound);
        return r;
    }

    public static Double getGaussian() {
        double d = ThreadLocalRandom.current().nextGaussian();
        return d;
    }

    public static boolean flipCoin() {
        return RandomNumber.getInteger(0, 2) == 0;
    }


}
