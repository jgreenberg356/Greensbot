import org.jibble.pircbot.*;
import java.sql.*;
import java.util.*;
import java.lang.*;

public class Greensbot extends PircBot {

	String my_channel = "#retrogaminggreens"; //hardcoded channel name
	int input = 0;
	int secret_number = 0; //hold the actual random number generated for the RNG number game
	boolean numberGameOn = false; //is the RNG number game running?
	boolean numberGameOn_firstRun = false; //keep track of first run through of RNG number game
	
	ArrayList randomMsg = new ArrayList();
	User[] viewers = new User[10];
	
	Connection conn = null;
	Statement stmt = null;

    public Greensbot() {
        this.setName("greensbot");
		//connect();
		
		randomMsg.add("Ah! I think I pooped myself..");
		randomMsg.add("Affirmative.");
		randomMsg.add("Definitely shit the bed...");
		randomMsg.add("Yes?");
		randomMsg.add("Kappa");
		randomMsg.add("PogChamp");
    }
	public void connect(){
		//Connection conn = null;
		try {
			// db parameters
			Class.forName("org.sqlite.JDBC");
			String url = "jdbc:sqlite:C:/Users/Retro/Desktop/Greenbot/greensbot_db.db";	
			conn = DriverManager.getConnection(url);
			//conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/Retro/Desktop/Greenbot/greensbot_db.db");
			conn.setAutoCommit(false);

			System.out.println("Connected to database.");
					
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			} /*finally {
				try {
					if (conn != null) {
						conn.close();
					}
					} catch (SQLException ex) {
						System.out.println(ex.getMessage());
					}
			}*/ catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	public void onConnect(){
		sendMessage(my_channel, "/me has been summoned to the channel.");
	}
	public void onJoin(String channel, String sender, String login, String hostname){
		if (sender.equalsIgnoreCase("greensbot")){
		}
		else {
			sendMessage(channel, "Welcome to the channel, " + sender);
			System.out.println("welcome, " + sender);
		}
			
	}    
    public void onMessage(String channel, String sender,
                       String login, String hostname, String message) {
		//Return date and time
        if (message.equalsIgnoreCase("time")) {
            String time = new java.util.Date().toString();
            sendMessage(channel, sender + ": The time is now " + time);
        }
		//Hyperlink Filter
		else if ((message.startsWith("www.")) || (message.startsWith("http://")) || (message.startsWith("https://"))) {
			sendMessage(channel, "/me slaps " + sender + " on the wrist. No links!");
			sendMessage(channel, "/timeout " + sender);
		}
		else if (message.equalsIgnoreCase("list users")){
			
//			viewers = getUsers(channel);
//			for (int x = 0; x <= viewers.; x++){
//				System.out.println(viewers.length);
//				String bla = viewers[x].getNick();
//				sendMessage(channel, bla);
			//}
			//sendMessage(channel, getUsers(channel).toString());
		}
		//Start Number Game
		else if (message.equalsIgnoreCase("start rng")) {
			if (numberGameOn){
				sendMessage(channel, sender + ", the Number Game is already in progress. Join in!");
			}
			else{
				numberGameOn = true;
				numberGameOn_firstRun = true;
				numberGame(message, channel, sender, numberGameOn_firstRun, numberGameOn);
			}
		}
		//send the message to the Number Game
		else if (numberGameOn){
			numberGame(message, channel, sender, numberGameOn_firstRun, numberGameOn);
		}
/* 		else if (message.contains("greensbot") || (message.contains("Greensbot"))){
			randomMessage();
		} */
		else if (message.equalsIgnoreCase("list")){
			list();
		}
		else{
			//normal input from chat
		}
    }

	public void numberGame(String msg, String channel, String sender, boolean ngofs, boolean numberGameOn){
		//if numberGameOn_firstRun is true
		if (ngofs){
			//sendMessage(channel, "inside ngofs if statement");
			sendMessage(channel, "/me is thinking of a number between 1 and 100.");
			sendMessage(channel, "Got it! Guess now.");
			numberGameOn_firstRun = false;
			//Guess a random number once and store into var rn
			Random r = new Random();
			secret_number = r.nextInt((99) + 1);
		}
		//if not the first pass
		else {
			try {
				//sendMessage(channel, "Trying to store the input");
				//Get the first number and each number past the initial "-" from the message (msg)
				input = Integer.parseInt(msg.substring(0));
			} catch (NumberFormatException ex){
				if (Character.isDefined(msg.charAt(0)) && (!(Character.isDigit(msg.charAt(0))))) {return;}
				sendMessage(channel, "That is not a number, " + sender);
				return;
			}
			if (input == secret_number && numberGameOn){
				sendMessage(channel, "You guessed the correct number, " + sender);
				setNumberGameOn(false);
			}
			else if (input > secret_number){
				sendMessage(channel, "The number is lower, " + sender);
			}
			else {
				sendMessage(channel, "The number is higher, " + sender);
			}
		}
	}
	
	public boolean getNumberGameOn(){
		return numberGameOn;
	}
	
	public void setNumberGameOn(boolean i){
		numberGameOn = i;
	}

	public void list(){
		try {
		  ResultSet rs = stmt.executeQuery( "SELECT * FROM viewers;" );
		  while ( rs.next() ) {
			 int v_id = rs.getInt("v_id");
			 String  v_name = rs.getString("v_name");
			 System.out.println( "v_id = " + v_id );
			 sendMessage(my_channel, v_name);
			 System.out.println( "v_name = " + v_name );
			 System.out.println();
		  }
		  rs.close();
		  stmt.close();
		} catch ( Exception e ) {
		  System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		  System.exit(0);
		}
	}
	public void randomMessage(){
		int i = 0;
		Random z = new Random();
		i = z.nextInt(randomMsg.size());
		String s1 = randomMsg.get(i).toString();
		sendMessage(my_channel, s1);
	}
}