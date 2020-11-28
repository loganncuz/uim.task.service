package com.ncuz.task.service.api;

public interface RestProperty {
	public void setParams(String key, String value);
	public void setHeaders(String key, String value);
	public void setInput(String key, Object value);
	public void clearInput();
	public void clearParams();
	public void clearHeaders(); 
}
