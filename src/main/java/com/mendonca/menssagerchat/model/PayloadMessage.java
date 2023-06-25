package com.mendonca.menssagerchat.model;

import java.io.Serializable;

public class PayloadMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3978666978247625899L;

	private String sender; // quem mandou
	
	private String addressee; // quem vai receber
	
	private String messageText;

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



}
