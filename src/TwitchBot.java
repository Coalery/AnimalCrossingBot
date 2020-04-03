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
	
	private final String dbPath = "D:\\File\\HW\\Files\\Programming\\Java\\AnimalCrossingBot\\fishdata.db";
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
    				return "!물고기 목록 <바다/민물/*> <월/*>";
    		}
    		else if(split.length == 3) {
    			if(split[1].equals("정보"))
    				return getFishInformation(split[2]);
    		}
    		else if(split.length == 4) {
    			String location = split[2];
    			int date = -1;
    			
    			// Validate Location
    			boolean locationCheck = true;
    			if(!location.equals("*"))
    				locationCheck = !location.contains("바다/민물");
    			
    			// Validate Dates
    			boolean dateCheck = true;
    			if(!split[3].equals("*")) {
	    			try {
	    				date = Integer.parseInt(split[3]);
	    				dateCheck = (1 <= date && date <= 12);
	    			} catch(NumberFormatException e) { dateCheck = false; }
    			}
    			
    			// Validate Apply
    			if(!(locationCheck && dateCheck))
    				return "잘못된 매개변수를 입력하였습니다.";
    			
    			if(location.equals("민물"))
    				location = "'강', '연못', '호수', '절벽위', '하구'";
    			else if(location.equals("바다"))
    				location = "'바다', '부두'";
    			else if(location.equals("*"))
    				location = null;
    			if(split[3].equals("*"))
    				date = 0;
    			return getFishList(location, date);
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
    
    private String getFishList(String location, int date) {
    	Connection conn = null;
    	Statement stmt = null;
    	
    	try {
    		String query = "select name from fishdata";
    		
    		if(location != null && date != 0)
    			query += " where date" + String.format("%02d", date) + "=1 and location in (" + location +  ")";
    		else if(location != null)
    			query += " where location in (" + location + ")";
    		else if(date != 0)
    			query += " where date" + String.format("%02d", date) + "=1";
    		query += " order by name";
    		
    		conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    		stmt = conn.createStatement();
    		ResultSet rs =  stmt.executeQuery(query);
			StringBuilder sb = new StringBuilder();
			
			while(rs.next())
				sb.append(rs.getString(1) + ", ");
			
			String result = sb.toString();
			if(result.length() == 0)
				return "데이터를 불러오는데 실패하였습니다.";
			return result.substring(0, result.length() - 2);
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
