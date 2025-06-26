package memoria;

import javax.swing.*;
import java.awt.*;

/**
 * Janela Swing que atua como console ao vivo, sem usar lambdas.
 */
public class Console {
    private static JTextArea textArea;

    /** Inicializa e exibe a janela de console */
    public static void init() {
        JFrame frame = new JFrame("Console de Operações");
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(textArea);
        frame.getContentPane().add(scroll, BorderLayout.CENTER);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /** Adiciona uma linha de texto ao console */
    public static void log(final String message) {
        if (textArea == null) {
            init();
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                textArea.append(message + "\n");
                textArea.setCaretPosition(textArea.getDocument().getLength());
            }
        });
    }
}
