-- WTF!
UPDATE qrtz_job_details
SET job_class_name='io.quarkus.quartz.runtime.QuartzSchedulerImpl$InvokerJob'
WHERE job_class_name='io.quarkus.quartz.runtime.QuartzScheduler$InvokerJob';