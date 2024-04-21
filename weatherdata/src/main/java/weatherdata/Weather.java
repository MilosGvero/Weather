package weatherdata;

import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;

public class Weather {

	private static final Logger logger = LoggerFactory.getLogger(Weather.class);
	
	public static void main(String[] args) throws Exception {
		logger.info("Main started.");
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = schedulerFactory.getScheduler();
		scheduler.start();
		JobDetail job = JobBuilder.newJob(DataJob.class)
				  .withIdentity("myJob", "group1")
				  .build();
		
		Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "group1")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInMinutes(5)
                        .repeatForever())
                .build();
		
		scheduler.scheduleJob(job, trigger);

        Thread.sleep(Long.MAX_VALUE);
    	logger.info("Main completed.");
	}

	
}
