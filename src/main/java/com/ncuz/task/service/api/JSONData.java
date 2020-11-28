package com.ncuz.task.service.api;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject; 

public class JSONData { 
	private JSONObject main; 
	private JSONObject processing;
	private JSONArray data; 
	private String value;
	private String lastheader="";
	boolean isfound = false; 
	int totalrecord;
	
	public JSONArray getData() {
		return data;
	}
	public void setData(JSONArray value) {
		this.data = value;
	}  
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}  
	
	public JSONData(JSONObject json){ 
		 this.setMain(json);
	 }
	
	 public String FindData(String... keys){
		 return FindData(0,keys);
	 }
	 
	 public String FindData(int index,String... keys){
		 boolean b=false;
		 
		 processing=this.getMain();
		 for(int i=0;i<keys.length;i++){
			 //System.out.println("FindData : "+i+" | "+keys.length+" | "+keys[i]);
			 b=iterateJson(keys[i],index,i);
			 this.setLastheader(keys[i]);
			 isfound=false; 
			 //System.out.println("---------------------------------------- : "+b);
			 if ( !b ) break; 
		 }
		return this.getValue();
	 }
	 
	 @SuppressWarnings("unchecked")
	private boolean iterateJson(String parent,int index,int level){ 
		 //System.out.println("iterateJson :"+processing.keySet()+" | "+this.getMain().keySet().size());
			Iterator<String> it=processing.keySet().iterator();
			while(it.hasNext()){
       		String keys=it.next();
       		isfound=definedData(processing,parent,keys,index);
       		if (isfound) break;
			}
			return isfound;
	 }
	 
	 private boolean definedData(JSONObject obj,String parent,String keys,int index){
		 boolean isObject=false;
		 boolean isArray=false; 
		 JSONObject innerdata;
		 String innervalue = null;
		 
		 if(this.lastheader.equals(parent)){ 
			 
			 if(keys.equals(parent)){
					//System.out.println("INNERDATA FOUND");
					isfound =true; 
				}
		 }else{
			 if(keys.equals(parent)){
					//System.out.println("INNERDATA FOUND");
					isfound =true; 
				}  
		 }
		 
		 
		if((obj.get(keys)) instanceof JSONObject) 
			isObject=true; 
		if((obj.get(keys)) instanceof JSONArray) 
			isArray=true; 
		
		if(isObject){ 
			  innerdata=(JSONObject)obj.get(keys); 
			//System.out.println("Object :"+isfound+" | "+keys+" | "+innerdata.toString());
			if(!isfound)
				iterateInnerData(innerdata,parent,index);
			else {
				processing=innerdata;
				//if (index>totalrecord) value=null;else
				value=innerdata.toString();
				return isfound;
			}
		}else if(isArray){ 
			JSONArray innerarray=(JSONArray)obj.get(keys);
			//System.out.println("Array :"+isfound+" | "+keys+" | "+innerarray.toString());
			if(!isfound){
				iterateInnerArray(innerarray,parent,index,false);
			//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			}
			else {
				iterateInnerArray(innerarray,parent,index,true);
				//if (index>totalrecord) value=null;else
				//data=innerarray;
				value=innerarray.toString();
				return isfound;
			}
		}else { 
			if(obj.get(keys)!=null)
				innervalue=obj.get(keys).toString();
			//System.out.println("Value :"+isfound+" | "+keys+" | "+innervalue+" | "+obj.get(keys));
			if(isfound) {
				if (index>totalrecord) value=null;else
				value=innervalue;
				return isfound;
			}
			
		}
		
		
		
		return isfound;
		
		
	 }
	 
	 @SuppressWarnings("unchecked")
	private boolean iterateInnerData(JSONObject data,String parent,int index){
		 boolean isfound = false;
		 //System.out.println(" ");
			//System.out.println("**********************************************************");
			//System.out.println("iterateInnerData :"+data.keySet()+" | "+data.keySet().size());
			Iterator<String> it=data.keySet().iterator();
	        	while(it.hasNext()){
	        		String keys=it.next();
	        		isfound =definedData(data,parent,keys,index); 
	        		if (isfound) { 
	        			break;
	        		}
	        	}
				return isfound;
	 }
	 
	 @SuppressWarnings("rawtypes")
	private boolean iterateInnerArray(JSONArray data,String parent,int index,boolean found){
		 int i=0;
		// System.out.println(" ");
		//	System.out.println("####################################################");
			if(this.data==null)this.data=data;
			//System.out.println("iterateInnerArray :"+data.size()+" | "+this.data.size()+" } data :"+data.toString());
			Iterator  ar=this.data.iterator();
	        	while(ar.hasNext()){
	        		Object oj=ar.next();
	        		//System.out.println("RECORD ARRAY :"+i+" | "+(index-1)+" | "+isfound+" | found = "+found);
	        		if(oj instanceof JSONObject){
	        			JSONObject jr=(JSONObject) oj;
	        			if(index>data.size()){
	        				this.setValue(null);
		        		}else
	        			if(i==(index-1)){
	        				isfound=true;
	        				//System.out.println("PPPPPPPPPPPPPPP");
	        				definedRecord(jr,parent,index);
	        			}
	        			//System.out.println("RECORD : "+jr.toString());
	        			if(found){
	        				this.data=data;
	        				totalrecord=data.size();
	        				processing=jr;
	        			return isfound;
	        			}
	        			
	        		}
	        		i++;
	        		if (isfound) { 
	        			break;
	        		}
	        		 
	    			 
	    			
	        	}
				return isfound;
	 }
	 
	 @SuppressWarnings("unchecked")
	private boolean definedRecord(JSONObject data,String parent,int index){ 
		 Iterator<String> it=data.keySet().iterator();
			while(it.hasNext()){
				String keys=it.next();
				isfound=definedData(data,parent,keys,index);
				
				 if (isfound) { 
					 
					 break;
				 }
			}
			return isfound;
	 }
	 
	 

	public JSONObject getMain() {
		return main;
	}

	public void setMain(JSONObject main) {
		this.main = main;
	}
	public String getLastheader() {
		return lastheader;
	}
	public void setLastheader(String lastheader) {
		this.lastheader = lastheader;
	} 

}
