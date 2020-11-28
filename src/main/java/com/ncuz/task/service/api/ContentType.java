package com.ncuz.task.service.api;

public enum ContentType {
	JSON,FORM;
	@SuppressWarnings("unused")
	public  static String getString(ContentType type){
		switch (type)
	    {
	    case JSON:return "application/json";
	    case FORM:return "application/x-www-form-urlencoded";
	    }  defalut:return "application/json";
	}
}
