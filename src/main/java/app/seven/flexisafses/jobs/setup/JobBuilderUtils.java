package app.seven.flexisafses.jobs.setup;

import org.quartz.*;

import java.util.Date;

public class JobBuilderUtils {
    static JobDetail buildJobDetails(Class<? extends Job> jobClass, JobTimerInfo info)  {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(jobClass.getSimpleName(), info);
        return JobBuilder.newJob(jobClass)
                .withIdentity(jobClass.getSimpleName())
                .setJobData(jobDataMap)
                .build();
    }

    static Trigger buildTrigger(Class<? extends Job> jobClass, JobTimerInfo info)  {
        var builder = SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(info.repeatIntervalMs);
            if (info.isRunForever) {
                builder = builder.repeatForever();
        } else {
                builder = builder.withRepeatCount(info.totalFireCount - 1);
        }
        return TriggerBuilder.newTrigger()
                .withIdentity(jobClass.getSimpleName())
                .withSchedule(builder)
                .startAt(new Date(System.currentTimeMillis() + info.initialOffsetMs))
                .build();
    }
}
