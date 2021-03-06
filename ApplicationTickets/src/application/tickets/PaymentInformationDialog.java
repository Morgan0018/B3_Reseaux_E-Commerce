package application.tickets;

import java.awt.Frame;
import java.text.DecimalFormat;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author Morgan
 */
public class PaymentInformationDialog extends javax.swing.JDialog {

	/**
	 * Creates new form PaymentInformationDialog
	 *
	 * @param parent
	 * @param title
	 * @param modal
	 * @param amount
	 */
	public PaymentInformationDialog(Frame parent, String title, boolean modal, float amount) {
		super(parent, title, modal);
		initComponents();
		
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		amountTooLabel.setText(df.format(amount) + " €");
	}

	/**
	 * This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        payLabel = new javax.swing.JLabel();
        cardLabel = new javax.swing.JLabel();
        cardTF = new javax.swing.JTextField();
        ownerLabel = new javax.swing.JLabel();
        ownerTF = new javax.swing.JTextField();
        amountLabel = new javax.swing.JLabel();
        amountTooLabel = new javax.swing.JLabel();
        sendButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        payLabel.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        payLabel.setText("Informations de paiement");

        cardLabel.setText("Numéro de carte :");

        ownerLabel.setText("Nom du propriétaire :");

        amountLabel.setText("Montant de la transaction :");

        amountTooLabel.setText("jLabel1");

        sendButton.setText("Envoyer");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(payLabel)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(ownerLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(ownerTF))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(cardLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cardTF, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(amountLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(amountTooLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(135, 135, 135)
                        .addComponent(sendButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(payLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardLabel)
                    .addComponent(cardTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ownerLabel)
                    .addComponent(ownerTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(amountLabel)
                    .addComponent(amountTooLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sendButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	/**
	 * 
	 * @param evt 
	 */
    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
		String cardNumber = cardTF.getText();
		String ownerName = ownerTF.getText();
		if ("".equals(cardNumber) || "".equals(ownerName)) 
			JOptionPane.showMessageDialog(this,
				"Le numéro de carte et le proriétaire doivent être remplit",
				"Attention", JOptionPane.WARNING_MESSAGE);
		else {
			((AppTicketsForm)getParent()).setCardNumber(cardNumber);
			((AppTicketsForm)getParent()).setOwnerName(ownerName);
			this.setVisible(false);
			this.dispose();
		}
    }//GEN-LAST:event_sendButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel amountLabel;
    private javax.swing.JLabel amountTooLabel;
    private javax.swing.JLabel cardLabel;
    private javax.swing.JTextField cardTF;
    private javax.swing.JLabel ownerLabel;
    private javax.swing.JTextField ownerTF;
    private javax.swing.JLabel payLabel;
    private javax.swing.JButton sendButton;
    // End of variables declaration//GEN-END:variables
}
