import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;

public class Main {

    private static final String ADDRESS = "irc.twitch.tv.";
    private static final int PORT = 6667;
    private static final String NICKNAME = "CoaleryBot";
    public static final String CHANNEL = "#youkyean";
    
    private static JTextArea log;
    
    public static void addLog(String log) {
    	String currentLog = Main.log.getText();
    	if(currentLog.length() > 5000)
    		currentLog = "";
    	Main.log.setText(currentLog + "\n" + log);
    }

    public static void main(String[] args) throws NickAlreadyInUseException, IOException, IrcException {
    	log = new JTextArea();
    	log.setEditable(false);
    	DefaultCaret dc = (DefaultCaret)log.getCaret();
    	dc.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    	
    	System.setProperty("file.encoding", "UTF-8");
    	try {
    		Field charset = Charset.class.getDeclaredField("defaultCharset");
    		charset.setAccessible(true);
    		charset.set(null, null);
    	} catch(Exception e) {
    		addLog(e.getMessage());
    		System.exit(0);
    	}
    	
        TwitchBot bot = new TwitchBot(CHANNEL, NICKNAME);
        bot.setVerbose(false);

        bot.connect(ADDRESS, PORT, Oauth.OATUH);
        bot.joinChannel(CHANNEL);
        
		bot.sendMessage(CHANNEL, "치사토봇 ON!");
		
        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        frame.addWindowListener(new WindowAdapter() {public void windowClosing(WindowEvent event) {
        	bot.sendMessage(CHANNEL, "치사토봇 OFF!");
        	System.exit(0);
        }});
        JScrollPane sc = new JScrollPane(log, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        JPanel send = new JPanel();
        send.setLayout(new BorderLayout());
        
        final JTextField field = new JTextField();
        field.addKeyListener(new KeyAdapter() {public void keyTyped(KeyEvent event) {
        	if(event.getKeyChar() != '\n') return;
        	if(field.getText().equals("")) return;
        	bot.sendMessage(CHANNEL, field.getText());
        	field.setText("");        	
        }});
        
        JButton sendButton = new JButton("전송");
        sendButton.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent event) {
        	if(field.getText().equals("")) return;
        	bot.sendMessage(CHANNEL, field.getText());
        	field.setText("");
        }});
        
        send.add(field, "Center");
        send.add(sendButton, "East");
        
        frame.add(sc, "Center");
        frame.add(send, "South");
        
        frame.setVisible(true);
    }
}