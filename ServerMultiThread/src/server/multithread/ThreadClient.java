package server.multithread;

import server.multithread.interfaces.SourceTasks;

/**
 * Thread that will handle a client
 *
 * @author Morgan
 */
public class ThreadClient extends Thread {

	private final SourceTasks toDoTasks;
	private final String name;
	private Runnable running;

	/**
	 * Constructor
	 *
	 * @param toDoTasks : an Object implementing server.multithread.interfaces.SourceTasks
	 * @param name : the [Type of thread (ex: Pool),] server name [and thread number] (for logging)
	 */
	public ThreadClient(SourceTasks toDoTasks, String name) {
		this.toDoTasks = toDoTasks;
		this.name = name;
	}

	/**
	 * Loops while not Interrupted.
	 * Waits for a task (a Runnable) to be available in the SourceTasks and calls its run() method
	 */
	@Override
	public void run() {
		while (!isInterrupted()) {
			try {
				running = toDoTasks.getTask();
			} catch (InterruptedException e) {
				System.err.println(name + " - Interruption : " + e.getMessage());
				break;
			}
			running.run();
		}
	}

}
