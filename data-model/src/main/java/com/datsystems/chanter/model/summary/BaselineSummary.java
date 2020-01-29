package com.datsystems.chanter.model.summary;

public class BaselineSummary {
	private String guid;
	private String name;
	private int reqCount;
	
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getReqCount() {
		return reqCount;
	}
	public void setReqCount(int reqCount) {
		this.reqCount = reqCount;
	}
}
