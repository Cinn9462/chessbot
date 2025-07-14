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
     * 
     * @param time_spent Time spent by player (in miliseconds)
     * @return If play has run out of time, return false. Otherwise return true.
     */
    public boolean updateClock(long time_spent) {
        if (time_spent > delayLeft) {
            delayLeft = 0;
            return updateActualClock(time_spent - delayLeft);
        } else {
            delayLeft -= time_spent;
            return true;
        }
    }

    /**
     * Called when delayLeft is out of time
     * @param time_spent Time spent by player (in miliseconds)
     * @return If player has ran out of time, return false. Otherwise return true. 
     */
    private boolean updateActualClock(long time_spent) {
        time -= time_spent;

        if (time <= 0) {
            time = 0;
            System.out.println("Player has run out of time");
            return false;
        }

        return true;
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
