package application.email;

/**
 * @author Morgan
 */
public class ReloadThread extends Thread {

	private final ApplicationEMailForm parent;

	/**
	 * 
	 * @param parent 
	 */
	public ReloadThread(ApplicationEMailForm parent) {
		this.parent = parent;
	}

	/**
	 * 
	 */
	@Override
	@SuppressWarnings("SleepWhileInLoop")
	public void run() {
		while (!isInterrupted()) {
			try {
				System.out.println("begin sleep");
				sleep(60000);
				System.out.println("end sleep");
				parent.reloadMessages();
				System.out.println("applicationmail.ReloadThread.run() : messages loaded");
			} catch (InterruptedException ex) {
				System.err.println("Interruption : " + ex.getMessage());
			}
		}
	}

}
