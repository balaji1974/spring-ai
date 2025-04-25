package com.bala.springboot.ai.ai_vector_db.model;

import java.util.Optional;

public class Passenger {
	private Integer passengerId;
	private Integer survived;
	private Integer pclass;
	private String name;
	private String sex;
	private Double age;
	private Integer sibsp;
	private Integer parch;
	private String ticket;
	private Double fare;
	private String cabin;
	private String embarked;
	private Double wikiid;
	private String nameWiki;
	private Double ageWiki;
	private String hometown;
	private String boarded;
	private String destination;
	private String lifeboat;
	private String body;
	private Integer sclass;
	
	public Passenger(Integer passengerId, Integer survived, Integer pclass, String name, String sex, Double age,
			Integer sibsp, Integer parch, String ticket, Double fare, String cabin, String embarked, Double wikiid,
			String nameWiki, Double ageWiki, String hometown, String boarded, String destination, String lifeboat,
			String body, Integer sclass) {
		super();
		this.passengerId = passengerId;
		this.survived = survived;
		this.pclass = pclass;
		this.name = name;
		this.sex = sex;
		this.age = age;
		this.sibsp = sibsp;
		this.parch = parch;
		this.ticket = ticket;
		this.fare = fare;
		this.cabin = cabin;
		this.embarked = embarked;
		this.wikiid = wikiid;
		this.nameWiki = nameWiki;
		this.ageWiki = ageWiki;
		this.hometown = hometown;
		this.boarded = boarded;
		this.destination = destination;
		this.lifeboat = lifeboat;
		this.body = body;
		this.sclass = sclass;
	}
	public Integer getPassengerId() {
		return passengerId;
	}
	public void setPassengerId(Integer passengerId) {
		this.passengerId = passengerId;
	}
	public Integer getSurvived() {
		return survived;
	}
	public void setSurvived(Integer survived) {
		this.survived = survived;
	}
	public Integer getPclass() {
		return pclass;
	}
	public void setPclass(Integer pclass) {
		this.pclass = pclass;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Double getAge() {
		return age;
	}
	public void setAge(Double age) {
		this.age = age;
	}
	public Integer getSibsp() {
		return sibsp;
	}
	public void setSibsp(Integer sibsp) {
		this.sibsp = sibsp;
	}
	public Integer getParch() {
		return parch;
	}
	public void setParch(Integer parch) {
		this.parch = parch;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public Double getFare() {
		return fare;
	}
	public void setFare(Double fare) {
		this.fare = fare;
	}
	public String getCabin() {
		return cabin;
	}
	public String getNotNullCabin() {
		return Optional.ofNullable(this.cabin).orElse("");
	}
	
	public void setCabin(String cabin) {
		this.cabin = cabin;
	}
	public String getEmbarked() {
		return embarked;
	}
	public String getNotNullEmbarked() {
		return Optional.ofNullable(this.embarked).orElse("");
	}
	
	public void setEmbarked(String embarked) {
		this.embarked = embarked;
	}
	public Double getWikiid() {
		return wikiid;
	}
	public void setWikiid(Double wikiid) {
		this.wikiid = wikiid;
	}
	public String getNameWiki() {
		return nameWiki;
	}
	public String getNotNullNameWiki() {
		return Optional.ofNullable(this.nameWiki).orElse("");
	}
	public void setNameWiki(String nameWiki) {
		this.nameWiki = nameWiki;
	}
	public Double getAgeWiki() {
		return ageWiki;
	}
	public void setAgeWiki(Double ageWiki) {
		this.ageWiki = ageWiki;
	}
	public String getHometown() {
		return hometown;
	}
	public String getNotNullHometown() {
		return Optional.ofNullable(this.hometown).orElse("");
	}
	public void setHometown(String hometown) {
		this.hometown = hometown;
	}
	public String getBoarded() {
		return boarded;
	}
	public String getNotNullBoarded() {
		return Optional.ofNullable(this.boarded).orElse("");
	}
	public void setBoarded(String boarded) {
		this.boarded = boarded;
	}
	public String getDestination() {
		return destination;
	}
	public String getNotNullDestination() {
		return Optional.ofNullable(this.destination).orElse("");
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getLifeboat() {
		return lifeboat;
	}
	public String getNotNullLifeboat() {
		return Optional.ofNullable(this.lifeboat).orElse("");
	}
	public void setLifeboat(String lifeboat) {
		this.lifeboat = lifeboat;
	}
	public String getBody() {
		return body;
	}
	public String getNotNullBody() {
		return Optional.ofNullable(this.body).orElse("");
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Integer getSclass() {
		return sclass;
	}
	public void setSclass(Integer sclass) {
		this.sclass = sclass;
	}
	@Override
	public String toString() {
		return "passengerId=" + passengerId + ", survived=" + survived + ", pclass=" + pclass + ", name="
				+ name + ", sex=" + sex + ", age=" + age + ", sibsp=" + sibsp + ", parch=" + parch + ", ticket="
				+ ticket + ", fare=" + fare + ", cabin=" + cabin + ", embarked=" + embarked + ", wikiid=" + wikiid
				+ ", nameWiki=" + nameWiki + ", ageWiki=" + ageWiki + ", hometown=" + hometown + ", boarded=" + boarded
				+ ", destination=" + destination + ", lifeboat=" + lifeboat + ", body=" + body + ", sclass=" + sclass;
	}
	
}

