package com.panda.maptest.bean;

import com.metaio.sdk.jni.LLACoordinate;

public class Place {
	public Place(){
		super();
	}
	
	public Place(String name, LLACoordinate coordinate) {
		super();
		Name = name;
		this.coordinate = coordinate;
	}

	public Place(String name, LLACoordinate coordinate, boolean isHot) {
		super();
		Name = name;
		this.coordinate = coordinate;
		this.isHot = isHot;
	}
	String Name;
	LLACoordinate coordinate;
	boolean isHot;
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public LLACoordinate getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(LLACoordinate coordinate) {
		this.coordinate = coordinate;
	}
	public boolean isHot() {
		return isHot;
	}
	public void setHot(boolean isHot) {
		this.isHot = isHot;
	}
	
}
