package com.jas.pojo;

public class MailDetails {

	private String recipient;
	private String msgBody;
	private String subject;
	private String attachment;

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getMsgBody() {
		return msgBody;
	}

	public void setMsgBody(String msgBody) {
		msgBody = msgBody
				+ "<footer style='padding:10px 20px; font-size: 0.75em;'><p>Mail sent by <a href='https://github.com/anushibin007/jira-automater'>jira-automater</a> // A <a href='https://github.com/anushibin007'>JAS</a> side project<p></footer>";
		this.msgBody = msgBody;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	@Override
	public String toString() {
		return "EmailDetails [recipient=" + recipient + ", msgBody= <masked>" + ", subject=" + subject + ", attachment="
				+ attachment + "]";
	}

}