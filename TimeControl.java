// Class is written where time tracks miliseconds

public class TimeControl {
    private long time;
    private long delayLeft;
    private long delay;
    private long increment;
    
    /**
     * @param time Initial time (in seconds)
     * @param delay Delay (in seconds)
     * @param increment Increment (in seconds)
     */
    public TimeControl(long time, long delay, long increment) {
        this.time = time * 1000;
        this.delay = delay * 1000;
        this.increment = increment * 1000;
    }

    /**
     * Create a time control with unlimited time
     */
    public TimeControl() {
        time = Long.MAX_VALUE;
        delay = 0;
        increment = 0;
    }

    /**
     * @param time_spent Time spent by player (in miliseconds)
     */
    public void updateClock(long time_spent) {
        if (time_spent > delayLeft) {
            delayLeft = 0;
            updateActualClock(time_spent - delayLeft);
        } else {
            delayLeft -= time_spent;
        }
    }

    /**
     * Called when delayLeft is out of time
     * @param time_spent Time spent by player (in miliseconds)
     */
    private void updateActualClock(long time_spent) {
        time -= time_spent;

        if (time < 0) {
            time = 0;
        }
    }

    /**
     * @return Time left on player clock in miliseconds
     */
    public long getTime() {
        return time;
    }

    /**
     * @return Time left on player delay in miliseconds
     */
    public long getDelay() {
        return delayLeft;
    }
    
    /**
     * Resets delay to initial delay
     */
    public void resetDelay() {
        delayLeft = delay;
    }

    /**
     * Increments time by increment at the end of a turn
     */
    public void inc() {
        time += increment;
    }
}
