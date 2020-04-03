import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jibble.pircbot.PircBot;

public class TwitchBot extends PircBot {
	
	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	private String dbPath = "D:\\File\\HW\\Files\\Programming\\Java\\AnimalCrossingBot\\fishdata.db";
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
    		else if(split.length == 2) {
    			if(split[1].equals("정보"))
    				return "!물고기 정보 <물고기 이름>";
    			if(split[1].equals("목록"))
    				return "!물고기 목록 <월>";
    		}
    		else if(split.length == 3) {
    			if(split[1].equals("정보"))
    				return getFishInformation(split[2]);
    			if(split[1].equals("목록")) {
    				
    			}
    		}
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
    
    private String getFishInformation(String name) {
    	Connection conn = null;
    	Statement stmt = null;
    	
    	try {
    		conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    		stmt = conn.createStatement();
    		ResultSet rs = stmt.executeQuery("select * from fishdata where name='" + name + "'");
    		boolean success = rs.next();
    		if(!success) return "데이터를 불러오는데 실패하였습니다.";
    		
    		String datestr = rs.getString(2);
    		String time = rs.getString(3);
    		String location = rs.getString(4);
    		String size = rs.getString(5);
    		int price = rs.getInt(6);
    		if(time.equals("하루종일"))
    			return String.format("%s :: %s, %s %s에서 출현 :: %s :: %d벨", name, datestr, time, location, size, price);
    		else
    			return String.format("%s :: %s, %s에 %s에서 출현 :: %s :: %d벨", name, datestr, time, location, size, price);
    	} catch(SQLException e) {
    		e.printStackTrace();
    	} finally {
    		if(stmt != null)
    			try { stmt.close(); } catch(SQLException e) { e.printStackTrace(); }
    		if(conn != null)
    			try { conn.close(); } catch(SQLException e) { e.printStackTrace(); }
    	}
    	return "데이터를 불러오는데 실패하였습니다.";
    }
}
