package com.example.mina.test;

import java.nio.ByteBuffer;

import com.neusoft.leaf.nio.NioServiceHandler;
import com.neusoft.leaf.nio.NioServiceSession;
import com.neusoft.leaf.nio.mina.TcpMinaServer;

public class DamfServer {

	private static int PORT = 9999;

	private Counter counter = new Counter();

	private TcpMinaServer tms = new TcpMinaServer();

	public void start() {
		try {
			counter.start();
			tms.setServiceHandler(new NioServiceHandler() {
				public void handle(Object recvedMsg, NioServiceSession ts) {
					try {
						// ByteBuffer bb = (ByteBuffer) recvedMsg;
						// byte[] b = new byte[bb.remaining()];
						// bb.get(b);
						counter.add();
						// String body = new String(b);
						// System.out.println(body);
						// Assert.assertEquals("abc321", body);
						// ts.write("aaa".getBytes());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			tms.startup(PORT);
			// Thread.sleep(99999999);

			// Socket socket = new Socket("127.0.0.1", PORT);
			// OutputStream os = socket.getOutputStream();
			// InputStream is = socket.getInputStream();
			// os.write("abc321".getBytes());
			// os.flush();
			// byte[] b = new byte[3];
			// if ((is.read(b)) >= 0) {
			// Assert.assertEquals("aaa", new String(b));
			// } else {
			// Assert.fail();
			// }
			// assertTrue(true);
			// socket.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new DamfServer().start();
	}

}
