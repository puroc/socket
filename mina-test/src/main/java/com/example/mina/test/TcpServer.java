package com.example.mina.test;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.ThreadModel;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptorConfig;

public class TcpServer {

	private Counter counter = new Counter();

	private void start() {
		try {
			System.out.println("new TcpServer started.");
			counter.start();
			SocketAcceptor acceptor = new SocketAcceptor(2, Executors.newCachedThreadPool());
			SocketAcceptorConfig cfg = new SocketAcceptorConfig();
			// cfg.setThreadModel(ThreadModel.MANUAL);
			cfg.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory()));
			// cfg.getFilterChain().addLast("threadPool", new
			// ExecutorFilter(Executors.newCachedThreadPool()));
			acceptor.bind(new InetSocketAddress(9999), new IoHandler() {

				public void sessionOpened(IoSession session) throws Exception {

				}

				public void sessionIdle(IoSession session, IdleStatus status) throws Exception {

				}

				public void sessionCreated(IoSession session) throws Exception {

				}

				public void sessionClosed(IoSession session) throws Exception {

				}

				public void messageSent(IoSession session, Object message) throws Exception {

				}

				public void messageReceived(IoSession session, Object message) throws Exception {
					// System.out.println((String) message);
					counter.add();
				}

				public void exceptionCaught(IoSession session, Throwable cause) throws Exception {

				}
			}, cfg);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new TcpServer().start();
	}

}
