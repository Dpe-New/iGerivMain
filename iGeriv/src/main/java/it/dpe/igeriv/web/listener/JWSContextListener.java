package it.dpe.igeriv.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class JWSContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// start the jWebSocket server sub system
		/*JWebSocketFactory.start("");
		TokenServer tokenServer = (TokenServer) JWebSocketFactory.getServer("ts0");
		if (tokenServer != null) {
			tokenServer.addListener(new JWebSocketListener());
		}*/
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		//JWebSocketFactory.stop();
	}

}
