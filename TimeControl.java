// Class is written where time tracks miliseconds

public class TimeControl {
    private long time;
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
     * @return If player has ran out of time, return false. Otherwise return true. 
     */
    public boolean updateClock(long time_spent) {
        time -= Math.max(0, time_spent - delay);
        
        if (time <= 0) {
            time = 0;
            return false;
        }
        
        time += increment;

        return true;
    }

    /**
     * @return Time left on player clock in miliseconds
     */
    public long getTime() {
        return time;
    }
}
