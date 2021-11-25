package app.seven.flexisafses.jobs.setup;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Component
public class JobSchedulerService {

    private final Scheduler scheduler;

    public JobSchedulerService(@Autowired Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void schedule(Class<? extends Job> jobClass, JobTimerInfo info){
        JobDetail jobDetail = JobBuilderUtils.buildJobDetails(jobClass, info);
        Trigger trigger = JobBuilderUtils.buildTrigger(jobClass, info);

        try{
            scheduler.scheduleJob(jobDetail, trigger);
        }catch (SchedulerException err){
            log.debug(err.getMessage(), err);
        }
    }

    @PostConstruct
    public void initScheduler() {
        try{
            scheduler.start();
        }catch (SchedulerException err){
            log.debug(err.getMessage(), err);
        }
    }

    @PreDestroy
    public void closeScheduler() {
        try{
            scheduler.shutdown();
        }catch (SchedulerException err){
            log.debug(err.getMessage(), err);
        }
    }
}
