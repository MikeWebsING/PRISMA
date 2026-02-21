package AnalizadorSintactico;

import AnalizadorLexico.AnalizadorLexico;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.awt.Insets;

public class VentanaPrincipal extends JFrame {
    private JTextArea areaCodigo;
    private JTextArea areaSalida;
    private JButton botonRun;

    private final Color COLOR_FONDO = new Color(20, 35, 45);
    private final Color COLOR_PANEL = new Color(30, 50, 60);
    private final Color COLOR_TEXTO = new Color(200, 220, 220);
    private final Color COLOR_ACCENTO = new Color(40, 180, 130);

    public VentanaPrincipal() {
        setTitle("PRISMA IDE");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout(10, 10));

        configurarBarraSuperior();
        configurarAreasTexto();

        ((JPanel) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));
    }

    private void configurarBarraSuperior() {
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSuperior.setOpaque(false);

        botonRun = new JButton("RUN");
        botonRun.setFocusPainted(false);
        botonRun.setBackground(COLOR_ACCENTO);
        botonRun.setForeground(Color.WHITE);
        botonRun.setFont(new Font("Segoe UI", Font.BOLD, 12));
        botonRun.setPreferredSize(new Dimension(80, 35));
        botonRun.setBorder(BorderFactory.createEmptyBorder());

        botonRun.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botonRun.setBackground(COLOR_ACCENTO.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                botonRun.setBackground(COLOR_ACCENTO);
            }
        });

        botonRun.addActionListener(e -> ejecutarCompilador());
        panelSuperior.add(botonRun);
        add(panelSuperior, BorderLayout.NORTH);
    }

    private void configurarAreasTexto() {
        areaCodigo = new JTextArea();
        areaCodigo.setBackground(COLOR_PANEL);
        areaCodigo.setForeground(COLOR_TEXTO);
        areaCodigo.setCaretColor(Color.WHITE);
        areaCodigo.setFont(new Font("Consolas", Font.PLAIN, 16));
        areaCodigo.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollCodigo = new JScrollPane(areaCodigo);
        scrollCodigo.setBorder(BorderFactory.createLineBorder(COLOR_PANEL.brighter(), 1));

        areaSalida = new JTextArea(12, 0);
        areaSalida.setEditable(false);
        areaSalida.setBackground(new Color(15, 25, 30));
        areaSalida.setForeground(new Color(54, 108, 95));
        areaSalida.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaSalida.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollSalida = new JScrollPane(areaSalida);
        scrollSalida.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_PANEL), "SALIDA", 0, 0, null, COLOR_TEXTO));

        JPanel panelCentral = new JPanel(new GridLayout(2, 1, 0, 10));
        panelCentral.setOpaque(false);
        panelCentral.add(scrollCodigo);
        panelCentral.add(scrollSalida);

        add(panelCentral, BorderLayout.CENTER);
    }

    private void ejecutarCompilador() {
        areaSalida.setText("Compilando PRISMA...\n");
        String codigo = areaCodigo.getText();

        if (codigo.trim().isEmpty()) {
            areaSalida.append("Error: El area de codigo esta vacia.");
            return;
        }

        try {
            InputStream stream = new ByteArrayInputStream(codigo.getBytes());
            System.setIn(stream);

            AnalizadorLexico lexico = new AnalizadorLexico();
            AnalizadorSintactico sintactico = new AnalizadorSintactico(lexico);

            sintactico.analizar();

            areaSalida.append(">>> ANALISIS FINALIZADO: El codigo es sintacticamente correcto.\n");
        } catch (ManejadorError err) {
            areaSalida.append(">>> ERROR SINTACTICO: " + err.getMessage() + "\n");
        } catch (Exception ex) {
            areaSalida.append(">>> ERROR CRITICO: " + ex.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}