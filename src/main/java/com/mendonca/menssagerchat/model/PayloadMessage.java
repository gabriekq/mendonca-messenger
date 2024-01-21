package com.mendonca.menssagerchat.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PayloadMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3978666978247625899L;

	private String sender;
	
	private String addressee;
	
	private String messageText;
	
	private String audioData;

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getAddressee() {
		return addressee;
	}

	public void setAddressee(String addressee) {
		this.addressee = addressee;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public String getAudioData() {
		return audioData;
	}

	public void setAudioData(String audioData) {
		this.audioData = audioData;
	}


}
