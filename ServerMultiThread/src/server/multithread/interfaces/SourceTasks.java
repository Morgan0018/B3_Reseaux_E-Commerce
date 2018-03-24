package server.multithread.interfaces;

/**
 * @author Morgan
 */
public interface SourceTasks {

	/**
	 *
	 * @return a Runnable from the "source"
	 * @throws InterruptedException
	 */
	public Runnable getTask() throws InterruptedException;

	/**
	 * Contains what is needed to know if there is a task (Runnable) in the
	 * source
	 *
	 * @return a boolean
	 */
	public boolean existsTasks();

	/**
	 * Adds the task (Runnable) to the source
	 *
	 * @param r: an Object implementing Runnable
	 */
	public void recordTask(Runnable r);
}
