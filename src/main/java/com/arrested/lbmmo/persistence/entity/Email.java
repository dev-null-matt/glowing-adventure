package com.arrested.lbmmo.persistence.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table
public class Email {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="email_seq_gen")
	@SequenceGenerator(name="email_seq_gen", sequenceName="EMAIL_ID")
	private long id;
	
	@ManyToOne
	@JoinColumn(name="RECIPIENT")
	private User recipient;
	
	private String subject;
	
	private String body;
	
	private Date queuedDate;
	
	private Date sentDate;

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	public User getRecipient() {
		return recipient;
	}

	public void setRecipient(User recipient) {
		this.recipient = recipient;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Date getQueuedDate() {
		return queuedDate;
	}

	public void setQueuedDate(Date queuedDate) {
		this.queuedDate = queuedDate;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	public long getId() {
		return id;
	}
}
