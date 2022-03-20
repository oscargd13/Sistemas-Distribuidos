package Practica2;

import HelloApp.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;

import java.util.Properties;

class HelloImpl extends HelloPOA {
  private ORB orb;

  public void setORB(ORB orb_val) {
    orb = orb_val; 
  }
    
  // implementar el metodo sayHello()
  public String sayHello() {
    return "\nHello world !!\n";
  }
    
  // implementar el metodo shutdown() 
  public void shutdown() {
    orb.shutdown(false);
  }
}


public class Server {

  public static void main(String args[]) {
    try{
      // crear e inicializar el ORB
      ORB orb = ORB.init(args, null);

      // obtener referencia a rootpoa y activar POAManager
      POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
      rootpoa.the_POAManager().activate();

      // crear servidor y registrarlo en el ORB
      HelloImpl helloImpl = new HelloImpl();
      helloImpl.setORB(orb); 

      // obtener la referencia la objeto servidor
      org.omg.CORBA.Object ref = rootpoa.servant_to_reference(helloImpl);
      Hello href = HelloHelper.narrow(ref);
          
      // obtener el contexto
      // NameService invoca el servicio de nombres
      org.omg.CORBA.Object objRef =
          orb.resolve_initial_references("NameService");

      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

      // vincular la refenrencia de objeto en la deniminación
      String name = "Hello";
      NameComponent path[] = ncRef.to_name( name );
      ncRef.rebind(path, href);

      System.out.println("HelloServer ready and waiting ...");

      // esperar a las invocaciones del cliente
      orb.run();
    } 
        
      catch (Exception e) {
        System.err.println("ERROR: " + e);
        e.printStackTrace(System.out);
      }
          
      System.out.println("HelloServer Exiting ...");
        
  }
}