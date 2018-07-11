package com.taotao.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class TestActiveMq {
	//@Test
	// queue
	// Producer
	public void testQueueProducer() throws Exception {
		// 1.创建一个连接工厂对象ConnectionFactory对象。需要指定mq服务的ip及端口号。注意参数brokerURL的开头是
		// tcp://而不是我们通常的http://，端口是61616而不是我们访问activemq后台管理页面所使用的8161
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://120.77.46.212:61616");
		// 2.使用ConnectionFactory创建一个连接Connection对象
		Connection connection = connectionFactory.createConnection();
		// 3.开启连接。调用Connection对象的start方法
		connection.start();
		// 4.使用Connection对象创建一个Session对象
		// 第一个参数是是否开启事务，一般不使用分布式事务，因为它特别消耗性能，而且顾客体验特别差，现在互联网的
		// 做法是保证数据的最终一致（也就是允许暂时数据不一致），比如顾客下单购买东西，一旦订单生成完就立刻响应给用户
		// 下单成功。至于下单后一系列的操作，比如通知会计记账、通知物流发货、商品数量同步等等都先不用管，只需要
		// 发送一条消息到消息队列，消息队列来告知各模块进行相应的操作，一次告知不行就两次，直到完成所有相关操作为止，这
		// 也就做到了数据的最终一致性。如果第一个参数为true，那么第二个参数将会被忽略掉。如果第一个参数为false，那么
		// 第二个参数为消息的应答模式，常见的有手动和自动两种模式，我们一般使用自动模式。
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 5.使用Session对象创建一个Destination对象，两种形式queue、topic。现在我们使用queue
		// 参数就是消息队列的名称
		Queue queue = session.createQueue("test-queue");
		// 6.使用Session对象创建一个Producer对象
		MessageProducer producer = session.createProducer(queue);
		// 7.创建一个TextMessage对象
		// 有两种方式，第一种方式：
		// TextMessage textMessage = new ActiveMQTextMessage();
		// textMessage.setText("hello,activemq!!!");
		// 第二种方式：
		TextMessage textMessage = session.createTextMessage("hello,activemq!!");
		// 8.发送消息
		producer.send(textMessage);
		// 9.关闭资源
		producer.close();
		session.close();
		connection.close();
	}

	//@Test
	public void testQueueConsumer() throws Exception {
		// 创建一个连接工厂对象
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://120.77.46.212:61616");
		// 使用连接工厂对象创建一个连接
		Connection connection = connectionFactory.createConnection();
		// 开启连接
		connection.start();
		// 使用连接对象创建一个Session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 使用Session创建一个Destination，Destination应该和消息的发送端一致。
		Queue queue = session.createQueue("test-queue");
		// 使用Session创建一个Consumer对象
		MessageConsumer consumer = session.createConsumer(queue);
		// 向Consumer对象中设置一个MessageListener对象，用来接收消息
		consumer.setMessageListener(new MessageListener() {
			// 匿名内部类
			@Override
			public void onMessage(Message message) {
				// 取消息的内容
				if (message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					try {
						String text = textMessage.getText();
						// 打印消息内容
						System.out.println(text);
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}

			}
		});
		// 系统等待接收消息
		/*
		 * while(true) { Thread.sleep(100); }
		 */
		System.in.read();
		// 关闭资源
		consumer.close();
		session.close();
		connection.close();
	}

	// topic
	// Producer
	//@Test
	public void testTopicProducer() throws Exception {
		// 创建一个连接工厂对象
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://120.77.46.212:61616");
		// 创建连接
		Connection connection = connectionFactory.createConnection();
		// 开启连接
		connection.start();
		// 创建Session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 创建Destination，应该使用topic
		Topic topic = session.createTopic("test-topic");
		// 创建一个Producer对象
		MessageProducer producer = session.createProducer(topic);
		// 创建一个TextMessage对象
		TextMessage textMessage = session.createTextMessage("hello activemq topic");
		// 发送消息
		producer.send(textMessage);
		// 关闭资源
		producer.close();
		session.close();
		connection.close();
	}

	//@Test
	public void testTopicConsumser() throws Exception {
		// 创建一个连接工厂对象
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://120.77.46.212:61616");
		// 使用连接工厂对象创建一个连接
		Connection connection = connectionFactory.createConnection();
		// 开启连接
		connection.start();
		// 使用连接对象创建一个Session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 使用Session创建一个Destination，Destination应该和消息的发送端一致。
		Topic topic = session.createTopic("test-topic");
		// 使用Session创建一个Consumer对象
		MessageConsumer consumer = session.createConsumer(topic);
		// 向Consumer对象中设置一个MessageListener对象，用来接收消息
		consumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
				// 取消息的内容
				if (message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					try {
						String text = textMessage.getText();
						// 打印消息内容
						System.out.println(text);
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}

			}
		});
		// 系统等待接收消息
		/*
		 * while(true) { Thread.sleep(100); }
		 */
		System.out.println("topic消费者2.。。。");
		System.in.read();
		// 关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
}