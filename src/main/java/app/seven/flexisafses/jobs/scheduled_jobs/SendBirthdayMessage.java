package app.seven.flexisafses.jobs.scheduled_jobs;

import app.seven.flexisafses.config.EmailConfiguration;
import app.seven.flexisafses.config.MemorystoreConfigutration;
import app.seven.flexisafses.external_services.CacheService;
import app.seven.flexisafses.external_services.EmailService;
import app.seven.flexisafses.models.pojo.Student;
import app.seven.flexisafses.services.student.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

@Slf4j
public class SendBirthdayMessage implements Job {
    private final StudentService service;
    private final CacheService cacheService;
    private final EmailService emailService;


    public SendBirthdayMessage(@Autowired StudentService service, @Autowired CacheService cacheService, @Autowired EmailService emailService) {
        this.service = service;
        this.cacheService = cacheService;
        this.emailService = emailService;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<Student> students = service.getDueBirthdays(LocalDate.now());

        for (Student student : students) {
            try{
               Student stud = cacheService.getRegistrationByKey(student.getMatricNumber());

               if(stud == null){
                   emailService.sentHappyBirthDayEmail(student);
                   cacheService.setKey(student);
               }

            }catch (Exception exception){
                log.error(exception.getMessage(), exception);
            }
        }
    }
}
