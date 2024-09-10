package argonath.reflector.generator;

public class Sequence {
    int index;

    public Sequence() {
        this.index = 0;
    }


    public int next() {
        return index++;
    }
}
