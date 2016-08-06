import org.jibble.pircbot.*;

public class GreensbotMain {
    
    public static void main(String[] args) throws Exception {
        
        // Now start our bot up.
        Greensbot greensbot = new Greensbot();
        
        // Enable debugging output.
        greensbot.setVerbose(true);
        
        // Connect to the IRC server.
        //greensbot.connect("irc.freenode.net");
        greensbot.connect("irc.chat.twitch.tv", 6667, "oauth:mm7baycjv59syyjw545wzibj749er5");

        // Join the #pircbot channel.
        //greensbot.joinChannel("#jibble");
        greensbot.joinChannel("#retrogaminggreens");
        
    }
    
}