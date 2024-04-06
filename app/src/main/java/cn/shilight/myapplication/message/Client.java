package cn.shilight.myapplication.message;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Client {

	public static void ss(String[] args) {
		  String serverName = "127.0.0.1";

		Scanner sc = new Scanner(System.in);
	      int port = 122;
	      try
	      {
	         System.out.println("地址" + serverName + "端口" + port);
	         Socket client = new Socket(serverName, port);
	         System.out.println("远程地址" + client.getRemoteSocketAddress());
	         ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
			 ObjectInputStream in = new ObjectInputStream(client.getInputStream());


				new Thread(new Runnable() {
					@Override
					public void run() {

						while(client.isConnected()){

							try {
								MessagObtian ms = (MessagObtian) in.readObject();
								System.out.println("收到"+ms.getFromUid()+"的消息");
								System.out.println("内容"+ms.getContent());

							} catch (IOException | ClassNotFoundException e) {
								throw new RuntimeException(e);
							}
						}

					}
				}).start();


			 while (true){

				 out.writeObject((new MessagObtian(sc.next(), sc.next(), 0)).setContent(sc.next()));
				 out.flush();
			 }


	 
	 
	        // client.close();
	      }catch(IOException e)
	      {
	         e.printStackTrace();
	      }

	}

}
