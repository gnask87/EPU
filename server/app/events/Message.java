package events;

public class Message {

	String tag;
	Object message;
	
	public String getTag() {
		return tag;
	}



	public void setTag(String tag) {
		this.tag = tag;
	}



	public Message(String t, Object o) {
		tag = t;
		message = o;
	}



	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}



	@Override
	public String toString() {
		return "Message [tag=" + tag + ", message=" + message + "]";
	}


	
	
}
