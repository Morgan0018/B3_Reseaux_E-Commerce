package requestresponse.multithread;

import crypto.utilities.CryptoUtilities;
import java.security.SecureRandom;
import java.util.Date;
import javax.swing.JOptionPane;
import requestresponse.multithread.abstracts.AbstractApplicationForm;

/**
 *
 * @author Morgan
 */
public class BasicLoginDialog extends javax.swing.JDialog {

	protected boolean loginOK;
	protected String user;

	/**
	 * Creates new form BasicLoginDialog
	 *
	 * @param parent
	 * @param modal
	 */
	public BasicLoginDialog(java.awt.Frame parent, boolean modal) {
		this(parent, modal, true);
		initComponents();
	}
	
	/**
	 * 
	 * @param parent
	 * @param modal
	 * @param noUI 
	 */
	public BasicLoginDialog(java.awt.Frame parent, boolean modal, boolean noUI) {
		super(parent, modal);
		loginOK = false;
		if (!noUI) initComponents();
	}

	/**
	 *
	 * @return
	 */
	public boolean isLoginOK() {
		return loginOK;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        loginLabel = new javax.swing.JLabel();
        loginTF = new javax.swing.JTextField();
        pwdLabel = new javax.swing.JLabel();
        pwdTF = new javax.swing.JTextField();
        loginButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Login");

        loginLabel.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        loginLabel.setText("Login :");

        pwdLabel.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        pwdLabel.setText("Password :");

        loginButton.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        loginButton.setText("Login");
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(loginLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(loginTF, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pwdLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pwdTF)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(loginLabel)
                    .addComponent(loginTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pwdLabel)
                    .addComponent(pwdTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(loginButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	/**
	 *
	 * @param evt
	 */
    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
		int rand = (new SecureRandom()).nextInt();
		long time = (new Date()).getTime();
		byte[] saltedPwd = CryptoUtilities.makeDigest(pwdTF.getText(), rand, time);
		user = loginTF.getText();
		Object cu[] = {user, saltedPwd, rand, time};
		BasicResponse res = (BasicResponse) ((AbstractApplicationForm) getParent())
			.connectToServer(new BasicRequest(BasicRequest.LOGIN, cu));
		if (res == null)
			loginOK = false;
		else if (res.getCode() == BasicResponse.LOGIN_OK) {
			((AbstractApplicationForm)getParent()).setUserCertificate((String) res.getChargeUtile());
			loginOK = true;
			this.setVisible(false);
		} else {
			loginOK = false;
			if (res.getCode() == BasicResponse.LOGIN_FAIL)
				JOptionPane.showMessageDialog(this, "Informations de login erronées", "Erreur", JOptionPane.ERROR_MESSAGE);
			else JOptionPane.showMessageDialog(this, res.getChargeUtile(), "Erreur", JOptionPane.ERROR_MESSAGE);
		}
    }//GEN-LAST:event_loginButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton loginButton;
    private javax.swing.JLabel loginLabel;
    private javax.swing.JTextField loginTF;
    private javax.swing.JLabel pwdLabel;
    private javax.swing.JTextField pwdTF;
    // End of variables declaration//GEN-END:variables

}