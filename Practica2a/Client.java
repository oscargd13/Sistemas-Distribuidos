import chat.*;
import java.io.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.CosNaming.*;

public class Client implements Runnable {
	public void run() {
		try {
			// obtenemos referencia a POA
			org.omg.CORBA.Object obj =
			orb.resolve_initial_references("RootPOA");
			POA rootpoa = POAHelper.narrow(obj);
			//obtenemos referencia a POA manager
			POAManager manager = rootpoa.the_POAManager();
			// activamos el manager 
			manager.activate();
			orb.run();
		} catch (Exception e) {}
	}

	protected static ORB orb;

	public static void main(String args[]) {
		try {
			// inicializamos el ORB
			orb = ORB.init(args,null);
			// obtenemos NameService
			org.omg.CORBA.Object obj = 
			orb.resolve_initial_references("NameService");
			NamingContextExt ncRef =
			org.omg.CosNaming.NamingContextExtHelper.narrow(obj);

			// resolviendo el nombre del servidor
			obj = ncRef.resolve_str("chatserver_yzioaw");
			ChatServer chatserver = ChatServerHelper.narrow(obj);

			// creando el servidor
			ChatClientImpl cc = new ChatClientImpl();
			// conectando servidor a ORB
			ChatClient chatclient = cc._this(orb);
			Thread t = new Thread(new Client());
			String id = chatserver.subscribe("test", chatclient);
			try {
				System.out.println("Connected with ID " + id);
				System.out.println("Type /quit to exit");
				t.start();
				BufferedReader br =
					new BufferedReader(new InputStreamReader(System.in));
				while (true) {
					String s = br.readLine();
					if (s.equals("/quit")) break;
					chatserver.comment(id, s);
				}
			} finally {
				System.out.print("Unsubscribing...");
				chatserver.unsubscribe(id);
				System.out.println(" done");
				orb.destroy();
				t.join();
			}
		} catch(Exception e) {
			e.printStackTrace(System.out);
		}
	}
}



