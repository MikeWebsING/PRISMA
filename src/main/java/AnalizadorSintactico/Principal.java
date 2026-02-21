package AnalizadorSintactico;

import AnalizadorLexico.AnalizadorLexico;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;

public class Principal extends JFrame {
    private JTextArea areaCodigo;
    private JTextArea areaSalida;
    private JButton botonRun;

    private final Color COLOR_FONDO = new Color(20, 35, 45);
    private final Color COLOR_PANEL = new Color(30, 50, 60);
    private final Color COLOR_TEXTO = new Color(200, 220, 220);
    private final Color COLOR_ACCENTO = new Color(40, 180, 130);

    public Principal() {
        setTitle("IDE PRISMA - Compilador Profesional");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout(15, 15));

        configurarInterfaz();
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));
    }

    private void configurarInterfaz() {
        JPanel panelSup = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSup.setOpaque(false);
        botonRun = new JButton("RUN");
        botonRun.setPreferredSize(new Dimension(100, 40));
        botonRun.setBackground(COLOR_ACCENTO);
        botonRun.setForeground(Color.WHITE);
        botonRun.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botonRun.addActionListener(e -> ejecutarAnalisis());
        panelSup.add(botonRun);
        add(panelSup, BorderLayout.NORTH);

        areaCodigo = new JTextArea();
        areaCodigo.setBackground(COLOR_PANEL);
        areaCodigo.setForeground(COLOR_TEXTO);
        areaCodigo.setFont(new Font("Consolas", Font.PLAIN, 16));
        areaCodigo.setCaretColor(Color.WHITE);
        areaCodigo.setMargin(new Insets(10, 10, 10, 10));

        areaSalida = new JTextArea();
        areaSalida.setEditable(false);
        areaSalida.setBackground(new Color(10, 20, 25));
        areaSalida.setForeground(new Color(100, 255, 218));
        areaSalida.setFont(new Font("Monospaced", Font.PLAIN, 14));
        areaSalida.setMargin(new Insets(10, 10, 10, 10));

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(areaCodigo),
                new JScrollPane(areaSalida));
        split.setDividerLocation(400);
        split.setOpaque(false);
        add(split, BorderLayout.CENTER);
    }

    private void ejecutarAnalisis() {
        areaSalida.setText("Analizando...\n");
        try {
            InputStream stream = new ByteArrayInputStream(areaCodigo.getText().getBytes());
            System.setIn(stream);

            AnalizadorLexico lex = new AnalizadorLexico();
            AnalizadorSintactico parser = new AnalizadorSintactico(lex);
            parser.analizar();

            areaSalida.append(">>> EXITO: Sintaxis validada correctamente.");
        } catch (ManejadorError err) {
            areaSalida.append(">>> ERROR: " + err.getMessage());
        } catch (Exception ex) {
            areaSalida.append(">>> ERROR CRITICO: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Principal().setVisible(true));
    }
}