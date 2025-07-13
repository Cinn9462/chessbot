public class TimeControl {
    private long time;
    private long delay;
    private long increment;
    public TimeControl(long t, long d, long i) {
        time = t;
        delay = d;
        increment = i;
    }

    public TimeControl() {
        time = Long.MAX_VALUE;
        delay = Long.MAX_VALUE;
        increment = 0;
    }

    public void start() {
        return;
    }

    public void end() {
        return;
    }
}
