package app.seven.flexisafses.jobs;

import app.seven.flexisafses.jobs.scheduled_jobs.TestJobService;
import app.seven.flexisafses.jobs.setup.JobSchedulerService;
import app.seven.flexisafses.jobs.setup.JobTimerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

public class JobRunner {
    private final JobSchedulerService schedule;

    public JobRunner(@Autowired JobSchedulerService schedule) {
        this.schedule = schedule;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void executeAfterStartup() {
        runHelloWorld();
    }

    private void runHelloWorld(){
        JobTimerInfo timerInfo = new JobTimerInfo();
        timerInfo.setTotalFireCount(2);
        timerInfo.setRepeatIntervalMs(20000L);
        timerInfo.setInitialOffsetMs(10000L);
        timerInfo.setCallbackData("Test hello world job");

        schedule.schedule(TestJobService.class, timerInfo);
    }
}
