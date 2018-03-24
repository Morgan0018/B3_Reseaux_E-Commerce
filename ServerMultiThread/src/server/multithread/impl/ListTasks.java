package server.multithread.impl;

import java.util.LinkedList;
import server.multithread.interfaces.SourceTasks;

/**
 * A class that implements SourceTasks as a FIFO list
 * @author Morgan
 */
public class ListTasks implements SourceTasks {
	private final LinkedList tasksList;

	/**
	 * Default constructor
	 */
	public ListTasks(){
		tasksList = new LinkedList();
	}

	/**
	 * Removes the first Runnable from the list and returns it.
	 * The method is synchronized to avoid returning the same object twice
	 * @return The first Runnable in the list
	 * @throws InterruptedException
	 */
	@Override
	public synchronized Runnable getTask() throws InterruptedException {
		while (!existsTasks()) {
			wait();
		}
		return (Runnable)tasksList.remove();
	}

	/**
	 * checks if the list is empty and returns false if it is and true if it isn't.
	 * @return a boolean
	 */
	@Override
	public synchronized boolean existsTasks() {
		return !tasksList.isEmpty();
	}

	/**
	 * Adds the Runnable passed to the end of the list.
	 * @param r : An Object that implements Runnable
	 */
	@Override
	public synchronized void recordTask(Runnable r) {
		tasksList.addLast(r);
		notify();
	}

}
