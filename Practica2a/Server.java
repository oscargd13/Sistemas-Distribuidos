import chat.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.CosNaming.*;

public class Server {
	public static void main(String args[]) {
		try{
			// inicializar el ORB
			ORB orb = ORB.init(args,null);

			// obtener referencia a POA
			org.omg.CORBA.Object obj =
				orb.resolve_initial_references("RootPOA");
			POA rootpoa = POAHelper.narrow(obj);
			// obtener referencia a POA manager
			POAManager manager = rootpoa.the_POAManager();
			// activar manager 
			manager.activate();

			// obtener NameService
			obj = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef =
				org.omg.CosNaming.NamingContextExtHelper.narrow(obj);

			// crear servidor
			ChatServerImpl cs = new ChatServerImpl();
			// conectar servidor al ORB
			ChatServer chatserver = cs._this(orb);
			// referenciar servidor a NameService
			ncRef.rebind(ncRef.to_name("chatserver_yzioaw"), chatserver);

			System.out.println("Object activated");
			orb.run();

		} catch(Exception e) {
			e.printStackTrace(System.out);
		}
	}
}



