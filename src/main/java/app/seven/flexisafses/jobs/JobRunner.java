package app.seven.flexisafses.jobs;

import app.seven.flexisafses.jobs.scheduled_jobs.SendBirthdayMessage;
import app.seven.flexisafses.jobs.scheduled_jobs.TestJobService;
import app.seven.flexisafses.jobs.setup.JobSchedulerService;
import app.seven.flexisafses.jobs.setup.JobTimerInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobRunner {
    private final JobSchedulerService schedule;

    public JobRunner(@Autowired JobSchedulerService schedule) {
        this.schedule = schedule;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void executeAfterStartup() {
        log.debug("Initializing jobs");
        runHelloWorld();
        runBirthdayJob();
    }

    private void runBirthdayJob(){
        JobTimerInfo timerInfo = new JobTimerInfo();
        timerInfo.setIsRunForever(true);
        timerInfo.setRepeatIntervalMs(10800000L);
        timerInfo.setInitialOffsetMs(10000L);
        timerInfo.setCallbackData("Birthday job");

        schedule.schedule(SendBirthdayMessage.class, timerInfo);
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
