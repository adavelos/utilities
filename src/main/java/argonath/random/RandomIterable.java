package argonath.random;

public class RandomIterable {
    public static byte[] randomByteArray(int minSize, int maxSize) {
        int size = RandomNumber.getInteger(minSize, maxSize);
        byte[] ret = new byte[size];
        for (int i = 0; i < size; i++) {
            ret[i] = RandomNumber.getByte(Byte.MIN_VALUE, Byte.MAX_VALUE);
        }
        return ret;
    }
}
