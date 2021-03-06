package application.datamining;

import db.airport.models.Airline;
import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import requestresponse.impl.LUGANAP.RequestLUGANAP;
import requestresponse.impl.LUGANAP.ResponseLUGANAP;
import requestresponse.multithread.abstracts.AbstractApplicationForm;
import statistics.jfreechart.StatGraphics;

/**
 *
 * @author Morgan
 */
public class AppDataMiningForm extends AbstractApplicationForm {

	//Application variables
	private ArrayList<Airline> airlines;

	/**
	 * Creates new form AppDataMiningForm
	 */
	public AppDataMiningForm() {
		super("appdatamining.properties", true);
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        typeBG = new javax.swing.ButtonGroup();
        varPanel = new javax.swing.JPanel();
        varLabel = new javax.swing.JLabel();
        yearLabel = new javax.swing.JLabel();
        yearTF = new javax.swing.JTextField();
        monthLabel = new javax.swing.JLabel();
        monthCB = new javax.swing.JComboBox<>();
        airlineLabel = new javax.swing.JLabel();
        airlineCB = new javax.swing.JComboBox<>();
        typePanel = new javax.swing.JPanel();
        typeLabel = new javax.swing.JLabel();
        regCorrLugRB = new javax.swing.JRadioButton();
        nbAccCheckB = new javax.swing.JCheckBox();
        ageCheckB = new javax.swing.JCheckBox();
        anovaRB = new javax.swing.JRadioButton();
        genderCheckB = new javax.swing.JCheckBox();
        goButton = new javax.swing.JButton();
        resultsPanel = new javax.swing.JPanel();
        resultsLabel = new javax.swing.JLabel();
        resultsScrollPane = new javax.swing.JScrollPane();
        resultsTP = new javax.swing.JTextPane();
        graphPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        varLabel.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        varLabel.setText("Paramètres de la requète");

        yearLabel.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        yearLabel.setText("Année :");

        monthLabel.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        monthLabel.setText("Mois :");

        monthCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Aucun --", "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre" }));

        airlineLabel.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        airlineLabel.setText("Companie aérienne :");

        airlineCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout varPanelLayout = new javax.swing.GroupLayout(varPanel);
        varPanel.setLayout(varPanelLayout);
        varPanelLayout.setHorizontalGroup(
            varPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(varPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(varPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(varPanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(yearLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(yearTF, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(monthLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(monthCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(airlineLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(airlineCB, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(varLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        varPanelLayout.setVerticalGroup(
            varPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(varPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(varLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(varPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(yearLabel)
                    .addComponent(yearTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(monthLabel)
                    .addComponent(monthCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(airlineLabel)
                    .addComponent(airlineCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        typeLabel.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        typeLabel.setText("Type de la requète");

        typeBG.add(regCorrLugRB);
        regCorrLugRB.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        regCorrLugRB.setSelected(true);
        regCorrLugRB.setText("Relation poids des bagages / distance du vol");
        regCorrLugRB.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                regCorrLugRBItemStateChanged(evt);
            }
        });

        nbAccCheckB.setText("+ nombre d'accompagnants");

        ageCheckB.setText("+ age du voyageur");

        typeBG.add(anovaRB);
        anovaRB.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        anovaRB.setText("Relation poids des bagages / zone de destination");
        anovaRB.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                anovaRBItemStateChanged(evt);
            }
        });

        genderCheckB.setText("+ sexe du voyageur");
        genderCheckB.setEnabled(false);

        goButton.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        goButton.setText("Commencer");
        goButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout typePanelLayout = new javax.swing.GroupLayout(typePanel);
        typePanel.setLayout(typePanelLayout);
        typePanelLayout.setHorizontalGroup(
            typePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(typePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(typePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(typeLabel)
                    .addGroup(typePanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(typePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(regCorrLugRB)
                            .addGroup(typePanelLayout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(typePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ageCheckB)
                                    .addComponent(nbAccCheckB))))
                        .addGap(18, 18, 18)
                        .addGroup(typePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(anovaRB)
                            .addGroup(typePanelLayout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(genderCheckB)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, typePanelLayout.createSequentialGroup()
                .addGap(580, 580, 580)
                .addComponent(goButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        typePanelLayout.setVerticalGroup(
            typePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(typePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(typeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(typePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(regCorrLugRB)
                    .addComponent(anovaRB))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(typePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nbAccCheckB)
                    .addComponent(genderCheckB))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ageCheckB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(goButton))
        );

        resultsLabel.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        resultsLabel.setText("Résultats");

        resultsScrollPane.setViewportView(resultsTP);

        graphPanel.setLayout(new java.awt.GridLayout(2, 0));

        javax.swing.GroupLayout resultsPanelLayout = new javax.swing.GroupLayout(resultsPanel);
        resultsPanel.setLayout(resultsPanelLayout);
        resultsPanelLayout.setHorizontalGroup(
            resultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resultsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(resultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(resultsPanelLayout.createSequentialGroup()
                        .addComponent(resultsLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(resultsPanelLayout.createSequentialGroup()
                        .addComponent(resultsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(graphPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        resultsPanelLayout.setVerticalGroup(
            resultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resultsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(resultsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(resultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(graphPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(resultsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(varPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(typePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(resultsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(varPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(typePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resultsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	/**
	 *
	 * @param evt
	 */
    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
		ResponseLUGANAP res = (ResponseLUGANAP) connectToServer(
			new RequestLUGANAP(RequestLUGANAP.GET_AIRLINES, ""));
		if (res != null) {
			if (res.getCode() == ResponseLUGANAP.GET_AIRLINES_OK) {
				airlines = (ArrayList<Airline>) res.getChargeUtile();
				DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
				dcbm.addElement("-- Aucune --");
				for (Airline airline : airlines) {
					dcbm.addElement(airline.toString());
				}
				airlineCB.setModel(dcbm);
				airlineCB.setSelectedIndex(0);
			} else {
				JOptionPane.showMessageDialog(this, res.getChargeUtile(),
					"Erreur", JOptionPane.ERROR_MESSAGE);
			}
		}
    }//GEN-LAST:event_formWindowOpened

	/**
	 *
	 * @param evt
	 */
    private void regCorrLugRBItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_regCorrLugRBItemStateChanged
		System.out.println("application.datamining.AppDataMiningForm.regCorrLugRBItemStateChanged()");
		nbAccCheckB.setEnabled(regCorrLugRB.isSelected());
		ageCheckB.setEnabled(regCorrLugRB.isSelected());
    }//GEN-LAST:event_regCorrLugRBItemStateChanged

	/**
	 *
	 * @param evt
	 */
    private void anovaRBItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_anovaRBItemStateChanged
		System.out.println("application.datamining.AppDataMiningForm.anovaRBItemStateChanged()");
		genderCheckB.setEnabled(anovaRB.isSelected());
    }//GEN-LAST:event_anovaRBItemStateChanged

	/**
	 * 
	 */
	private void reset() {
		resultsTP.setText("");
		graphPanel.removeAll();
		graphPanel.validate();
		graphPanel.repaint(); //https://stackoverflow.com/a/5812981
	}

	/**
	 *
	 * @param evt
	 */
    private void goButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goButtonActionPerformed
		reset();
		try {
			String yearString = yearTF.getText();
			if ("".equals(yearString)) {
				JOptionPane.showMessageDialog(this, "Il faut une année !",
					"Erreur", JOptionPane.ERROR_MESSAGE);
			} else {
				int year = Integer.parseInt(yearString);
				int month = monthCB.getSelectedIndex();
				int a = airlineCB.getSelectedIndex();
				String airline = (a != 0) ? airlineCB.getItemAt(a) : null;
				Object[] cu = {year, month, airline};
				if (regCorrLugRB.isSelected()) {
					if (nbAccCheckB.isSelected() || ageCheckB.isSelected()) {
						Object[] cu2 = {year, month, airline,
							nbAccCheckB.isSelected(), ageCheckB.isSelected()};
						regCorrLugPlus(cu2);
					} else regCorrLug(cu);
				} else {
					if (genderCheckB.isSelected()) anova2LugHF(cu);
					else anova1Lug(cu);
				}
			}
		} catch (NumberFormatException e) {
			System.err.println("Error : "+ e.getMessage());
			JOptionPane.showMessageDialog(this, "Le format de l'année est incorrect",
				"Erreur", JOptionPane.ERROR_MESSAGE);
		} catch (HeadlessException e) {
			System.err.println("Error : " + e.getMessage());
		}
    }//GEN-LAST:event_goButtonActionPerformed

	/**
	 * 
	 * @param toSend
	 */
	private void regCorrLug(Object[] toSend) {
		ResponseLUGANAP res = (ResponseLUGANAP) connectToServer(
			new RequestLUGANAP(RequestLUGANAP.REG_CORR_LUG, toSend));
		if (res != null) {
			if (res.getCode() == ResponseLUGANAP.REG_CORR_LUG_FAIL) {
				JOptionPane.showMessageDialog(this, res.getChargeUtile(),
					"Erreur", JOptionPane.ERROR_MESSAGE);
			} else {
				Object[] received = (Object[]) res.getChargeUtile();
				resultsTP.setText((String) received[0]);
				//Scatter plot
				HashMap<String, Object> dataPlot = (HashMap<String, Object>) received[1];
				if (dataPlot != null) {
					JFreeChart scatterPlot = StatGraphics.buildScatterPlotChart(
						"MeanWeight", "Distance", dataPlot);

					graphPanel.add(new ChartPanel(scatterPlot));
				}
				//Bar plot
				HashMap<String, HashMap<Integer, Double>> dataBar =
					(HashMap<String, HashMap<Integer, Double>>) received[2];
				if (dataBar != null) {
					JFreeChart barChart = StatGraphics.buildBarChart(
						"MeanWeight", "Distance", dataBar);
					graphPanel.add(new ChartPanel(barChart));
				}
				graphPanel.validate();
			}
		}
	}

	/**
	 *
	 * @param toSend
	 */
	private void regCorrLugPlus(Object[] toSend) {
		ResponseLUGANAP res = (ResponseLUGANAP) connectToServer(
			new RequestLUGANAP(RequestLUGANAP.REG_CORR_LUG_PLUS, toSend));
		if (res != null) {
			if (res.getCode() == ResponseLUGANAP.REG_CORR_LUG_PLUS_FAIL) {
				JOptionPane.showMessageDialog(this, res.getChargeUtile(),
					"Erreur", JOptionPane.ERROR_MESSAGE);
			} else {
				resultsTP.setText((String) res.getChargeUtile());
			}
		}
	}

	/**
	 *
	 * @param toSend
	 */
	private void anova1Lug(Object[] toSend) {
		ResponseLUGANAP res = (ResponseLUGANAP) connectToServer(
			new RequestLUGANAP(RequestLUGANAP.ANOVA_1_LUG, toSend));
		if (res != null) {
			if (res.getCode() == ResponseLUGANAP.ANOVA_1_LUG_FAIL) {
				JOptionPane.showMessageDialog(this, res.getChargeUtile(),
					"Erreur", JOptionPane.ERROR_MESSAGE);
			} else {
				Object[] received = (Object[]) res.getChargeUtile();
				resultsTP.setText((String) received[0]);
				//BoxAndWiskers
				HashMap<String, List> dataBox = (HashMap<String, List>) received[1];
				if (dataBox != null) {
					JFreeChart boxAndWiskers = StatGraphics.buildBoxAndWiskersChart(
					"MeanWeight", "Zone", dataBox);
					graphPanel.add(new ChartPanel(boxAndWiskers));
				}
				//Pie chart
				HashMap<String, Double> dataPie = (HashMap<String, Double>) received[2];
				if (dataPie != null) {
					JFreeChart pieChart = StatGraphics.buildPieChart(
					"TotalWeight", "Zone", dataPie);
					graphPanel.add(new ChartPanel(pieChart));
				}
				graphPanel.validate();
			}
		}
	}

	/**
	 *
	 * @param toSend
	 */
	private void anova2LugHF(Object[] toSend) {
		ResponseLUGANAP res = (ResponseLUGANAP) connectToServer(
			new RequestLUGANAP(RequestLUGANAP.ANOVA_2_LUG_HF, toSend));
		if (res != null) {
			if (res.getCode() == ResponseLUGANAP.ANOVA_2_LUG_HF_FAIL) {
				JOptionPane.showMessageDialog(this, res.getChargeUtile(),
					"Erreur", JOptionPane.ERROR_MESSAGE);
			} else {
				resultsTP.setText((String) res.getChargeUtile());
			}
		}
	}

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
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(AppDataMiningForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				AppDataMiningForm admf = new AppDataMiningForm();
				admf.setLocationRelativeTo(null);
				admf.setVisible(true);
			}
		});
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox ageCheckB;
    private javax.swing.JComboBox<String> airlineCB;
    private javax.swing.JLabel airlineLabel;
    private javax.swing.JRadioButton anovaRB;
    private javax.swing.JCheckBox genderCheckB;
    private javax.swing.JButton goButton;
    private javax.swing.JPanel graphPanel;
    private javax.swing.JComboBox<String> monthCB;
    private javax.swing.JLabel monthLabel;
    private javax.swing.JCheckBox nbAccCheckB;
    private javax.swing.JRadioButton regCorrLugRB;
    private javax.swing.JLabel resultsLabel;
    private javax.swing.JPanel resultsPanel;
    private javax.swing.JScrollPane resultsScrollPane;
    private javax.swing.JTextPane resultsTP;
    private javax.swing.ButtonGroup typeBG;
    private javax.swing.JLabel typeLabel;
    private javax.swing.JPanel typePanel;
    private javax.swing.JLabel varLabel;
    private javax.swing.JPanel varPanel;
    private javax.swing.JLabel yearLabel;
    private javax.swing.JTextField yearTF;
    // End of variables declaration//GEN-END:variables
}
