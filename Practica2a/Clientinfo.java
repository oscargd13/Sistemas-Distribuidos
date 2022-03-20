import org.omg.CORBA.*;
import java.util.*;
import chat.*;

public class Clientinfo {
	public chat.ChatClient chatclient;
	public String nick;
	public Clientinfo(String nick, chat.ChatClient chatclient) {
		this.chatclient = chatclient;
		this.nick = nick;
	}
}