package application.datamining.rserve;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import statistics.exception.RServeStatsException;
import statistics.jfreechart.StatGraphics;
import statistics.rserve.RServeStats;

/**
 * An application for statistical operation using RServe and JFreeCart.
 * Can do ANOVA (with 1 or 2 factors) or regression-correlation (simple or multiple)
 *
 * @author Morgan
 */
public class DataMiningForm extends javax.swing.JFrame {

	private RServeStats stats = null;
	private String file;

	/**
	 * Creates new form DataMiningForm.
	 * Connects to RServe.
	 */
	public DataMiningForm() {
		initComponents();
		try {
			stats = new RServeStats();
		} catch (RServeStatsException ex) {
			System.err.println("Error : " + ex.getMessage());
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

        statTypeBG = new javax.swing.ButtonGroup();
        chooseFilePanel = new javax.swing.JPanel();
        chooseFileLabel = new javax.swing.JLabel();
        chooseFileTF = new javax.swing.JTextField();
        chooseFileButton = new javax.swing.JButton();
        sepLabel = new javax.swing.JLabel();
        sepTF = new javax.swing.JTextField();
        readDataButton = new javax.swing.JButton();
        namesPanel = new javax.swing.JPanel();
        namesLabel = new javax.swing.JLabel();
        namesSP = new javax.swing.JScrollPane();
        namesTP = new javax.swing.JTextPane();
        varPanel = new javax.swing.JPanel();
        typeLabel = new javax.swing.JLabel();
        simpRegCorrRB = new javax.swing.JRadioButton();
        multRegCorrRB = new javax.swing.JRadioButton();
        anova1RB = new javax.swing.JRadioButton();
        anova2RB = new javax.swing.JRadioButton();
        varExpliqueeLabel = new javax.swing.JLabel();
        varExpliqueeTF = new javax.swing.JTextField();
        varExplicativeLabel = new javax.swing.JLabel();
        varExplicativeTF = new javax.swing.JTextField();
        startButton = new javax.swing.JButton();
        limitLabel = new javax.swing.JLabel();
        limitTF = new javax.swing.JTextField();
        resultsPanel = new javax.swing.JPanel();
        resultsLabel = new javax.swing.JLabel();
        resultsSP = new javax.swing.JScrollPane();
        resultsTP = new javax.swing.JTextPane();
        graphPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Application Data Mining (R Serve)");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        chooseFileLabel.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        chooseFileLabel.setText("Choisir le fichier : ");

        chooseFileButton.setText("Parcourir...");
        chooseFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseFileButtonActionPerformed(evt);
            }
        });

        sepLabel.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        sepLabel.setText("Séparateur :");

        sepTF.setText(";");

        readDataButton.setText("Lire les données");
        readDataButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                readDataButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout chooseFilePanelLayout = new javax.swing.GroupLayout(chooseFilePanel);
        chooseFilePanel.setLayout(chooseFilePanelLayout);
        chooseFilePanelLayout.setHorizontalGroup(
            chooseFilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chooseFilePanelLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(chooseFileLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chooseFileTF, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chooseFileButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sepLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sepTF, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(readDataButton)
                .addContainerGap())
        );
        chooseFilePanelLayout.setVerticalGroup(
            chooseFilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chooseFilePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(chooseFilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chooseFileLabel)
                    .addComponent(chooseFileTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chooseFileButton)
                    .addComponent(sepLabel)
                    .addComponent(sepTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(readDataButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        namesLabel.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        namesLabel.setText("Noms :");

        namesTP.setEditable(false);
        namesSP.setViewportView(namesTP);

        javax.swing.GroupLayout namesPanelLayout = new javax.swing.GroupLayout(namesPanel);
        namesPanel.setLayout(namesPanelLayout);
        namesPanelLayout.setHorizontalGroup(
            namesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(namesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(namesPanelLayout.createSequentialGroup()
                        .addComponent(namesLabel)
                        .addGap(0, 145, Short.MAX_VALUE))
                    .addComponent(namesSP))
                .addContainerGap())
        );
        namesPanelLayout.setVerticalGroup(
            namesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(namesLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(namesSP)
                .addContainerGap())
        );

        typeLabel.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        typeLabel.setText("Type de calcul :");

        statTypeBG.add(simpRegCorrRB);
        simpRegCorrRB.setSelected(true);
        simpRegCorrRB.setText("Régression-Corrélation Linéaire Simple");

        statTypeBG.add(multRegCorrRB);
        multRegCorrRB.setText("Régression-Corrélation Linéaire Multiple");

        statTypeBG.add(anova1RB);
        anova1RB.setText("Analyse de variance à 1 facteur");

        statTypeBG.add(anova2RB);
        anova2RB.setText("Analyse de variance à 2 facteurs");

        varExpliqueeLabel.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        varExpliqueeLabel.setText("Variable expliquée :");

        varExplicativeLabel.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        varExplicativeLabel.setText("Variable(s) explicative(s) [séparées par des \" ; \"] :");

        startButton.setText("Commencer");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        limitLabel.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        limitLabel.setText("Seuil :");

        limitTF.setText("0.05");

        javax.swing.GroupLayout varPanelLayout = new javax.swing.GroupLayout(varPanel);
        varPanel.setLayout(varPanelLayout);
        varPanelLayout.setHorizontalGroup(
            varPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(varPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(varPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(typeLabel, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, varPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(varPanelLayout.createSequentialGroup()
                            .addComponent(varExpliqueeLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(varExplicativeLabel))
                        .addGroup(varPanelLayout.createSequentialGroup()
                            .addGap(6, 6, 6)
                            .addGroup(varPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(varPanelLayout.createSequentialGroup()
                                    .addComponent(multRegCorrRB)
                                    .addGap(18, 18, 18)
                                    .addComponent(anova2RB))
                                .addGroup(varPanelLayout.createSequentialGroup()
                                    .addComponent(simpRegCorrRB)
                                    .addGap(18, 18, 18)
                                    .addComponent(anova1RB))
                                .addGroup(varPanelLayout.createSequentialGroup()
                                    .addComponent(varExpliqueeTF, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(varExplicativeTF)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, varPanelLayout.createSequentialGroup()
                        .addComponent(limitLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(limitTF, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(130, 130, 130)
                        .addComponent(startButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        varPanelLayout.setVerticalGroup(
            varPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(varPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(typeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(varPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(simpRegCorrRB)
                    .addComponent(anova1RB))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(varPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(multRegCorrRB)
                    .addComponent(anova2RB))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(varPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(varExpliqueeLabel)
                    .addComponent(varExplicativeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(varPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(varExpliqueeTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(varExplicativeTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(varPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startButton)
                    .addComponent(limitLabel)
                    .addComponent(limitTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        resultsLabel.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        resultsLabel.setText("Résultats :");

        resultsSP.setViewportView(resultsTP);

        javax.swing.GroupLayout resultsPanelLayout = new javax.swing.GroupLayout(resultsPanel);
        resultsPanel.setLayout(resultsPanelLayout);
        resultsPanelLayout.setHorizontalGroup(
            resultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resultsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(resultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(resultsSP)
                    .addGroup(resultsPanelLayout.createSequentialGroup()
                        .addComponent(resultsLabel)
                        .addGap(0, 400, Short.MAX_VALUE)))
                .addContainerGap())
        );
        resultsPanelLayout.setVerticalGroup(
            resultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resultsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(resultsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resultsSP, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                .addContainerGap())
        );

        graphPanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(namesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(resultsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(varPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(graphPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(chooseFilePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 538, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(chooseFilePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(namesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(varPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(resultsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(graphPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	/**
	 * Wipes the various Text Fields &, optionally, the graph panel.
	 *
	 * @param all : reset the results & graph too or not
	 */
	private void reset(boolean all) {
		if (all) {
			namesTP.setText("");
			varExpliqueeTF.setText("");
			varExplicativeTF.setText("");
		}
		resultsTP.setText("");
		graphPanel.removeAll();
		graphPanel.validate();
	}

	/**
	 * Open a File Chooser to select the file the data will be read from.
	 *
	 * @param evt
	 */
    private void chooseFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooseFileButtonActionPerformed
		String sep = System.getProperty("file.separator");
		JFileChooser fc = new JFileChooser(System.getProperty("user.dir") + sep + ".." + sep + "R");
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile().getAbsolutePath();
			chooseFileTF.setText(file);
		}
    }//GEN-LAST:event_chooseFileButtonActionPerformed

	/**
	 * Read the data from the file chosen and shows the summary.
	 *
	 * @param evt
	 */
    private void readDataButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_readDataButtonActionPerformed
		reset(true);
		String sep = sepTF.getText();
		String result = stats.readData(file, sep);
		namesTP.setText(result);
    }//GEN-LAST:event_readDataButtonActionPerformed

	/**
	 * Starts the operation indicated by the radio button with the variables
	 * provided.
	 *
	 * @param evt
	 */
    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
		reset(false);
		String varExpliquee = varExpliqueeTF.getText();
		String raw = varExplicativeTF.getText();
		if ("".equals(varExpliquee) || "".equals(raw)) {
			JOptionPane.showMessageDialog(this, "Il faut encoder le nom des variables",
				"Erreur", JOptionPane.ERROR_MESSAGE);
		} else {
			double limit;
			try {
				limit = Double.parseDouble(limitTF.getText());
			} catch (NumberFormatException e) {
				System.err.println("Error : " + e.getMessage());
				limit = 0.05;
			}
			stats.setLimit(limit);
			StringTokenizer st = new StringTokenizer(raw, ";");
			String varExplicative = st.nextToken();
			if (simpRegCorrRB.isSelected()) {
				HashMap<String, Object> data = stats.getDataForScatterPlotChart(varExpliquee, varExplicative);
				addChartToPanel(StatGraphics.buildScatterPlotChart(varExpliquee, varExplicative, data));
				resultsTP.setText(stats.simRegCorr(varExpliquee, varExplicative));
			} else if (multRegCorrRB.isSelected()) {
				String[] varExplicatives = new String[st.countTokens() + 1];
				varExplicatives[0] = varExplicative;
				for (int i = 1; st.hasMoreElements(); i++) {
					varExplicatives[i] = st.nextToken();
				}
				HashMap<String, Object> data = stats
					.getDataForMultipleScatterPlotsChart(varExpliquee, varExplicatives);
				addChartToPanel(StatGraphics.buildMultipleScatterPlotsChart(varExpliquee, data));
				resultsTP.setText(stats.multRegCorr(varExpliquee, varExplicatives));
			} else if (anova1RB.isSelected()) {
				HashMap<String, List> data = stats.getDataForBoxAndWiskersChart(varExpliquee, varExplicative);
				addChartToPanel(StatGraphics.buildBoxAndWiskersChart(varExpliquee, varExplicative, data));
				resultsTP.setText(stats.anova1(varExpliquee, varExplicative));
			} else if (anova2RB.isSelected()) {
				String varExplicative2 = st.nextToken();
				HashMap<String, HashMap<String, Double>> data = stats
					.getDataForMultipleLinesChart(varExpliquee, varExplicative, varExplicative2);
				addChartToPanel(StatGraphics.buildMultipleLinesChart(varExpliquee, varExplicative, data));
				resultsTP.setText(stats.anova2(varExpliquee, varExplicative, varExplicative2));
			} else {
				System.err.println("No type selected");
			}
		}
    }//GEN-LAST:event_startButtonActionPerformed

	/**
	 * 
	 * @param evt
	 */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        stats.closeRConnection();
    }//GEN-LAST:event_formWindowClosing

	/**
	 * 
	 * @param chart
	 */
	private void addChartToPanel(JFreeChart chart){
		ChartPanel chartPanel = new ChartPanel(chart);
		graphPanel.add(chartPanel, BorderLayout.CENTER); //The panel layout needs to be BorderLayout
		graphPanel.validate();
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
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(DataMiningForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(DataMiningForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(DataMiningForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(DataMiningForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				DataMiningForm dataMiningForm = new DataMiningForm();
				dataMiningForm.setLocationRelativeTo(null);
				dataMiningForm.setVisible(true);
			}
		});
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton anova1RB;
    private javax.swing.JRadioButton anova2RB;
    private javax.swing.JButton chooseFileButton;
    private javax.swing.JLabel chooseFileLabel;
    private javax.swing.JPanel chooseFilePanel;
    private javax.swing.JTextField chooseFileTF;
    private javax.swing.JPanel graphPanel;
    private javax.swing.JLabel limitLabel;
    private javax.swing.JTextField limitTF;
    private javax.swing.JRadioButton multRegCorrRB;
    private javax.swing.JLabel namesLabel;
    private javax.swing.JPanel namesPanel;
    private javax.swing.JScrollPane namesSP;
    private javax.swing.JTextPane namesTP;
    private javax.swing.JButton readDataButton;
    private javax.swing.JLabel resultsLabel;
    private javax.swing.JPanel resultsPanel;
    private javax.swing.JScrollPane resultsSP;
    private javax.swing.JTextPane resultsTP;
    private javax.swing.JLabel sepLabel;
    private javax.swing.JTextField sepTF;
    private javax.swing.JRadioButton simpRegCorrRB;
    private javax.swing.JButton startButton;
    private javax.swing.ButtonGroup statTypeBG;
    private javax.swing.JLabel typeLabel;
    private javax.swing.JLabel varExplicativeLabel;
    private javax.swing.JTextField varExplicativeTF;
    private javax.swing.JLabel varExpliqueeLabel;
    private javax.swing.JTextField varExpliqueeTF;
    private javax.swing.JPanel varPanel;
    // End of variables declaration//GEN-END:variables
}
