package cn.joymates.infoin.domain;

import com.google.common.collect.ListMultimap;

public class School {
	private String name;
	
	private String address;
	
	private String[] grade;
	
	private ListMultimap<String, String> classes;
	
	private String[] termInfo;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String[] getGrade() {
		return grade;
	}

	public void setGrade(String[] grade) {
		this.grade = grade;
	}

	public ListMultimap<String, String> getClasses() {
		return classes;
	}

	public void setClasses(ListMultimap<String, String> classes) {
		this.classes = classes;
	}

	public String[] getTermInfo() {
		return termInfo;
	}

	public void setTermInfo(String[] termInfo) {
		this.termInfo = termInfo;
	}
	
	
}
