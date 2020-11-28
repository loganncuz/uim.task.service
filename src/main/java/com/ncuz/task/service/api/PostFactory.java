package com.ncuz.task.service.api;

import java.io.File;
import java.util.Map.Entry;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.glassfish.jersey.media.multipart.internal.MultiPartWriter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

 
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.api.client.Client; 
import com.sun.jersey.api.client.ClientResponse;

public class PostFactory { 
	
	private WebResource webResource;
	private Client   client; 
	private RestConfig config;
	private WebResource.Builder headers;  
	private ClientResponse response;
	private int status=0;
	
	private String outstring; 
	
	
	

	public PostFactory(RestConfig cnf){ 
		this.config=cnf; 
		 ClientConfig configs = new DefaultClientConfig();  
		 configs.getClasses().add(MultiPartWriter.class); 
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
		 Response responseData = null ;
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
					if(this.getConfig().getFormDataMultiPart().getBodyParts().size()>0){   
						   
					       /* MultiPart multipartEntity = new FormDataMultiPart()
					        .field("body", playWithDadMetaJson, MediaType.APPLICATION_JSON_TYPE)
					        .bodyPart(filePart);*/
					        
					        MultiPart multipartEntity = this.getConfig().getFormDataMultiPart();
					        
					        response = headers
						            .type(MediaType.MULTIPART_FORM_DATA_TYPE)
						            .post(ClientResponse.class, multipartEntity);
						 
					        System.out.println("FORM DATA:"+response); 
					}else{
						System.out.println("PostFactory this.getConfig().getInput() masuk 0 : "+this.getConfig().getData());
					 
						response = headers.post(ClientResponse.class,this.getConfig().getData());
					}
				}else{
					 System.out.println("PostFactory this.getConfig().getInput() masuk 1");
					response = headers.post(ClientResponse.class,this.getConfig().getInput());
				}
				
				
			} 
			else{
				 System.out.println("PostFactory this.getConfig().getStringData() masuk 2");
				response = headers.post(ClientResponse.class,this.getConfig().getStringData());
			} 
		//} 
		
		 
			 System.out.println("PostFactory status : "+status);
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
