import java.io.IOException;

import javax.swing.JFrame;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;

public class goFile {

    private static final String OAUTH = Oauth.oauth;
    private static final String ADDRESS = "irc.twitch.tv.";
    private static final int PORT = 6667;
    private static final String NICKNAME = "MYNICKHERE";
    private static final String CHANNEL = "#doralife12";

    public static void main(String[] args) throws NickAlreadyInUseException, IOException, IrcException {
        TwitchBot bot = new TwitchBot(NICKNAME);
        bot.setVerbose(false);

        bot.connect(ADDRESS, PORT, OAUTH);
        bot.joinChannel(CHANNEL);
        
        bot.sendMessage(CHANNEL, "러리봇이 켜졌습니다.");
        
        JFrame frame = new JFrame();
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}