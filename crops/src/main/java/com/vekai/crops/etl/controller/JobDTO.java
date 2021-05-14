package com.vekai.crops.etl.controller;

import java.io.Serializable;

public class JobDTO implements Serializable,Cloneable{
		private String jobId;
		private String jobGroup;

		public String getJobId() {
			return jobId;
		}

		public void setJobId(String jobId) {
			this.jobId = jobId;
		}

		public String getJobGroup() {
			return jobGroup;
		}

		public void setJobGroup(String jobGroup) {
			this.jobGroup = jobGroup;
		}

		@Override
		public String toString() {
			return "JobDTO{" +
					"jobId='" + jobId + '\'' +
					", jobGroup='" + jobGroup + '\'' +
					'}';
		}
	}