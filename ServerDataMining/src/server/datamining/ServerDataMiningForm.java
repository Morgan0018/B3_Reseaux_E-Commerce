package server.datamining;

import requestresponse.multithread.abstracts.ConsoleServerForm;
import server.multithread.abstracts.ThreadServer;
import server.multithread.abstracts.ThreadServerMultiClient;
import server.multithread.extend.ThreadServerMultiConnection;
import server.multithread.impl.ListTasks;

/**
 * A class that extends ConsoleServerForm
 * @author Morgan
 */
public class ServerDataMiningForm extends ConsoleServerForm {

	private int port_dm;
	private ThreadServer ts_dm;

	/**
	 * Creates new form ServerDataMiningForm.
	 * The properties file name is "serverdatamining.properties" and is in the current folder.
	 */
	public ServerDataMiningForm() {
		super("Serveur Data Mining");
		super.setPropertiesFileName("serverdatamining.properties");
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(ServerDataMiningForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(ServerDataMiningForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(ServerDataMiningForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(ServerDataMiningForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				ServerDataMiningForm sdmf = new ServerDataMiningForm();
				sdmf.setLocationRelativeTo(null);
				sdmf.setVisible(true);
			}
		});
	}

	/**
	 * Sets the ports that this server listens to. Creates and starts an
	 * extension of server.multithread.ThreadServer for each port depending on
	 * the type of client expected.
	 */
	@Override
	protected void serverStart() {
		port_dm = Integer.parseInt(properties.getProperty("PORT_DM"));
		ts_dm = new ThreadServerMultiConnection("DataMining", port_dm, this, new ListTasks());
		ts_dm.start();
		TraceEvents("server#start data mining#main");
	}

	/**
	 * Calls the doStop() method of every ThreadServer created.
	 */
	@Override
	protected void serverStop() {
		if (ts_dm.isAlive()) {
			ts_dm.doStop();
			TraceEvents("server#stop data mining#main");
		}
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
