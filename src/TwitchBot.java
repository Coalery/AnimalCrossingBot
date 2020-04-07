import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.jibble.pircbot.PircBot;

public class TwitchBot extends PircBot {
	
	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	private final String fishDBPath = "D:\\File\\HW\\Files\\Programming\\Java\\AnimalCrossingBot\\fishdata.db";
	private final String cmdDBPath = "D:\\File\\HW\\Files\\Programming\\Java\\AnimalCrossingBot\\command.db";
	private String channel;
	
	private HashMap<String, Command> commands;
	
	public TwitchBot(String channel, String nick) {
		this.channel = channel;
		setName(nick);
		setLogin(nick);
		
		commands = new HashMap<>();
		ArrayList<Command> commandList = getCommands();
		for(int i=0; i<commandList.size(); i++) {
			Command tmpCmd = commandList.get(i);
			commands.put(tmpCmd.getRequest(), tmpCmd);
		}
		sendMessage(channel, "치사토봇 ON!");
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
            	if(!recvLines[3].substring(1).startsWith("!"))
            		return;
            	for(int i=4; i<recvLines.length; i++)
            		message.append(" " + recvLines[i]);
            	String result = recieveMessage(sender, message.toString());
            	if(result != null) {
            		sendMessage(channel, "@" + sender + " : " + result);
            		System.out.println("[Send Completed] " + result);
            	}
            }
        }
    }
    
    private String recieveMessage(String sender, String message) {
    	String[] split = message.split(" ");
    	System.out.println("[" + message + "]");
    	
    	if(!split[0].startsWith("!"))
    		return null;
    	
    	String[] masterCommand = {"!도움말", "!물고기", "!돌돔", "!명령어"};
    	
    	if(split[0].equals("!도움말")) {
    		StringBuilder sb = new StringBuilder("!도움말");
    		for(int i=1; i<masterCommand.length; i++)
    			sb.append(", " + masterCommand[i]);
    		Iterator<String> it = commands.keySet().iterator();
    		while(it.hasNext())
    			sb.append(", " + it.next());
    		return sb.toString();
    	}
    	else if(split[0].equals("!물고기")) {
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
    	else if(split[0].equals("!돌돔")) {
    		if(split.length == 1)
    			return "!돌돔 <벨>";
    		try {
    			int val = Integer.parseInt(split[1]);
    			return val / 5000.0f + "돌돔";
    		} catch(NumberFormatException e) {
    			return "숫자 값을 입력해주세요.";
    		}
    	}
    	else if(split[0].equals("!명령어")) {
    		if(split.length == 1)
    			return "!명령어 <추가/수정/삭제>";
    		else if(split.length == 2) {
    			if(split[1].equals("추가"))
    				return "!명령어 추가 <명령어> <반환값>";
    			else if(split[1].equals("수정"))
    				return "!명령어 수정 <명령어> <반환값>";
    			else if(split[1].equals("삭제"))
    				return "!명령어 삭제 <명령어>";
    		}
    		else if(split.length == 3) {
    			if(split[1].equals("삭제")) {
    				String request = split[2];
    				if(!request.startsWith("!"))
						request = "!" + request;
    				if(commands.containsKey(request)) {
    					Command currentCmd = commands.get(request);
    					if(sender.equals(currentCmd.getMaker()) || sender.equals("derbls")) {
    						commands.remove(currentCmd.getRequest());
    						removeCommand(currentCmd);
    						return "명령어가 정상적으로 삭제되었습니다!";
    					} else
    						return "명령어 삭제 권한이 없습니다!";
    				} else
    					return "존재하지 않는 명령어입니다!";
    			}
    		}
    		else if(split.length >= 4) {
    			if(split[1].equals("추가")) {
    				if(!commands.containsKey(split[2])) {
    					String request = split[2];
    					if(!request.startsWith("!"))
    						request = "!" + request;
    					String response = split[3];
    					for(int i=4; i<split.length; i++)
    						response += " " + split[i];
    					Command currentCmd = new Command(sender, request, response);
    					commands.put(request, currentCmd);
    					createCommand(currentCmd);
    					return "명령어가 정상적으로 추가되었습니다!";
    				} else
    					return "명령어가 이미 존재합니다!";
    			}
    			else if(split[1].equals("수정")) {
    				if(commands.containsKey(split[2])) {
    					Command currentCmd = commands.get(split[2]);
    					if(sender.equals(currentCmd.getMaker()) || sender.equals("derbls")) {
    						commands.remove(currentCmd.getRequest());
    						String request = split[2];
    						if(!request.startsWith("!"))
        						request = "!" + request;
    						String response = split[3];
        					for(int i=4; i<split.length; i++)
        						response += " " + split[i];
    						Command targetCmd = new Command(sender, request, response);
    						commands.put(request, targetCmd);
    						editCommand(currentCmd);
    						return "명령어 수정이 완료되었습니다!";
    					} else
    						return "명령어 수정 권한이 없습니다!";
    				} else
    					return "존재하지 않는 명령어입니다!";
    			}
    		}
    	}
    	else { // Custom Command
    		if(!commands.containsKey(split[0]))
    			return null;
    		return commands.get(split[0]).getResponse();
    	}
    	return null;
    }
    
    private void createCommand(Command cmd) {
    	Connection conn = null;
    	Statement stmt = null;
    	
    	try {
    		conn = DriverManager.getConnection("jdbc:sqlite:" + cmdDBPath);
    		stmt = conn.createStatement();
    		stmt.executeUpdate("insert into command values('" + cmd.getMaker() + "', '" + cmd.getRequest() + "', '" + cmd.getResponse() + "')");
    	} catch(SQLException e) {
    		e.printStackTrace();
    	} finally {
    		if(stmt != null)
    			try { stmt.close(); } catch(SQLException e) { e.printStackTrace(); }
    		if(conn != null)
    			try { conn.close(); } catch(SQLException e) { e.printStackTrace(); }
    	}
    }

    private void editCommand(Command cmd) {
    	Connection conn = null;
    	Statement stmt = null;
    	
    	try {
    		conn = DriverManager.getConnection("jdbc:sqlite:" + cmdDBPath);
    		stmt = conn.createStatement();
    		stmt.executeUpdate("update command set response='" + cmd.getResponse() + "' where request='" + cmd.getRequest() + "'");
    	} catch(SQLException e) {
    		e.printStackTrace();
    	} finally {
    		if(stmt != null)
    			try { stmt.close(); } catch(SQLException e) { e.printStackTrace(); }
    		if(conn != null)
    			try { conn.close(); } catch(SQLException e) { e.printStackTrace(); }
    	}
    }
    
    private void removeCommand(Command cmd) {
    	Connection conn = null;
    	Statement stmt = null;
    	
    	try {
    		conn = DriverManager.getConnection("jdbc:sqlite:" + cmdDBPath);
    		stmt = conn.createStatement();
    		stmt.executeUpdate("delete from command where request='" + cmd.getRequest() + "'");
    	} catch(SQLException e) {
    		e.printStackTrace();
    	} finally {
    		if(stmt != null)
    			try { stmt.close(); } catch(SQLException e) { e.printStackTrace(); }
    		if(conn != null)
    			try { conn.close(); } catch(SQLException e) { e.printStackTrace(); }
    	}
    }
    
    private ArrayList<Command> getCommands() {
    	ArrayList<Command> result = new ArrayList<Command>();
    	
    	Connection conn = null;
    	Statement stmt = null;
    	
    	try {
    		conn = DriverManager.getConnection("jdbc:sqlite:" + cmdDBPath);
    		stmt = conn.createStatement();
    		ResultSet rs =  stmt.executeQuery("select * from command");
			
			while(rs.next()) {
				String maker = rs.getString(1);
				String request = rs.getString(2);
				String response = rs.getString(3);
				result.add(new Command(maker, request, response));
			}
    	} catch(SQLException e) {
    		e.printStackTrace();
    	} finally {
    		if(stmt != null)
    			try { stmt.close(); } catch(SQLException e) { e.printStackTrace(); }
    		if(conn != null)
    			try { conn.close(); } catch(SQLException e) { e.printStackTrace(); }
    	}
    	
    	return result;
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
    		
    		conn = DriverManager.getConnection("jdbc:sqlite:" + fishDBPath);
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
    	if(name.equals("뚊"))
    		name = "돌돔";
    	
    	Connection conn = null;
    	Statement stmt = null;
    	
    	try {
    		conn = DriverManager.getConnection("jdbc:sqlite:" + fishDBPath);
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
