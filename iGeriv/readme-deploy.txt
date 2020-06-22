Per eseguire il deploy verificare:

1. Log4j in Tomcat: log4j-1.2.15.jar, commons-logging-1.0.4.jar in {catalina.home}/lib

2. Configurazione JNDI per email in Tomcat: 
	1.1 dsn.jar, pop3.jar, imap.jar, mailapi.jar, smtp.jar, activation.jar in {catalina.home}/lib 
	1.2 Se viene moficato il file META-INF/context.xml: 
		Cancellare il corrispettivo (con nome = al tag "path" nel context.xml) in {catalina.home}/conf/Catalina/localhost

3. Problema di classpath causato da jaxb 2.2
La VM 1.6+ include già le api di JAXB, quindi se il tomcat va in errore ed è installata la VM 1.6+:
Creare la cartella "endored" sotto {catalina.home} e copiarci il file jaxb-api-2.2.2.jar
TODO Bisogna verificare perchè sul server non dà nessun conflitto