import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public abstract class GameScreen extends JFrame {

    

    protected int getNumberOfPlayers() {
        int numGiocatori = 0;
        boolean inputValido = false;

        while (!inputValido) {
            String input = JOptionPane.showInputDialog(this, "Quanti giocatori vogliono partecipare? (2-4)", "Numero di Giocatori", JOptionPane.QUESTION_MESSAGE);
            if (input == null) {
                dispose();
                new HomeScreen().setVisible(true);
                return 0;
            }

            try {
                numGiocatori = Integer.parseInt(input);
                if (numGiocatori >= 2 && numGiocatori <= 4) {
                    inputValido = true;
                } else {
                    JOptionPane.showMessageDialog(this, "Per favore inserisci un numero tra 2 e 4.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Per favore inserisci un numero valido.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }

        return numGiocatori;
    }

    protected String[] getPlayerNames(int numGiocatori) {
        String[] playerNames = new String[numGiocatori];
        for (int i = 0; i < numGiocatori; i++) {
            String playerName = JOptionPane.showInputDialog(this, "Inserisci il nome del giocatore " + (i + 1) + ":");
            if (playerName == null) {
                dispose();
                new HomeScreen().setVisible(true);
                return new String[0];
            }
            playerNames[i] = playerName;
        }
        return playerNames;
    }

       public void GoToMenu() {
    int scelta = JOptionPane.showConfirmDialog(
        this,
        "Sei sicuro di voler tornare al menu?",
        "Conferma",
        JOptionPane.YES_NO_OPTION
    );

    if (scelta == JOptionPane.YES_OPTION) {
        dispose();
        new HomeScreen().setVisible(true);
    }
    // Se premi NO non succede nulla: resta sulla schermata attuale
    }

     // Reset dei campi (generalizzato)
    protected void resetFields(JCheckBox[] comboBoxList, DefaultTableModel tableModel) {
        for (JCheckBox cb : comboBoxList) cb.setSelected(false);

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            for (int j = 1; j < 9; j++) tableModel.setValueAt("", i, j);
            tableModel.setValueAt("", i, 9);
        }
    }

    // Calcolo del punteggio totale
    protected void calculateTotalScores(DefaultTableModel tableModel) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int totalScore = 0;
            for (int j = 1; j < 9; j++) {
                try {
                    String scoreStr = (String) tableModel.getValueAt(i, j);
                    if (scoreStr != null && !scoreStr.isEmpty()) {
                        totalScore += Integer.parseInt(scoreStr);
                    }
                } catch (NumberFormatException ignored) {}
            }
            tableModel.setValueAt(totalScore, i, 9);
        }
    }

}


