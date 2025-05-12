import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CariocaScreen extends GameScreen {

    private JCheckBox[] comboBoxList;
    private JTable table;
    private DefaultTableModel tableModel;


    public CariocaScreen() {
        setTitle("Carioca - Segnapunti");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Usa metodi ereditati da GameScreen
        int numGiocatori = getNumberOfPlayers();
        if (numGiocatori == 0) return;

        String[] playerNames = getPlayerNames(numGiocatori);
        if (playerNames.length == 0) return;

        // CheckBox
        String[] combinazioni = {
            "Coppia", "Doppia Coppia", "Tris", "Full",
            "Poker", "Scala Reale", "Scala 40", "Chiusura"
        };

        JPanel checkboxPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        checkboxPanel.setBorder(BorderFactory.createTitledBorder("Combinazioni"));
        comboBoxList = new JCheckBox[combinazioni.length];

        for (int i = 0; i < combinazioni.length; i++) {
            comboBoxList[i] = new JCheckBox(combinazioni[i]);
            checkboxPanel.add(comboBoxList[i]);
        }

        // Tabella
        String[] columnNames = {
            "Giocatore", "Coppia", "Doppia Coppia", "Tris", "Full",
            "Poker", "Scala Reale", "Scala 40", "Chiusura", "Punteggio Totale"
        };

        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        for (int i = 0; i < numGiocatori; i++) {
            Object[] row = new Object[10];
            row[0] = playerNames[i];
            for (int j = 1; j < 9; j++) row[j] = "";
            row[9] = "";
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
        resetButton.addActionListener((ActionEvent e) -> resetFields(comboBoxList, tableModel));

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
        add(checkboxPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

}
