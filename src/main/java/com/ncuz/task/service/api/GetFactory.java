package com.ncuz.task.service.api;

import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

 
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class GetFactory {
	
	private WebResource webResource;
	private Client   client;
	private RestConfig config;
	private WebResource.Builder headers;
	private ClientResponse response;
	private int status=0;
	
	private String outstring;
	
	
	
	

	public GetFactory(RestConfig config){ 
		this.config=config;
		 ClientConfig configs = new DefaultClientConfig();
		client =  Client.create(configs);
		
		System.out.println("GetFactory :"+this.config.getServerName()+""+
				this.config.getMethodName());
		
	}

	public WebResource getWebResource() {
		return webResource;
	}

	public void setWebResource(WebResource webResource) {
		this.webResource = webResource;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public RestConfig getConfig() {
		return config;
	}

	public void setConfig(RestConfig config) {
		this.config = config;
	}
	
	 
	
	
	public String accept(){
		webResource=client.resource(this.config.getServerName().trim()).path(this.config.getMethodName().trim()); 
		if(config.getParams()!=null){
//			System.out.println("QUERY PARAMS ACCEPT : "+config.getParams());
			this.headers=webResource.queryParams(config.getParams()).getRequestBuilder();
		}
		else{
			this.headers=webResource.getRequestBuilder(); 
		}
		 
		AllocateHeader();  
		response = this.headers.get(ClientResponse.class); 
		
		 status=response.getStatus();
		
		 JSONParser parser = new JSONParser();
		 
		 System.out.println("GetFactory status : "+status);
		//System.out.println("GetFactory outstring : "+outstring);
		if(status==200)
		try {
			outstring=response.getEntity(String.class);
			//System.out.println("GetFactory outstring : "+outstring);
			this.getConfig().setOutput((JSONObject) parser.parse(outstring)); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				//System.out.println("GetFactory outstring : "+outstring);
				this.getConfig().setOutputArray((JSONArray) parser.parse(outstring));
			} catch (ParseException parseException) {
				parseException.printStackTrace();
			}
//			/e.printStackTrace();
		}
		 
		//System.out.println("GetFactory getOutput :"+this.getConfig().getOutput());
		//outpust =  (JSONArray) parser.parse(output);
		 return  outstring;
	}

	

	private void AllocateHeader() {
		// TODO Auto-generated method stub
		for (Entry<String, String> entry : this.getConfig().getListHeader().entrySet()) {
		    String key = entry.getKey();
		    Object value = entry.getValue();
		    System.out.println("AllocateHeader : "+key+" | "+value);
		    this.headers.header(key, value);
		}
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
