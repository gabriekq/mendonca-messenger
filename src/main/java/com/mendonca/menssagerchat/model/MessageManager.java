package com.mendonca.menssagerchat.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;

public class MessageManager implements Runnable {

	@Autowired
	private Connection connection;

	private ArrayList<PayloadMessage> payloadMessages;

	private String userName;

	private PayloadMessage payloadMessageSend;

	private Session session;

	public MessageManager(String userName) {
		super();
		this.userName = userName;
		this.payloadMessages = new ArrayList<>();
	}

	@PostConstruct
	public void initSession() {
		try {
			session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
			Destination destination = session.createQueue(this.userName);
			MessageConsumer consumer = session.createConsumer(destination);
			consumer.setMessageListener(new MessageListener() {

				@Override
				public void onMessage(Message message) {

					synchronized (payloadMessages) {
						try {

							if (message instanceof ObjectMessage) {
								ObjectMessage objectMessage = (ObjectMessage) message;
								PayloadMessage messagePayloadMessage = (PayloadMessage) objectMessage.getObject();
								payloadMessages.add(messagePayloadMessage);
								message.acknowledge();
								payloadMessages.notifyAll();
							}

						} catch (JMSException  interruptedException) {
							System.err.println(interruptedException.toString());
						}
					}

				}
			});

		} catch (JMSException exception) {
			System.err.println(exception.toString());
		}
	}

	@Override
	public void run() {
		sendMenssage();
	}

	private void sendMenssage() {
		try {
			Destination destination = session.createQueue(this.payloadMessageSend.getAddressee());
			MessageProducer producer = session.createProducer(destination);
			ObjectMessage objectMessage = session.createObjectMessage();
			objectMessage.setObject(this.payloadMessageSend);
			producer.send(objectMessage);

		} catch (JMSException exception) {
			System.err.println(exception.toString());

		}
	}

	public void setPayloadMessageSend(PayloadMessage payloadMessageSend) {
		this.payloadMessageSend = payloadMessageSend;
	}

	public List<PayloadMessage> getMessages() {

		synchronized (this.payloadMessages) {

			while (true) {

				if (!this.payloadMessages.isEmpty()) {
					ArrayList<PayloadMessage> payloadMessagesReturn = new ArrayList<>(this.payloadMessages);
					this.payloadMessages.clear();
					return payloadMessagesReturn;
				} else {

					try {
						this.payloadMessages.wait();
					} catch (InterruptedException interruptedException) {
						System.err.println(interruptedException.toString());
						continue;
					}

				}

			}

		}

	}

}
