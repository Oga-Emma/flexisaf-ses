package app.seven.flexisafses.jobs.setup;

import org.quartz.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class JobSchedulerService {

    Scheduler scheduler;

    public void schedule(Class<? extends Job> jobClass, JobTimerInfo info){
        JobDetail jobDetail = JobBuilderUtils.buildJobDetails(jobClass, info);
        Trigger trigger = JobBuilderUtils.buildTrigger(jobClass, info);

        try{
            scheduler.scheduleJob(jobDetail, trigger);
        }catch (SchedulerException err){

        }
    }

    @PostConstruct
    public void initScheduler() {
        try{
            scheduler.start();
        }catch (SchedulerException err){

        }
    }

    @PreDestroy
    public void closeScheduler() {
        try{
            scheduler.shutdown();
        }catch (SchedulerException e){

        }
    }
}
