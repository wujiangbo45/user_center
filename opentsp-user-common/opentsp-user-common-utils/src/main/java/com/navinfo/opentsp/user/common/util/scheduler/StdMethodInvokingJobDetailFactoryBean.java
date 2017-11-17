package com.navinfo.opentsp.user.common.util.scheduler;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.*;
import org.springframework.beans.support.ArgumentConvertingMethodInvoker;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.MethodInvoker;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/**
 * 
 * rewrite ArgumentConvertingMethodInvoker, add execute history
 * 
 * @author wupeng
 *
 */
public class StdMethodInvokingJobDetailFactoryBean extends
		ArgumentConvertingMethodInvoker implements FactoryBean<JobDetail>,
		BeanNameAware, BeanClassLoaderAware, BeanFactoryAware, InitializingBean {

	private static Class<?> jobDetailImplClass;

	private static Method setResultMethod;

	static {
		try {
			jobDetailImplClass = ClassUtils.forName(
					"org.quartz.impl.JobDetailImpl",
					MethodInvokingJobDetailFactoryBean.class.getClassLoader());
		} catch (ClassNotFoundException ex) {
			jobDetailImplClass = null;
		}
		try {
			Class<?> jobExecutionContextClass = ClassUtils.forName(
					"org.quartz.JobExecutionContext",
					MethodInvokingJobDetailFactoryBean.class.getClassLoader());
			setResultMethod = jobExecutionContextClass.getMethod("setResult",
					Object.class);
		} catch (Exception ex) {
			throw new IllegalStateException("Incompatible Quartz API: " + ex);
		}
	}

	private String name;

	private String group = Scheduler.DEFAULT_GROUP;

	private boolean concurrent = true;

	private String targetBeanName;

	private String[] jobListenerNames;

	private String beanName;

	private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

	private BeanFactory beanFactory;

	private JobDetail jobDetail;

    	private String cron;

	public String getCron() {
	    return cron;
	}

	public void setCron(String cron) {
	    this.cron = cron;
	}

    /**
	 * Set the name of the job.
	 * <p>
	 * Default is the bean name of this FactoryBean.
	 * 
	 * @see org.quartz.JobDetail#setName
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Set the group of the job.
	 * <p>
	 * Default is the default group of the Scheduler.
	 * 
	 * @see org.quartz.JobDetail#setGroup
	 * @see org.quartz.Scheduler#DEFAULT_GROUP
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * Specify whether or not multiple jobs should be run in a concurrent
	 * fashion. The behavior when one does not want concurrent jobs to be
	 * executed is realized through adding the {@link org.quartz.StatefulJob} interface.
	 * More information on stateful versus stateless jobs can be found <a href=
	 * "http://www.quartz-scheduler.org/documentation/quartz-2.1.x/tutorials/tutorial-lesson-03"
	 * >here</a>.
	 * <p>
	 * The default setting is to run jobs concurrently.
	 */
	public void setConcurrent(boolean concurrent) {
		this.concurrent = concurrent;
	}

	/**
	 * Set the name of the target bean in the Spring BeanFactory.
	 * <p>
	 * This is an alternative to specifying {@link #setTargetObject
	 * "targetObject"}, allowing for non-singleton beans to be invoked. Note
	 * that specified "targetObject" and {@link #setTargetClass "targetClass"}
	 * values will override the corresponding effect of this "targetBeanName"
	 * setting (i.e. statically pre-define the bean type or even the bean
	 * object).
	 */
	public void setTargetBeanName(String targetBeanName) {
		this.targetBeanName = targetBeanName;
	}

	/**
	 * Set a list of JobListener names for this job, referring to non-global
	 * JobListeners registered with the Scheduler.
	 * <p>
	 * A JobListener name always refers to the name returned by the JobListener
	 * implementation.
	 * 
	 * @see org.springframework.scheduling.quartz.SchedulerFactoryBean#setJobListeners
	 * @see org.quartz.JobListener#getName
	 * @deprecated as of Spring 4.0, since it only works on Quartz 1.x
	 */
	@Deprecated
	public void setJobListenerNames(String... names) {
		this.jobListenerNames = names;
	}

	@Override
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.beanClassLoader = classLoader;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	@Override
	protected Class<?> resolveClassName(String className)
			throws ClassNotFoundException {
		return ClassUtils.forName(className, this.beanClassLoader);
	}

	@Override
	public void afterPropertiesSet() throws ClassNotFoundException,
			NoSuchMethodException {
		prepare();

		String name = (this.name != null ? this.name : this.beanName);

		Class<?> jobClass = (this.concurrent ? MethodInvokingJob.class : StatefulMethodInvokingJob.class);

		this.jobDetail = (JobDetail) BeanUtils.instantiate(jobDetailImplClass);
		
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(this.jobDetail);
		bw.setPropertyValue("name", name);
		bw.setPropertyValue("group", this.group);
		bw.setPropertyValue("jobClass", jobClass);
		bw.setPropertyValue("durability", true);
		((JobDataMap) bw.getPropertyValue("jobDataMap")).put("methodInvoker",this);

		postProcessJobDetail(this.jobDetail);
	}

	/**
	 * Callback for post-processing the JobDetail to be exposed by this
	 * FactoryBean.
	 * <p>
	 * The default implementation is empty. Can be overridden in subclasses.
	 * 
	 * @param jobDetail
	 *            the JobDetail prepared by this FactoryBean
	 */
	protected void postProcessJobDetail(JobDetail jobDetail) {
	}

	/**
	 * Overridden to support the {@link #setTargetBeanName "targetBeanName"}
	 * feature.
	 */
	@Override
	public Class<?> getTargetClass() {
		Class<?> targetClass = super.getTargetClass();
		if (targetClass == null && this.targetBeanName != null) {
			Assert.state(this.beanFactory != null,
					"BeanFactory must be set when using 'targetBeanName'");
			targetClass = this.beanFactory.getType(this.targetBeanName);
		}
		return targetClass;
	}

	/**
	 * Overridden to support the {@link #setTargetBeanName "targetBeanName"}
	 * feature.
	 */
	@Override
	public Object getTargetObject() {
		Object targetObject = super.getTargetObject();
		if (targetObject == null && this.targetBeanName != null) {
			Assert.state(this.beanFactory != null,
					"BeanFactory must be set when using 'targetBeanName'");
			targetObject = this.beanFactory.getBean(this.targetBeanName);
		}
		return targetObject;
	}

	@Override
	public JobDetail getObject() {
		return this.jobDetail;
	}

	@Override
	public Class<? extends JobDetail> getObjectType() {
		return (this.jobDetail != null ? this.jobDetail.getClass()
				: JobDetail.class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	/**
	 * Quartz Job implementation that invokes a specified method. Automatically
	 * applied by MethodInvokingJobDetailFactoryBean.
	 */
	public static class MethodInvokingJob extends QuartzJobBean {

		protected static final Logger logger = LoggerFactory.getLogger(MethodInvokingJob.class);

		private MethodInvoker methodInvoker;
		
		private String jobId = null;
		
		private Method setResultMethod = null;

		public String getJobId() {
			return jobId;
		}

		public void setJobId(String jobId) {
			this.jobId = jobId;
		}

		/**
		 * Set the MethodInvoker to use.
		 */
		public void setMethodInvoker(MethodInvoker methodInvoker) {
			this.methodInvoker = methodInvoker;
		}

		/**
		 * Invoke the method via the MethodInvoker.
		 */
		@Override
		protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
			if(methodInvoker == null){
				throw new NullPointerException("method invoker can not be null !!");
			}
			
			try {
				Object targetObject = methodInvoker.getTargetObject();
				@SuppressWarnings("rawtypes")
				Class targetClass = targetObject.getClass();
				
				if(targetObject instanceof Job){
					((Job)targetObject).execute(context);
				} else {
					Method method = ReflectionUtils.findMethod(targetClass, "execute", JobExecutionContext.class);
					if(method != null){
						ReflectionUtils.invokeMethod(method, targetObject, context);
					} else {
						methodInvoker.invoke();
					}
				}
				
				
				
				Object obj = context.getResult();
				if(setResultMethod != null)
					ReflectionUtils.invokeMethod(setResultMethod, context, this.methodInvoker.invoke());
			} catch (InvocationTargetException | IllegalAccessException e) {
				logger.error(e.getMessage(), e);
				e.printStackTrace();
			}
			
		/*	try {
				ReflectionUtils.invokeMethod(setResultMethod, context,
						this.methodInvoker.invoke());
			} catch (InvocationTargetException ex) {
				if (ex.getTargetException() instanceof JobExecutionException) {
					// -> JobExecutionException, to be logged at info level by
					// Quartz
					throw (JobExecutionException) ex.getTargetException();
				} else {
					// -> "unhandled exception", to be logged at error level by
					// Quartz
					throw new JobMethodInvocationFailedException(
							this.methodInvoker, ex.getTargetException());
				}
			} catch (Exception ex) {
				// -> "unhandled exception", to be logged at error level by
				// Quartz
				throw new JobMethodInvocationFailedException(
						this.methodInvoker, ex);
			}*/
		}
	}

	/**
	 * Extension of the MethodInvokingJob, implementing the StatefulJob
	 * interface. Quartz checks whether or not jobs are stateful and if so,
	 * won't let jobs interfere with each other.
	 */
	public static class StatefulMethodInvokingJob extends MethodInvokingJob
			implements StatefulJob {

		// No implementation, just an addition of the tag interface StatefulJob
		// in order to allow stateful method invoking jobs.
	}

}
