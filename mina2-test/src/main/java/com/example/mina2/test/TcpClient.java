/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package com.example.mina2.test;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 * An UDP client taht just send thousands of small messages to a UdpServer.
 * 
 * This class is used for performance test purposes. It does nothing at all, but
 * send a message repetitly to a server.
 * 
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class TcpClient extends IoHandlerAdapter {

	private Counter counter = new Counter();
	/** The connector */
	private IoConnector connector;

	/** The session */
	private static IoSession session;

	/** The buffer containing the message to send */
	// private IoBuffer buffer =
	// IoBuffer.allocate(8).put("123".getBytes()).flip();

	/** Timers **/
	private long t0;
	private long t1;

	/**
	 * Create the UdpClient's instance
	 */
	public TcpClient() {

		counter.start();

		connector = new NioSocketConnector();

		connector.setHandler(this);

		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		chain.addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory()));

		// ConnectFuture connFuture = connector.connect(new
		// InetSocketAddress("localhost", TcpServer.PORT));
		// ConnectFuture connFuture = connector.connect(new
		// InetSocketAddress("115.29.36.159", TcpServer.PORT));
		ConnectFuture connFuture = connector.connect(new InetSocketAddress("192.168.1.230", TcpServer.PORT));

		connFuture.awaitUninterruptibly();

		session = connFuture.getSession();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		counter.add();
		// System.out.println("sent:" + ((IoBuffer) message).toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sessionClosed(IoSession session) throws Exception {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sessionCreated(IoSession session) throws Exception {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sessionOpened(final IoSession session) throws Exception {

		for (int i = 0; i < 100; i++) {
			new Thread(new Runnable() {

				public void run() {
					while (true) {
						try {
							// IoBuffer buffer =
							// IoBuffer.allocate(8).put("123".getBytes()).flip();
							// session.write(buffer);
							session.write("hahahaha");
							Thread.sleep(1);
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
		}
	}

	/**
	 * The main method : instanciates a client, and send N messages. We sleep
	 * between each K messages sent, to avoid the server saturation.
	 * 
	 * @param args
	 *            The arguments
	 * @throws Exception
	 *             If something went wrong
	 */
	public static void main(String[] args) throws Exception {
		TcpClient client = new TcpClient();
	}
}
