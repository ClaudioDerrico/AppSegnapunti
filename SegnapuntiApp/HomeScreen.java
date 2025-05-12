import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class HomeScreen extends JFrame {

    public HomeScreen() {
        setTitle("Segnapunti Giochi di Carte");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(0, 128, 0));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50));

        Font buttonFont = new Font("Arial", Font.BOLD, 18);

        JButton cariocaBtn = createStyledButton("Carioca", buttonFont);
        JButton scalaBtn = createStyledButton("Scala 40", buttonFont);
        JButton burracoBtn = createStyledButton("Burraco", buttonFont);

        cariocaBtn.addActionListener((ActionEvent e) -> {
            this.dispose();  // Chiudi la finestra corrente (HomeScreen)
            new CariocaScreen();  // Apre la finestra CariocaScreen
        });

        scalaBtn.addActionListener((ActionEvent e) -> {
            this.dispose();  // Chiudi la finestra corrente (HomeScreen)
            new Scala40Screen();  // Apre la finestra Scala40Screen
        });

        burracoBtn.addActionListener((ActionEvent e) -> {
            this.dispose();  // Chiudi la finestra corrente (HomeScreen)
            new BurracoScreen();  // Apre la finestra BurracoScreen
        });

        mainPanel.add(cariocaBtn);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(scalaBtn);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(burracoBtn);

        add(mainPanel);
    }

    private JButton createStyledButton(String text, Font font) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(font);
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setPreferredSize(new Dimension(200, 50));
        button.setMaximumSize(new Dimension(200, 50));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HomeScreen().setVisible(true);
        });
    }
}
