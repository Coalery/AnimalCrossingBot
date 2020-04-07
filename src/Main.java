import java.io.IOException;

import javax.swing.JFrame;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;

public class Main {

    private static final String ADDRESS = "irc.twitch.tv.";
    private static final int PORT = 6667;
    private static final String NICKNAME = "doralife12_bot";
    public static final String CHANNEL = "#derbls";

    public static void main(String[] args) throws NickAlreadyInUseException, IOException, IrcException {
        TwitchBot bot = new TwitchBot(CHANNEL, NICKNAME);
        bot.setVerbose(false);

        bot.connect(ADDRESS, PORT, Oauth.OATUH);
        bot.joinChannel(CHANNEL);
        
        JFrame frame = new JFrame();
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}