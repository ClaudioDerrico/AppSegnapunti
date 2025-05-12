import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Scala40Screen extends GameScreen {

    private JTable table;
    private DefaultTableModel tableModel;
    private int punteggioLimite = 101;  // Punteggio di uscita predefinito

    public Scala40Screen() {
        setTitle("Scala 40 - Segnapunti");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Fase 1: Chiedi il punteggio limite per l'uscita
        chiediPunteggioLimite();
        // Fase 2: Chiedi il numero di giocatori e i loro nomi
        int numGiocatori = getNumberOfPlayers();
        if (numGiocatori == 0) return;

        String[] playerNames = getPlayerNames(numGiocatori);
        if (playerNames.length == 0) return;

        

        // Inizializza le colonne: nome giocatore, 5 colonne di punteggio, e una per il totale
        String[] columnNames = {"Giocatore", "Punteggio 1", "Punteggio 2", "Punteggio 3", "Punteggio 4", "Punteggio 5", "Punteggio 6", "Punteggio 7", "Punteggio 8", "Totale"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Disabilita l'editing per la riga del giocatore che ha superato la soglia
                if ((int) tableModel.getValueAt(row, tableModel.getColumnCount() - 1) >= punteggioLimite) {
                    return false;
                }
                return super.isCellEditable(row, column);
            }
        };

        // Imposta il renderizzatore per la colonna "Totale"
        table.getColumnModel().getColumn(tableModel.getColumnCount() - 1).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(String.valueOf(value));
                if ((int) value >= punteggioLimite) {
                    label.setBackground(Color.RED);
                    label.setOpaque(true); // Necessario per colorare lo sfondo della cella
                } else {
                    label.setBackground(Color.WHITE);
                    label.setOpaque(false); // Ripristina lo sfondo bianco
                }
                return label;
            }
        });

        // Aggiungi i giocatori alla tabella
        for (int i = 0; i < numGiocatori; i++) {
            Object[] row = new Object[tableModel.getColumnCount()];
            row[0] = playerNames[i]; // Nome giocatore
            for (int j = 1; j < tableModel.getColumnCount() - 1; j++) { // Tutte le colonne tranne "Totale"
                row[j] = ""; // Imposta i punteggi iniziali vuoti
            }
            row[tableModel.getColumnCount() - 1] = 0;  // Imposta il punteggio totale iniziale a 0
            tableModel.addRow(row);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Segnapunti"));

        // Pulsanti
        JButton calcolaButton = new JButton("Calcola");
        calcolaButton.setBackground(Color.GREEN);
        calcolaButton.setForeground(Color.WHITE);
        calcolaButton.setFont(new Font("Arial", Font.BOLD, 14));
        calcolaButton.addActionListener((ActionEvent e) -> calculateTotalScores(tableModel));

        JButton resetButton = new JButton("Resetta");
        resetButton.setBackground(Color.RED);
        resetButton.setForeground(Color.WHITE);
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));
        resetButton.addActionListener((ActionEvent e) -> resetFields(null, tableModel));

        JButton menuButton = new JButton("Menu'");
        menuButton.setBackground(Color.BLUE);
        menuButton.setForeground(Color.WHITE);
        menuButton.setFont(new Font("Arial", Font.BOLD, 14));
        menuButton.addActionListener((ActionEvent e) -> GoToMenu());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(calcolaButton);
        bottomPanel.add(resetButton);
        bottomPanel.add(menuButton);

        // Layout
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

   private void chiediPunteggioLimite() {
    JDialog dialog = new JDialog(this, "Punteggio Limite", true); // true = modal
    dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // Impedisce chiusura con X
    dialog.setSize(300, 150);
    dialog.setLocationRelativeTo(this);
    dialog.setLayout(new BorderLayout());

    JLabel label = new JLabel("Scegli il punteggio limite per uscire dal gioco:", JLabel.CENTER);
    dialog.add(label, BorderLayout.NORTH);

    JPanel buttonPanel = new JPanel();
    JButton button101 = new JButton("101");
    JButton button201 = new JButton("201");

    button101.addActionListener(e -> {
        punteggioLimite = 101;
        dialog.dispose();
    });

    button201.addActionListener(e -> {
        punteggioLimite = 201;
        dialog.dispose();
    });

    buttonPanel.add(button101);
    buttonPanel.add(button201);
    dialog.add(buttonPanel, BorderLayout.CENTER);

    dialog.setVisible(true); // Blocca finché l'utente non sceglie
}

  

    // Calcola il punteggio totale per ogni giocatore
    protected void calculateTotalScores(DefaultTableModel tableModel) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int totalScore = 0;
            for (int j = 1; j < tableModel.getColumnCount() - 1; j++) { // Non calcoliamo sulla colonna "Totale"
                try {
                    String scoreStr = (String) tableModel.getValueAt(i, j);
                    if (scoreStr != null && !scoreStr.isEmpty()) {
                        totalScore += Integer.parseInt(scoreStr);
                    }
                } catch (NumberFormatException ignored) {}
            }
            tableModel.setValueAt(totalScore, i, tableModel.getColumnCount() - 1); // Imposta il punteggio totale
        }

        // Rende visibile l'aggiornamento del colore rosso per la colonna totale
        table.repaint();
    }

    // Reset dei campi
    protected void resetFields(JCheckBox[] comboBoxList, DefaultTableModel tableModel) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            for (int j = 1; j < tableModel.getColumnCount(); j++) {
                tableModel.setValueAt("", i, j);  // Azzera i punteggi
            }
            tableModel.setValueAt(0, i, tableModel.getColumnCount() - 1);  // Resetta il punteggio totale a 0
        }
        // Ripristina il colore di sfondo
        table.repaint();
    }
}
