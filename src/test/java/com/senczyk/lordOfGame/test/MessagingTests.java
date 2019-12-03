package com.senczyk.lordOfGame.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import org.junit.jupiter.api.*;


class MessagingTests {

	@Disabled
	@Test
	@DisplayName("Test zapytania POST do adresu /loginPl")
	void test() {
		
		try {
		URL url = new URL("localhost:8080/loginPl");
		URLConnection postLoginPl = url.openConnection();
		postLoginPl.connect();
		InputStream is = postLoginPl.getInputStream();
		int i;
		while((i=is.read())!=-1) {
			System.out.println(i);
		}
		
			Socket ss = new Socket( "localhost", 8080);
			assertThat( ss.isConnected() );
			PrintWriter pw = new PrintWriter(ss.getOutputStream());
			pw.println("Kamil");
		} catch (UnknownHostException e) {
			fail("UnknownHostException");
		} catch (IOException e) {
			fail("IOException");
		}
			
		

	}
	
	@Test
	@DisplayName("Test socketu dla url /getPlayers")
	public void getPlayersFromSocketTest() {
		try {
			InetAddress address = InetAddress.getByName("localhost:8080/getPlayers");
			SocketAddress socketAddress = new InetSocketAddress( address, 8080);
			
			
			Socket ss = new Socket();
			ss.connect(socketAddress);
			
			assertThat(ss.isConnected());
			DataOutputStream dos = new DataOutputStream(ss.getOutputStream());
			dos.writeByte(1);
			dos.writeUTF("test junit");
			dos.flush();
			BufferedReader dis = new BufferedReader(new InputStreamReader(ss.getInputStream()));
			dis.lines().forEach(System.out::println);

		}catch(Exception e) {
			System.out.println(e.getCause()+" "+e.getMessage());
			fail("Exception socket not connected");
		}finally {
			
		}
	}

}
