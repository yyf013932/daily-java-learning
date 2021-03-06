package daniel.yyf.er017.december;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author danielyang
 * @Date 2017/11/21 11:19
 * Scheduler：与scheduler交互的主要API
 * job：通过scheduler执行任务，你的任务类需要实现的接口。
 * JobDetail：定义Job的实例。由于在执行的过程中需要创建多个实例，在构建JobDetail的时候需要有实现了Job接口的类
 * 而不能传入一个对象。参数的传递通过usingJobData实现
 * Trigger：触发Job的执行。
 * JobBuilder：定义和创建JobDetail实例的接口。
 * TriggerBuilder：定义和创建Trigger实例的接口
 */
public class QuartzTest {
    public static void main(String[] args) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            JobDetail jobDetail = newJob(QuartzJob.class).withIdentity("job", "group1")
                    .usingJobData("name", "daniel").build();

            Trigger trigger = newTrigger().withIdentity("trigger", "group1")
                    .startNow()
                    .withSchedule(simpleSchedule())
                    .build();



            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();

            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


