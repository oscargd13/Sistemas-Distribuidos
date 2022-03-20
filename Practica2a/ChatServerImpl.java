import org.omg.CORBA.*;
import java.util.*;
import chat.*;

public class ChatServerImpl extends chat.ChatServerPOA {

	private Map<String, Clientinfo> clients = new HashMap<String, Clientinfo>();
	private List<String> nicks = new Vector<String>();

	// Se ejecuta automaticanmente cuando ejecutas el client para suscribirte al chat 
	public String subscribe(String nick, chat.ChatClient c)
		throws chat.NameAlreadyUsed {
		if (nicks.contains(nick)) throw new chat.NameAlreadyUsed();
		nicks.add(nick);
		String id = UUID.randomUUID().toString();
		System.out.println("subscribe: " + nick + " -> " + id);
		clients.put(id, new Clientinfo(nick, c));
		return id;
	}

	// cuando escribes /quit en el chat se lanza la funcion sacandote del chat 
	public void unsubscribe(String id) throws chat.UnknownID {
		System.out.println("unsubscribe: " + id);
		Clientinfo c = clients.remove(id);
		if (c == null) throw new chat.UnknownID();
		nicks.remove(c.nick);
	}

	public void comment(String id, String text) throws chat.UnknownID {
		Clientinfo from = clients.get(id);
		if (from == null) throw new chat.UnknownID();
		System.out.println(
			"comment: " + text + " by " + id+ " [" + from.nick + "]");
		for (Clientinfo to : clients.values()) {
			to.chatclient.update(from.nick, text);
		}
	}

}

