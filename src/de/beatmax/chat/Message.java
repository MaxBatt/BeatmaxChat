package de.beatmax.chat;

public class Message {
	
	public Message(String chatWith, String author, String message) {
		this.chatWith = chatWith;
		this.author = author;
		this.message = message;
	}

	private String chatWith, author, message;

	public String getChatWith() {
		return chatWith;
	}

	public void setChatWith(String chatWith) {
		this.chatWith = chatWith;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
