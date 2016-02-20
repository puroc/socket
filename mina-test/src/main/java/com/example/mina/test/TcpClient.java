package com.example.mina.test;

import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.concurrent.Executors;

import org.apache.mina.common.ConnectFuture;
import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.ThreadModel;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.SocketConnector;
import org.apache.mina.transport.socket.nio.SocketConnectorConfig;

public class TcpClient {

	private SocketConnector connector;

	private Counter counter = new Counter();

	public void start() {
		counter.start();
		connector = new SocketConnector(2, Executors.newCachedThreadPool());
		SocketConnectorConfig cfg = new SocketConnectorConfig();
		// cfg.setThreadModel(ThreadModel.MANUAL);
		cfg.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory()));
		// cfg.getFilterChain().addLast("threadPool", new
		// ExecutorFilter(Executors.newCachedThreadPool()));
		ConnectFuture future = connector.connect(new InetSocketAddress("192.168.1.230", 9999), new IoHandler() {
			// ConnectFuture future = connector.connect(new
			// InetSocketAddress("127.0.0.1", 9999), new IoHandler() {

			public void sessionCreated(IoSession session) throws Exception {

			}

			public void sessionOpened(final IoSession session) throws Exception {
				for (int i = 0; i < 50; i++) {
					new Thread(new Runnable() {

						public void run() {

							while (true) {
								// Buffer flip =
								// ByteBuffer.allocate(10).put("123".getBytes()).flip();
								// session.write(flip);
								session.write("hello");
								try {
									Thread.sleep(1);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
					}).start();
				}
			}

			public void sessionClosed(IoSession session) throws Exception {

			}

			public void sessionIdle(IoSession session, IdleStatus status) throws Exception {

			}

			public void exceptionCaught(IoSession session, Throwable cause) throws Exception {

			}

			public void messageReceived(IoSession session, Object message) throws Exception {

			}

			public void messageSent(IoSession session, Object message) throws Exception {
				counter.add();
				// System.out.println("sent");
			}
		}, cfg);
		future.join();
	}

	public static void main(String[] args) {
		new TcpClient().start();
	}

}
