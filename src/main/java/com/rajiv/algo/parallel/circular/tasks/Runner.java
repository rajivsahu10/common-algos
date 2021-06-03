package com.rajiv.algo.parallel.circular.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

public class Runner {

	public static void main(String[] args) {

		try {
			(new Runner()).execute();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("end of main");
	}

	private void execute() throws InterruptedException, ExecutionException {
		List<Connector> connectors = getConnectors();

		BlockingQueue<Future<Connector>> resultQueue = new LinkedBlockingQueue<Future<Connector>>();
		BlockingQueue<Connector> taskQueue = new LinkedBlockingQueue<Connector>();
		TaskConsumer consumer = new TaskConsumer(taskQueue, resultQueue);
		consumer.start();

		TaskProducer producer = new TaskProducer(taskQueue, resultQueue, connectors);
		producer.start();

		System.out.println("end of execute method");
	}

	private static List<Connector> getConnectors() {
		List<Connector> connectors = new ArrayList<>();
		int waitPeriod = 2000;
		List<String> names = Arrays.asList("A", "B", "C", "D", "E");
//		List<String> names = Arrays.asList("A", "B");
		for (String name : names) {
			connectors.add(Connector.of(name, waitPeriod));
		}
		return connectors;
	}

}

class TaskProducer extends Thread {

	BlockingQueue<Connector> taskQueue;
	BlockingQueue<Future<Connector>> resultQueue;
	List<Connector> connectors;

	public TaskProducer(BlockingQueue<Connector> taskQueue, BlockingQueue<Future<Connector>> resultQueue,
			List<Connector> connectors) {
		this.taskQueue = taskQueue;
		this.resultQueue = resultQueue;
		this.connectors = connectors;
	}

	@Override
	public void run() {

		try {
			for (Connector connector : this.connectors) {
				taskQueue.put(connector);
			}
////			System.out.println("all initial tasks submitted");

			List<Future<Connector>> submittedTasks = new ArrayList<Future<Connector>>();
			while (true) {
				if (resultQueue.size() > 0) {
////					System.out.println("                                wait for new futures");
					Future<Connector> taskFuture = resultQueue.take();
//					System.out.println("                                got new futures");
					submittedTasks.add(taskFuture);
				} else {
////					System.out.println("                                future Q is empty");
					Thread.sleep(2000);
				}

				List<Future<Connector>> temp = new ArrayList<Future<Connector>>();
				for (Future<Connector> future : submittedTasks) {
					if (future.isDone()) {
						Connector connector = future.get();
////					System.out.println("                            putting back to task Q : " + connector.getName());
						taskQueue.put(connector);
					} else {
						temp.add(future);
					}
				}
				submittedTasks = temp;
				
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		System.out.println("                                            end of Task Producer Thread");
	}

}

class TaskConsumer extends Thread {

	BlockingQueue<Future<Connector>> outputQueue;
	BlockingQueue<Connector> inputQueue;
	ExecutorService executor = Executors.newFixedThreadPool(2);

	public TaskConsumer(BlockingQueue<Connector> taskQueue, BlockingQueue<Future<Connector>> resultQueue) {
		this.outputQueue = resultQueue;
		this.inputQueue = taskQueue;
	}

	@Override
	public void run() {
		try {
			while (true) {
				Connector connector = null;
				if (inputQueue.isEmpty()) {
////					System.out.println("task Queue is Empty");
					Thread.sleep(2000);
				} else {
////					System.out.println("wait for new task");
					connector = inputQueue.take();
//					System.out.println("got new task : " + connector.getName());

					MyTask task = new MyTask(connector);
////					System.out.println("submitted task to executor: " + connector.getName());
					Future<Connector> future = executor.submit(task);

////					System.out.println("putting to future Q : " + connector.getName());
					outputQueue.put(future);
				}
				
			}			
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();			
		}finally {
			System.out.println("end of Task Consure Thread");
			executor.shutdown();
		}
	}
}

class MyTask implements Callable<Connector> {

	private Connector connector;

	public MyTask(Connector connector) {
		this.connector = connector;
	}

	@Override
	public Connector call() throws Exception {
		Thread.sleep(connector.getWaitPeriod());
		connector.incr();
		return connector;
	}

}

class Connector {
	private Integer data = 0;
	private String name;
	private Integer waitPeriod = 0;
	private Integer iteration = 0;

	private Connector(String name, int waitPeriod) {
		this.name = name;
		this.waitPeriod = waitPeriod;
	}

	public static Connector of(String name, int waitPeriod) {
		return new Connector(name, waitPeriod);
	}

	public void incr() {
		iteration++;
		data += 2;
		StringBuilder sb = new StringBuilder();
		append(sb, "executing connector", name);
		append(sb, "iteration", iteration.toString());
		append(sb, "data", data.toString());

		System.out.println(sb.toString());

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((iteration == null) ? 0 : iteration.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((waitPeriod == null) ? 0 : waitPeriod.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Connector other = (Connector) obj;
		if (!this.name.equalsIgnoreCase(other.getName())) {
			return false;
		}
		return true;
	}

	private void append(StringBuilder sb, String key, String value) {
		sb.append(" ").append(key).append(" : ").append("[" + value + "], ");
	}

	public Integer getData() {
		return data;
	}

	public String getName() {
		return name;
	}

	public Integer getWaitPeriod() {
		return waitPeriod;
	}

	public Integer getIteration() {
		return iteration;
	}

}