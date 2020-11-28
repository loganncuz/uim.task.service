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

public class PutFactory {
	private WebResource webResource;
	private Client   client;
	private RestConfig config;
	private WebResource.Builder headers;
	private ClientResponse response;
	private int status=0;
	
	private String outstring;
	
	
	
	

	public PutFactory(RestConfig cnf){ 
		this.config=cnf;
		 ClientConfig configs = new DefaultClientConfig(); 
		client =  Client.create(configs); 
		System.out.println("PostFactory Constructor :"+this.config.getServerName()+" | METHOD : "+this.config.getMethodName()+" | CONFIG : "+this.config);
		
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
		/*
		if (this.getConfig().getParams().size()>0){ 
			System.out.println("PostFactory this.getConfig().getInput() masuk param : "+
					this.getConfig().getParams());
			 response=webResource.post(ClientResponse.class,this.getConfig().getParams());
		}else{*/
			this.headers=webResource.getRequestBuilder();  
			AllocateHeader(); 
			//System.out.println("PostFactory this.getConfig().getStringData() : "+this.getConfig().getStringData());
			if(this.getConfig().getStringData()==null){
				if( this.getConfig().isContent()){ 
					 System.out.println("PostFactory this.getConfig().getInput() masuk 0 : "+this.getConfig().getData());
					 
					response = headers.put(ClientResponse.class,this.getConfig().getData());
				}else{
					 System.out.println("PostFactory this.getConfig().getInput() masuk 1");
					response = headers.put(ClientResponse.class,this.getConfig().getInput());
				}
				
				
			}else{
				 System.out.println("PostFactory this.getConfig().getStringData() masuk 2");
				response = headers.put(ClientResponse.class,this.getConfig().getStringData());
			} 
		//} 
		
		  status=response.getStatus();
		
		 JSONParser parser = new JSONParser();
		 
		 System.out.println("PostFactory status : "+status);
		if(status==200)
		try {
			outstring=response.getEntity(String.class);
			this.getConfig().setOutput((JSONObject) parser.parse(outstring)); 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		//System.out.println("PostFactory setOutput :"+this.getConfig().getOutput());
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
