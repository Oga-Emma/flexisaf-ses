package app.seven.flexisafses.jobs.scheduled_jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
public class TestJobService implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.debug("Running hello world job");
    }
}
