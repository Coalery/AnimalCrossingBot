import org.jibble.pircbot.PircBot;

public class TwitchBot extends PircBot {
	
	private String channel;
	
	public TwitchBot(String channel, String nick) {
		this.channel = channel;
		setName(nick);
		setLogin(nick);
	}

    @Override
    protected void handleLine(String line) {
        super.handleLine(line);
        System.out.println("[Message] " + line);
        
        if (line.startsWith(":")) {
            String[] recvLines = line.split(" ");
            if(recvLines[1].equalsIgnoreCase("PRIVMSG")) {
            	String sender = recvLines[0].split("!")[0].substring(1);
            	StringBuilder message = new StringBuilder(recvLines[3].substring(1));
            	for(int i=4; i<recvLines.length; i++)
            		message.append(" " + recvLines[i]);
            	String result = recieveMessage(sender, message.toString());
            	System.out.println("[Result] " + result);
            	if(result != null)
            		sendMessage(channel, "@" + sender + " : " + result);
            }
        }
    }
    
    private String recieveMessage(String sender, String message) {
    	String[] split = message.split(" ");
    	System.out.println("[" + message + "]");
    	if(split[0].equals("!러리봇"))
    		return "띠링";
    	if(split[0].equals("!물고기")) {
    		if(split.length == 1)
    			return "!물고기 <정보/목록>";
    	}
    	if(split[0].equals("!돌돔")) {
    		if(split.length == 1)
    			return "!돌돔 <벨>";
    		try {
    			int val = Integer.parseInt(split[1]);
    			return val / 5000.0f + "돌돔";
    		} catch(NumberFormatException e) {
    			return "숫자 값을 입력해주세요.";
    		}
    	}
    	return null;
    }
}
