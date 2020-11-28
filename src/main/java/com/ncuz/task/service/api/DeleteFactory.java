package com.ncuz.task.service.api;

import java.util.Map.Entry;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

 
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

 

public class DeleteFactory {
	private WebResource webResource;
	private Client   client;
	private RestConfig config;
	private WebResource.Builder headers;
	private ClientResponse response;
	private int status=0;
	
	private String outstring;
	
	
	
	

	public DeleteFactory(RestConfig config){ 
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
		 
			this.headers=webResource.getRequestBuilder();  
			AllocateHeader(); 
			System.out.println("GetFactory this.getConfig().getStringData() : "+this.getConfig().getStringData());
			if(this.getConfig().getStringData()==null){
				System.out.println("GetFactory this.getConfig().getInput() masuk ");
				response = headers.delete(ClientResponse.class);
				outstring=response.getEntity(String.class);
			}else{
				System.out.println("GetFactory this.getConfig().getStringData() masuk ");
				response = headers.delete(ClientResponse.class);
			} 
	 
		 status=response.getStatus();
		
		 JSONParser parser = new JSONParser();
		 
		 System.out.println("GetFactory status : "+status);
		if(status==200)
		try {
			this.getConfig().setOutput((JSONObject) parser.parse(outstring)); 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		System.out.println("GetFactory setOutput :"+this.getConfig().getOutput());
		//outpust =  (JSONArray) parser.parse(output);
		 return  outstring;
	}

	

	private void AllocateHeader() {
		// TODO Auto-generated method stub
		for (Entry<String, String> entry : this.getConfig().getListHeader().entrySet()) {
		    String key = entry.getKey();
		    Object value = entry.getValue(); 
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
