import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;

public class BurracoScreen extends GameScreen {

    private JTable table;
    private DefaultTableModel tableModel;
    private int punteggioLimite = 505;

    public BurracoScreen() {
        setTitle("Burraco - Segnapunti");
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        chiediPunteggioLimite();
        int numGiocatori = getNumberOfPlayers();
        if (numGiocatori == 0) return;

        String[] playerNames = getPlayerNames(numGiocatori);
        if (playerNames.length == 0) return;

        // Colonne: Giocatore, Punteggio 1-10, Totale
        String[] columnNames = new String[12];
        columnNames[0] = "Giocatore";
        for (int i = 1; i <= 10; i++) {
            columnNames[i] = "Punteggio " + i;
        }
        columnNames[11] = "Totale";

        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if ((int) tableModel.getValueAt(row, tableModel.getColumnCount() - 1) >= punteggioLimite) {
                    return false;
                }
                return super.isCellEditable(row, column);
            }
        };

        // Evidenzia in verde se raggiunge il limite
        table.getColumnModel().getColumn(tableModel.getColumnCount() - 1).setCellRenderer(new TableCellRenderer() {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = new JLabel(String.valueOf(value), JLabel.CENTER);
        int currentVal = (value instanceof Integer) ? (int) value : 0;

        // Trova il massimo punteggio tra quelli >= punteggioLimite
        int maxEligible = Integer.MIN_VALUE;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Object cellValue = tableModel.getValueAt(i, tableModel.getColumnCount() - 1);
            if (cellValue instanceof Integer) {
                int val = (int) cellValue;
                if (val >= punteggioLimite) {
                    maxEligible = Math.max(maxEligible, val);
                }
            }
        }

        if (currentVal == maxEligible && currentVal >= punteggioLimite) {
            label.setBackground(Color.GREEN);
            label.setOpaque(true);
        } else {
            label.setOpaque(false);
        }

        return label;
    }
});


        // Aggiungi righe per ogni giocatore
        for (String name : playerNames) {
            Object[] row = new Object[tableModel.getColumnCount()];
            row[0] = name;
            for (int j = 1; j < tableModel.getColumnCount() - 1; j++) {
                row[j] = "";
            }
            row[tableModel.getColumnCount() - 1] = 0;
            tableModel.addRow(row);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Segnapunti"));

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

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void chiediPunteggioLimite() {
        JDialog dialog = new JDialog(this, "Punteggio Limite", true);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setSize(350, 150);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JLabel label = new JLabel("Scegli il punteggio limite per uscire dal gioco:", JLabel.CENTER);
        dialog.add(label, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        JButton button505 = new JButton("505");
        JButton button1005 = new JButton("1005");
        JButton button2005 = new JButton("2005");

        button505.addActionListener(e -> {
            punteggioLimite = 505;
            dialog.dispose();
        });

        button1005.addActionListener(e -> {
            punteggioLimite = 1005;
            dialog.dispose();
        });

        button2005.addActionListener(e -> {
            punteggioLimite = 2005;
            dialog.dispose();
        });

        buttonPanel.add(button505);
        buttonPanel.add(button1005);
        buttonPanel.add(button2005);
        dialog.add(buttonPanel, BorderLayout.CENTER);

        dialog.setVisible(true);
    }

    protected void calculateTotalScores(DefaultTableModel tableModel) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int totalScore = 0;
            for (int j = 1; j < tableModel.getColumnCount() - 1; j++) {
                try {
                    String scoreStr = (String) tableModel.getValueAt(i, j);
                    if (scoreStr != null && !scoreStr.isEmpty()) {
                        totalScore += Integer.parseInt(scoreStr);
                    }
                } catch (NumberFormatException ignored) {}
            }
            tableModel.setValueAt(totalScore, i, tableModel.getColumnCount() - 1);
        }
        table.repaint();
    }

    protected void resetFields(JCheckBox[] comboBoxList, DefaultTableModel tableModel) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            for (int j = 1; j < tableModel.getColumnCount() - 1; j++) {
                tableModel.setValueAt("", i, j);
            }
            tableModel.setValueAt(0, i, tableModel.getColumnCount() - 1);
        }
        table.repaint();
    }
}