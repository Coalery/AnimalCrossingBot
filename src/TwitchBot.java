import org.jibble.pircbot.PircBot;

public class TwitchBot extends PircBot {
	
	private final String requestedNick;
	
	private String realNick;
	private String realServer;
	
	public TwitchBot(String nick) {
		this.requestedNick = nick;
		
		setName(this.requestedNick);
		setLogin(this.requestedNick);
	}
	
	@Override
	protected void onConnect() {
		super.onConnect();
		System.out.println("Connected!");
		
		sendRawLine("CAP REQ : twitch.tv/membership");
		sendRawLine("CAP REQ : twitch.tv/commands");
        sendRawLine("CAP REQ : twitch.tv/tags");
    }

    @Override
    protected void handleLine(String line) {
        super.handleLine(line);
        System.out.println(line);
        
        if (line.startsWith(":")) {
            String[] recvLines = line.split(" ");

            if (recvLines[1].equals("001")) {
                this.realServer = recvLines[0].substring(1);
                this.realNick = recvLines[2];
                System.out.println("realServer: " + this.realServer);
                System.out.println("realNick: " + this.realNick);
            }
        }
    }

    @Override
    protected void onJoin(String channel, String sender, String login, String hostname) {
        super.onJoin(channel, sender, login, hostname);
        if (sender.equals(this.realNick)) {
            System.out.println("Successfully joined: " + channel);
        }
    }

    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
    	System.out.println("message");
        if (message.equalsIgnoreCase("time")) {
            String time = new java.util.Date().toString();
            sendMessage(channel, sender + ": The time is now " + time);
        }
    }
}
