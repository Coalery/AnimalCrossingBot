import org.jibble.pircbot.PircBot;

public class TwitchBot extends PircBot {
	
	public TwitchBot(String nick) {
		setName(nick);
		setLogin(nick);
	}

    @Override
    protected void handleLine(String line) {
        super.handleLine(line);
        System.out.println("[Message] " + line);
        
        if (line.startsWith(":")) {
            String[] recvLines = line.split(" ");
        }
    }
}
