public class OperationCounter {
    private long push;
    private long pop;
    private long comparison;

    public void push() { push++; }
    public void pop() { pop++; }
    public void comparison() { comparison++; }

    public long getPush() { return push; }
    public long getPop() { return pop; }
    public long getComparison() { return comparison; }

    public void reset() {
        push = 0;
        pop = 0;
        comparison = 0;
    }
}
