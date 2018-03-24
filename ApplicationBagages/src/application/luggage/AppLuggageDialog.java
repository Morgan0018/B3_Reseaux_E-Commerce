package application.luggage;

import db.airport.models.Luggage;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import requestresponse.impl.LUGAP.*;
import requestresponse.multithread.BasicRequest;
import requestresponse.multithread.BasicResponse;

/**
 * @author Morgan
 */
public class AppLuggageDialog extends javax.swing.JDialog {

	private ArrayList<Luggage> luggages;
	private Object oldValue, newValue;

	/**
	 * Creates new form AppLuggageDialog
	 *
	 * @param parent
	 * @param modal
	 */
	public AppLuggageDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
	}

	/**
	 *
	 * @param parent
	 * @param title
	 * @param modal
	 * @param luggages
	 */
	public AppLuggageDialog(AppLuggageForm parent, String title, boolean modal, ArrayList<Luggage> luggages) {
		super(parent, title, modal);
		initComponents();
		flightLabel.setText(title);
		this.luggages = luggages;
		DefaultTableModel dtm = (DefaultTableModel) luggageTable.getModel();
		dtm.setRowCount(0);
		for (Luggage luggage : luggages) {
			String suitcase = luggage.isSuitcase() ? "VALISE" : "PAS VALISE";
			String comment = luggage.getComments() == null ? "NEANT" : luggage.getComments();
			Object[] data = {luggage.getId(), luggage.getWeight() + " kg", suitcase, luggage.isReceived(),
				luggage.getLoaded(), luggage.isCustomChecked(), comment};
			dtm.addRow(data);
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
        flightLabel = new javax.swing.JLabel();
        jScrollPane = new javax.swing.JScrollPane();
        luggageTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        titleLabel.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        titleLabel.setText("Baggages pour le ");

        flightLabel.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        flightLabel.setText("jLabel1");

        luggageTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, "VALISE",  new Boolean(true), "N", null, null},
                {null, null, "PAS VALISE", null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "<html>&nbsp;<br/>Identifiant<br/>&nbsp;</html>", "<html>&nbsp;<br/>Poids<br/>&nbsp;</html>", "<html>&nbsp;<br/>Type<br/>&nbsp;</html>", "<html>&nbsp;<br/>Réceptionné<br/>(O/N)</html>", "<html>Chargé<br/>en soute<br/>(O/N/R)</html>", "<html>Vérifié par<br/>la douane<br/>(O/N)</html>", "<html>&nbsp;<br/>Remarques<br/>&nbsp;</html>"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.String.class, java.lang.Boolean.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        luggageTable.setCellSelectionEnabled(true);
        luggageTable.setFillsViewportHeight(true);
        luggageTable.setName(""); // NOI18N
        luggageTable.getTableHeader().setReorderingAllowed(false);
        luggageTable.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                luggageTablePropertyChange(evt);
            }
        });
        jScrollPane.setViewportView(luggageTable);
        luggageTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (luggageTable.getColumnModel().getColumnCount() > 0) {
            luggageTable.getColumnModel().getColumn(0).setResizable(false);
            luggageTable.getColumnModel().getColumn(0).setPreferredWidth(200);
            luggageTable.getColumnModel().getColumn(1).setResizable(false);
            luggageTable.getColumnModel().getColumn(1).setPreferredWidth(20);
            luggageTable.getColumnModel().getColumn(2).setResizable(false);
            luggageTable.getColumnModel().getColumn(2).setPreferredWidth(30);
            luggageTable.getColumnModel().getColumn(3).setResizable(false);
            luggageTable.getColumnModel().getColumn(3).setPreferredWidth(30);
            luggageTable.getColumnModel().getColumn(4).setResizable(false);
            luggageTable.getColumnModel().getColumn(4).setPreferredWidth(10);
            luggageTable.getColumnModel().getColumn(5).setResizable(false);
            luggageTable.getColumnModel().getColumn(5).setPreferredWidth(15);
            luggageTable.getColumnModel().getColumn(6).setResizable(false);
            luggageTable.getColumnModel().getColumn(6).setPreferredWidth(300);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(titleLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(flightLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 819, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(titleLabel)
                    .addComponent(flightLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	/**
	 * https://tips4java.wordpress.com/2009/06/07/table-cell-listener/
	 *
	 * @param evt
	 */
    private void luggageTablePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_luggageTablePropertyChange
		if ("tableCellEditor".equals(evt.getPropertyName())) {
			final JTable table = (JTable) evt.getSource();
			if (table.isEditing()) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						oldValue = table.getValueAt(table.getEditingRow(), table.getEditingColumn());
					}
				});
			} else {
				int column = table.getEditingColumn();
				int row = table.getEditingRow();
				newValue = table.getValueAt(row, column);
				if (!( newValue.toString() ).equals(( oldValue.toString() ))) {
					Luggage l = luggages.get(row);
					System.out.println("Debug : " + l);
					switch (column) {
						case 3: //RECEIVE_LUGGAGE
							sendReceived(l, (boolean)newValue, row, column);
							break;
						case 4: //LOAD_LUGGAGE
							sendLoaded(l, newValue.toString().charAt(0), row, column);
							break;
						case 5: //CUSTOM_CHECK
							sendCustomCheck(l, (boolean)newValue, row, column);
							break;
						case 6: //ADD_COMMENT
							sendComment(l, newValue.toString(), row, column);
							break;
						default:
							System.err.println("This shouldn't happen!!!");
							break;
					}
				}
			}
		}
    }//GEN-LAST:event_luggageTablePropertyChange

	/**
	 * 
	 * @param evt
	 */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        boolean ok = true;
		int showConfirmDialog = -1;
		for (int i = 0; i < luggages.size() && ok; i++) {
			if (luggages.get(i).getLoaded() == 'N') ok = false;
		}
		if (!ok) {
			showConfirmDialog = JOptionPane.showConfirmDialog(this, "Tous les bagages n'ont pas été chargé en soute !",
				"Attention!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			System.out.println("application.luggage.AppLuggageDialog.formWindowClosing() : "+showConfirmDialog);
		}
		if (ok || showConfirmDialog == 0) {
			this.dispose();
		}
    }//GEN-LAST:event_formWindowClosing

	/**
	 *
	 * @param l
	 * @param received
	 */
	private void sendReceived(Luggage l, boolean received, int row, int column) {
		Object[] cu = {l.getId(), received};
		ResponseLUGAP resp = (ResponseLUGAP) ((AppLuggageForm) getParent())
			.connectToServer(new RequestLUGAP(RequestLUGAP.RECEIVE_LUGGAGE, cu));
		if (resp != null && resp.getCode() == ResponseLUGAP.RECEIVE_LUGGAGE_OK) {
			l.setReceived(received);
		} else {
			JOptionPane.showMessageDialog(this, "La mise à jour a échoué", "Erreur", JOptionPane.ERROR_MESSAGE);
			luggageTable.setValueAt(l.isReceived(), row, column);
		}
	}

	/**
	 *
	 * @param l
	 * @param loaded
	 */
	private void sendLoaded(Luggage l, char loaded, int row, int column) {
		Object[] cu = {l.getId(), loaded};
		ResponseLUGAP resp = (ResponseLUGAP) ((AppLuggageForm) getParent())
			.connectToServer(new RequestLUGAP(RequestLUGAP.LOAD_LUGGAGE, cu));
		if (resp != null && resp.getCode() == ResponseLUGAP.LOAD_LUGGAGE_OK) {
			l.setLoaded(loaded);
		} else {
			JOptionPane.showMessageDialog(this, "La mise à jour a échoué", "Erreur", JOptionPane.ERROR_MESSAGE);
			luggageTable.setValueAt(l.getLoaded(), row, column);
		}
	}

	/**
	 *
	 * @param l
	 * @param checked
	 */
	private void sendCustomCheck(Luggage l, boolean checked, int row, int column) {
		Object[] cu = {l.getId(), checked};
		ResponseLUGAP resp = (ResponseLUGAP) ((AppLuggageForm) getParent())
			.connectToServer(new RequestLUGAP(RequestLUGAP.CUSTOM_CHECK, cu));
		if (resp != null && resp.getCode() == ResponseLUGAP.CUSTOM_CHECK_OK) {
			l.setCustomChecked(checked);
		} else {
			JOptionPane.showMessageDialog(this, "La mise à jour a échoué", "Erreur", JOptionPane.ERROR_MESSAGE);
			luggageTable.setValueAt(l.isCustomChecked(), row, column);
		}
	}

	/**
	 *
	 * @param l
	 * @param comment
	 */
	private void sendComment(Luggage l, String comment, int row, int column) {
		Object[] cu = {l.getId(), comment};
		ResponseLUGAP resp = (ResponseLUGAP) ((AppLuggageForm) getParent())
			.connectToServer(new RequestLUGAP(RequestLUGAP.ADD_COMMENT, cu));
		if (resp != null && resp.getCode() == ResponseLUGAP.ADD_COMMENT_OK) {
			l.setComments(comment);
		} else {
			JOptionPane.showMessageDialog(this, "La mise à jour a échoué", "Erreur", JOptionPane.ERROR_MESSAGE);
			luggageTable.setValueAt(l.getComments(), row, column);
		}
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel flightLabel;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JTable luggageTable;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
