package daniel.yyf.er017.december;

import org.quartz.*;

/**
 * @author danielyang
 * @Date 2017/11/21 13:18
 */
public class QuartzJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDetail detail = jobExecutionContext.getJobDetail();
        JobDataMap jobDataMap = detail.getJobDataMap();
        System.out.println("=================================");
        System.out.println("start to execute");
        System.out.println("parameters:");
        jobDataMap.keySet().forEach(e -> {
            System.out.println(e + ":" + jobDataMap.get(e));
        });
        System.out.println("end execute");
        System.out.println("=================================");
    }
}
