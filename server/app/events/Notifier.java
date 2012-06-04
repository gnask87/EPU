package events;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import play.Logger;
import play.libs.Akka;
import play.libs.Comet;
import play.libs.F.Callback0;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.util.Duration;

import com.google.gson.Gson;




public class Notifier extends UntypedActor {

	public static Hashtable<String, List<Comet>> pool = new Hashtable<String, List<Comet>>();
	public static ActorRef notifierActor = Akka.system().actorOf(new Props(Notifier.class));
	
	
	public static Gson gson = new Gson();
	
	@Override
	public void onReceive(Object omsg) throws Exception {


		if (omsg instanceof Message) {
			final Message msg = (Message) omsg;

			List<Comet> sockets = pool.get(msg.getTag());
			if (sockets == null) {
				sockets = new ArrayList<Comet>();
				pool.put(msg.getTag(), sockets);
			}

			System.out.println(msg.getMessage());
			
			if (msg.getMessage() instanceof Comet) {
				final Comet cometSocket = (Comet) msg.getMessage();


				if (sockets.contains(cometSocket)) {

					// Browser is disconnected
					sockets.remove(cometSocket);
					Logger.info("Browser disconnected (" + sockets.size()
							+ " browsers currently connected) " + msg.getTag());

				} else {

					// Register disconnected callback
					cometSocket.onDisconnected(new Callback0() {
						public void invoke() {
							getContext().self().tell(new Message(msg.getTag(), cometSocket));
						}
					});

					// New browser connected
					sockets.add(cometSocket);
					Logger.info("New browser connected (" + sockets.size()
							+ " browsers currently connected) " + msg.getTag());

				}


				
			} 
			
			else /*if (msg.getMessage() instanceof Event) */
				for (Comet cometSocket : sockets) 
					cometSocket.sendMessage(gson.toJson(msg.getMessage()));
				

			

		} 


	

    }

}
