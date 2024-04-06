package cn.shilight.myapplication.message;

import java.io.Serializable;

public class MessagObtian implements Serializable {

	private static final long serialVersionUID = 7471391170055841173L;


	private String forUid;
	private String fromUid;
	private int messageType;


	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	private String content ;
	final static int Tap = 0;
	final static int Text = 1;



	public String getForUid() {
		return forUid;
	}

	public void setForUid(String forUid) {
		this.forUid = forUid;
	}

	public String getFromUid() {
		return fromUid;
	}

	public void setFromUid(String fromUid) {
		this.fromUid = fromUid;
	}
	
	
	public MessagObtian(String forUid,String fromUid,int messageType) {
		// TODO Auto-generated method stub
		this.forUid = forUid;
		this.fromUid = fromUid;
		this.messageType = messageType;
		
	}
	
	
	public String getContent() {
		return content;
	}


	public MessagObtian setContent(String content) {
		this.content = content;
		
		return this;
		
	}
	
	
	

}
