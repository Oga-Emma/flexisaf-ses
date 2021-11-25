package app.seven.flexisafses.jobs.setup;

public class JobTimerInfo {
    int totalFireCount;
    Boolean isRunForever = false;
    Long repeatIntervalMs;
    Long initialOffsetMs;
    String callbackData ;

    public int getTotalFireCount() {
        return totalFireCount;
    }

    public void setTotalFireCount(int totalFireCount) {
        this.totalFireCount = totalFireCount;
    }

    public Boolean getIsRunForever() {
        return isRunForever;
    }

    public void setIsRunForever(Boolean isRunForever) {
        this.isRunForever = isRunForever;
    }

    public Long getRepeatIntervalMs() {
        return repeatIntervalMs;
    }

    public void setRepeatIntervalMs(Long repeatIntervalMs) {
        this.repeatIntervalMs = repeatIntervalMs;
    }

    public Long getInitialOffsetMs() {
        return initialOffsetMs;
    }

    public void setInitialOffsetMs(Long initialOffsetMs) {
        this.initialOffsetMs = initialOffsetMs;
    }

    public String getCallbackData() {
        return callbackData;
    }

    public void setCallbackData(String callbackData) {
        this.callbackData = callbackData;
    }
}
