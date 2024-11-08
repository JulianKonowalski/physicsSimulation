package App.Util;

public class Timer {
    public Timer() { mLastTimestamp = 0; }
    public Timer(long initialTimestamp) { mLastTimestamp = initialTimestamp; }
    public void start() { mLastTimestamp = System.nanoTime(); }
    public long getElapsedTime() { return System.nanoTime() - mLastTimestamp; }
    public long getElapsedTimeAndReset() {
        long timeDifference = getElapsedTime();
        this.start();
        return timeDifference;
    }

    private long mLastTimestamp;
}
