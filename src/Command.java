
public class Command {
	private String maker;
	private String request;
	private String response;
	
	public Command(String maker, String request, String response) {
		this.maker = maker;
		this.request = request;
		this.response = response;
	}

	public String getMaker() { return maker;}
	public String getRequest() { return request; }
	public String getResponse() { return response; }

	@Override
	public String toString() {
		return "Command [maker=" + maker + ", request=" + request + ", response=" + response + "]";
	}
}
