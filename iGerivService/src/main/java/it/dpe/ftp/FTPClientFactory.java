/*
 * Copyright 2002-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.dpe.ftp;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.springframework.integration.MessagingException;
import org.springframework.util.StringUtils;

/**
 * FTPClientFactory. 
 * 
 * @author romanom
 * 
 */
public class FTPClientFactory {

	private static final Logger log = Logger.getLogger(FTPClientFactory.class);
	
	private static final String DEFAULT_REMOTE_WORKING_DIRECTORY = "/";

	private static final int DEFAULT_CONNECTION_TIMEOUT_MILLS = 5000;

	private static int defaultPort = FTP.DEFAULT_PORT;

	private static String defaultRemoteWorkingDirectory = DEFAULT_REMOTE_WORKING_DIRECTORY;

	private static int defaultClientMode = FTPClient.ACTIVE_LOCAL_DATA_CONNECTION_MODE;

	/* (non-Javadoc)
	 * @see it.dpe.ftp.FTPClientFactory#getClient(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, org.apache.commons.net.ftp.FTPClientConfig)
	 */
	public static FTPClient getClient(String host, String username, String password, String remoteWorkingDirectory, Integer port, Integer timeout, Integer clientMode, FTPClientConfig config) throws SocketException, IOException {
		FTPClient client = new FTPClient();
		if (config != null) {
			client.configure(config);
		}
		if (!StringUtils.hasText(username)) {
			throw new MessagingException("username is required");
		}
		client.setConnectTimeout(timeout != null ? timeout : DEFAULT_CONNECTION_TIMEOUT_MILLS);
		client.connect(host, port != null ? port : defaultPort);
		setClientMode(client, clientMode);
		if (!FTPReply.isPositiveCompletion(client.getReplyCode())) {
			throw new MessagingException("Connecting to server [" + host + ":"
					+ port + "] failed, please check the connection");
		}
		//log.info("Connected to server [" + host + ":" + port + "]");
		if (!client.login(username, password)) {
			throw new MessagingException(
					"Login failed. Please check the username and password.");
		}
		//log.info("login successful");
		client.setFileType(FTP.BINARY_FILE_TYPE);
		
		String remoteWorkDir = remoteWorkingDirectory != null ? remoteWorkingDirectory : defaultRemoteWorkingDirectory;
		//log.info("Changing current working directory from " + client.printWorkingDirectory() + " to " + remoteWorkDir + " for user " + username);
		boolean changeWorkingDirectory = client.changeWorkingDirectory(remoteWorkDir);
		//log.info("changeWorkingDirectory = " + changeWorkingDirectory + " for user " + username);
		if (!remoteWorkDir.equals(client.printWorkingDirectory()) && !changeWorkingDirectory) {
			throw new MessagingException("Could not change directory to '" + remoteWorkDir + "'. Please check the path.");
		}
		//log.info("working directory is: " + client.printWorkingDirectory());
		return client;
	}

	/**
	 * Sets the mode of the connection. Only local modes are supported.
	 * @param clientMode 
	 */
	private static void setClientMode(FTPClient client, Integer clientMode) {
		switch (clientMode == null ? defaultClientMode : clientMode) {
			case FTPClient.ACTIVE_LOCAL_DATA_CONNECTION_MODE:
				client.enterLocalActiveMode();
				break;
			case FTPClient.PASSIVE_LOCAL_DATA_CONNECTION_MODE:
				client.enterLocalPassiveMode();
				break;
			default:
				break;
		}
	}
	
	
}
