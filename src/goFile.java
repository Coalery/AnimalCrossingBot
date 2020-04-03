import java.io.IOException;

import javax.swing.JFrame;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;

public class goFile {

    private static final String OAUTH = "";
    private static final String ADDRESS = "irc.twitch.tv.";
    private static final int PORT = 6667;
    private static final String Nick = "MYNICKHERE";
    private static final String Channel = "#doralife12";

    public static void main(String[] args) throws NickAlreadyInUseException, IOException, IrcException {
        TwitchBot bot = new TwitchBot(Nick);
//        bot.setVerbose(true);
        bot.setVerbose(false);

        bot.connect(ADDRESS, PORT, OAUTH);
        bot.joinChannel(Channel);
        
        JFrame frame = new JFrame();
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}