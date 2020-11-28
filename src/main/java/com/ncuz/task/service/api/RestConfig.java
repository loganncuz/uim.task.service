package com.ncuz.task.service.api;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import javax.ws.rs.core.MultivaluedMap;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sun.jersey.api.representation.Form;
import com.sun.jersey.core.util.MultivaluedMapImpl;

 

public class RestConfig implements RestProperty{
	private ContentType contentType;
	private String serverName;
	private String methodName;
	private JSONObject formData ;
	private MultivaluedMap<String, String>  params;
	private JSONObject output;
	private JSONArray outputArray;
	private  HashMap<String,String> listHeader;
	private Form   data;
	private String extendPath; 
	private boolean isContent;
	private String stringData;
	private String filter; 
	private FormDataMultiPart formDataMultiPart;
	//private static Logger logger =Config.getLogger(RestConfig.class,"file","OTHelper"); 
	
	
	public RestConfig(ContentType ct,String serverName) {
		//prop=new Properties();
		//prop= FileUtil.getOtchProperties();
		 this.setContentType(ct);
		 this.setServerName(serverName);
		 this.init();
	}
	
	private void init(){
		this.setFormData(new JSONObject());
		 this.setParams(new MultivaluedMapImpl());  
		this.setListHeader(new HashMap<String, String>());
		this.setOutput(new JSONObject());  
		this.setData(new Form());
		this.formDataMultiPart = new FormDataMultiPart(); 
		 
		if(contentType==ContentType.FORM){
			//logger.debug("ContentType FORM : "+contenttype);
			this.setHeaders("Content-Type", ContentType.getString(ContentType.FORM));
			this.setContent(true);
		}else{
			//logger.debug("ContentType JSON : "+contenttype);
			this.setHeaders("Content-Type", ContentType.getString(ContentType.JSON)); 
			this.setContent(false);
		}
		
	}
	
	public void setMethod(String method) {
		 
		//prop= FileUtil.getRestOtchCollectionProperties(); 
		//logger.debug("setMethod : "+" | "+method+" | "+this.getServerName()+"."+method+" | ext = "+this.getExtendPath());
		//logger.debug("Properties Method : "+this.getServerName()+"."+method);
		 
		if(this.getExtendPath()==null){ 
			this.methodName = method ;
			//logger.debug("METHOD WITHOUT EXTENDED :"+method);
		}else{
			this.methodName = method+this.getExtendPath() ;
			//logger.debug("METHOD WITH EXTENDED :"+method);
		}
		//logger.debug("REST URL : "+this.getServerName()+this.getMethodName()); 
	}
	
	public String getInput(){
		 StringWriter out = new StringWriter();
		 try {
			formData.writeJSONString(out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  
		 //logger.debug("PostFactory getInput :"+ out.toString());
		 return out.toString();
	}
	
	public String GenerateMapperPostingParams(String... args){
		String strfilter = "";
		String key = null;
		String value = null;
		if(args.length>0){  
			for(int i=0;i<args.length;i++){ 
				if (i % 2 == 1) { 
					value=args[i];
					strfilter=strfilter+key+"="+value+"&";
					key=null;
					value=null; 
		        }
		        else 
					key=args[i]; 
			 }
			strfilter=strfilter.substring(0, strfilter.length()-1);
		}
		//logger.debug("GenerateMapperPostingParams : "+strfilter);
		 return this.setFilter(strfilter);
	}


	public ContentType getContentType() {
		return contentType;
	}


	public void setContentType(ContentType contenttype) {
		this.contentType = contenttype;
	}


	public String getServerName() {
		return serverName;
	}


	public void setServerName(String serverName) {
		this.serverName = serverName;
	}


	public String getMethodName() {
		return methodName;
	}


	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}


	public JSONObject getFormData() {
		return formData;
	}


	public void setFormData(JSONObject formData) {
		this.formData = formData;
	}


	public MultivaluedMap<String, String> getParams() {
		return params;
	}


	public void setParams(MultivaluedMap<String, String> params) {
		this.params = params;
	}


	public JSONObject getOutput() {
		return output;
	}
	public JSONArray getOutputArray() {
		return outputArray;
	}


	public void setOutput(JSONObject output) {
		this.output = output;
	}

	public void setOutputArray(JSONArray output) {
		this.outputArray = output;
	}


	public HashMap<String,String> getListHeader() {
		return listHeader;
	}


	public void setListHeader(HashMap<String,String> listHeader) {
		this.listHeader = listHeader;
	}


	public Form getData() {
		return data;
	}


	public void setData(Form data) {
		this.data = data;
	}

	public String getExtendPath() {
		return extendPath;
	}

	public void setExtendPath(String extendPath) {
		this.extendPath = extendPath;
	}
	
	public void setHeaders(String key, String value) {
		// TODO Auto-generated method stub 
		this.getListHeader().put(key, value); 
		//logger.debug(this.listHeader.values());
	}

	@Override
	public void setParams(String key, String value) {
		// TODO Auto-generated method stub 
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setInput(String key, Object value) {
		// TODO Auto-generated method stub
		if( this.isContent()){ 
			this.getData().add(key, (String) value);
		}else
			this.getFormData().put(key, value);
	}

	@Override
	public void clearInput() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearParams() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearHeaders() {
		// TODO Auto-generated method stub
		
	}

	public boolean isContent() {
		return isContent;
	}

	public void setContent(boolean isContent) {
		this.isContent = isContent;
	}

	public String getStringData() {
		return stringData;
	}

	public void setStringData(String stringData) {
		this.stringData = stringData;
	}

	public String getFilter() {
		return filter;
	}

	public String setFilter(String filter) {
		this.filter = filter;
		return filter;
	}

	 

	public FormDataMultiPart getFormDataMultiPart() {
		return formDataMultiPart;
	}

	public void setFormDataMultiPart(FormDataMultiPart formDataMultiPart) {
		this.formDataMultiPart = formDataMultiPart;
	}

}
