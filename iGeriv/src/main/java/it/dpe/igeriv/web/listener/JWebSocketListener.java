package it.dpe.igeriv.web.listener;


public class JWebSocketListener /*implements WebSocketServerTokenListener*/ {

	/*@Override
	public void processClosed(WebSocketServerEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processOpened(WebSocketServerEvent event) {
		System.out.println("***********Client '" + event.getSessionId() + "' connected.*********");
	}

	@Override
	public void processPacket(WebSocketServerEvent arg0, WebSocketPacket arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processToken(WebSocketServerTokenEvent serverTokenEvent, Token token) {
		// here you can interpret the token type sent from the client according to your needs.
		String tokenNameSpace = token.getNS();
		String tokenType = token.getType();
		// check if token has a type and a matching namespace
		if (tokenType != null && "my.namespace".equals(tokenNameSpace)) {
			// create a response token
			Token tokenResponse = serverTokenEvent.createResponse(token);
			if ("getInfo".equals(tokenType)) {
				// if type is "getInfo" return some server information
				tokenResponse.setString("vendor", "JWebSocket");
				tokenResponse.setString("version", ".10");
				tokenResponse.setString("copyright", "JWebSocket.org");
				tokenResponse.setString("license", "No Liscense");
				tokenResponse.setString("Demo Text", "This is demo text");
			} else {
				// if unknown type in this namespace, return corresponding error message
				tokenResponse.setString("code", "-1");
				tokenResponse.setString("msg", "Token type '" + tokenType + "'not supported in namespace '" + tokenNameSpace + "'.");
			}
			serverTokenEvent.sendToken(tokenResponse);
		}
	}*/
}
