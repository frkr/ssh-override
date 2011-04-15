/*
Copyright © 2011 Davi Saranszky Mesquita. All Rights Reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY Davi Saranszky Mesquita "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package marisma.ssh.tunnel;

import java.net.ServerSocket;
import java.net.Socket;

import org.apache.sshd.ClientChannel;
import org.apache.sshd.ClientSession;
import org.apache.sshd.SshClient;
import org.apache.sshd.client.future.ConnectFuture;

public class SSHTunnelServer implements Runnable {

    private ClientChannel channel;
    private InputFacade in = new InputFacade(System.in);
    private OutputFacade out = new OutputFacade(System.out);
    private Integer server;

    public static void main(String[] args) {
	System.out.println("localport host port login password");
	int local=Integer.parseInt(args[0].toString());
	String host=args[1].toString();
	int port=Integer.parseInt(args[2].toString());
	String login=args[3].toString();
	String password=args[4].toString();

	new SSHTunnelServer(local, host, port, login, password);
    }

    private SSHTunnelServer(int serverPort, String host, int port, String login, String password) {
	this.server=serverPort;
	try {
	    SshClient client = SshClient.setUpDefaultClient();
	    client.start();
	    ConnectFuture f = client.connect(host, port);
	    for (int i=1;i<=10;i++) {
		if (f.isConnected()) {
		    break;
		}
		Thread.sleep(1000);
	    }
	    if (!f.isConnected()) throw new Exception("TUNNEL SSH CONNECT");
	    ClientSession session = f.getSession();
	    session.authPassword(login, password);
	    session.waitFor(ClientSession.AUTHED | ClientSession.CLOSED, 10);

	    new Thread(this,this.getClass().getSimpleName()).start();

	    this.channel = session.createChannel(ClientChannel.CHANNEL_SHELL);
	    this.channel.setIn(this.in);
	    this.channel.setOut(this.out);
	    this.channel.setErr(this.out);
	    this.channel.open();
	    SSHTunnelServer.this.channel.waitFor(ClientChannel.CLOSED, 0);
	    System.exit(0);
	} catch (Exception e) {
	    throw new RuntimeException("TUNNEL SSH ERROR",e);
	}
    }

    public void run() {
	try {
	    Socket client = new ServerSocket(this.server).accept();
	    SSHTunnelServer.this.out.setOut(client.getOutputStream());
	    SSHTunnelServer.this.in.setIn(client.getInputStream());
	    while (!client.isClosed()) {
		Thread.sleep(500);
	    }
	    System.exit(0);
	} catch (Exception e) {
	    throw new RuntimeException("TUNNEL SERVER",e);
	}
    }
}
