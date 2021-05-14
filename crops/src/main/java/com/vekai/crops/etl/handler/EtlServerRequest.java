package com.vekai.crops.etl.handler;

import com.alibaba.fastjson.JSONObject;
import com.vekai.crops.common.micro.MicroServerName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

@Component
public class EtlServerRequest {
	private static Logger logger = LoggerFactory.getLogger(EtlServerRequest.class);

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/deleteJob", method = {RequestMethod.GET, RequestMethod.POST})
	public JSONObject deleteJob(String jobId, String jobGroup){
		HttpEntity request = jobReqEntity(jobId, jobGroup);
		JSONObject rst = restTemplate.postForObject(MicroServerName.etl_service_http + "/schedule/deleteJob", request, JSONObject.class);
		return rst;
	}

    @PostMapping(value = "/saveOrUpdate")
    public JSONObject saveOrupdate(String jobId){
        HttpEntity request = jobReqEntity(jobId, null);
        JSONObject rst = restTemplate.postForObject(MicroServerName.etl_service_http + "/schedule/saveOrUpdate", request, JSONObject.class);
        return rst;
    }

	private HttpEntity jobReqEntity(String jobId, String jobGroup){
		JSONObject paramMap = new JSONObject();
		paramMap.put("jobId", jobId);
		paramMap.put("jobGroup", jobGroup);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity request = new HttpEntity(paramMap.toJSONString(), headers);

		return request;
	}

	
}
