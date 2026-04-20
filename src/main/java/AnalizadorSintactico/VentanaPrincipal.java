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
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        panelSuperior.setBorder(new EmptyBorder(0, 5, 10, 5));

        JLabel logo = new JLabel("PRISMA");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logo.setForeground(COLOR_ACCENTO);
        panelSuperior.add(logo, BorderLayout.WEST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setOpaque(false);

        botonRun = new JButton("RUN");
        botonRun.setFocusPainted(false);
        // Usar un color verde sólido y texto oscuro para máximo contraste
        botonRun.setBackground(new Color(40, 180, 130)); // Verde Prisma
        botonRun.setForeground(new Color(10, 25, 20));   // Casi negro para el texto
        botonRun.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botonRun.setPreferredSize(new Dimension(110, 45));
        botonRun.setBorder(BorderFactory.createLineBorder(new Color(10, 25, 20), 2));
        botonRun.setContentAreaFilled(true);
        botonRun.setOpaque(true);

        botonRun.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botonRun.setBackground(COLOR_ACCENTO.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                botonRun.setBackground(COLOR_ACCENTO);
            }
        });

        botonRun.addActionListener(e -> ejecutarCompilador());
        panelBotones.add(botonRun);
        
        panelSuperior.add(panelBotones, BorderLayout.EAST);
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

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollCodigo, scrollSalida);
        splitPane.setDividerLocation(450);
        splitPane.setDividerSize(8);
        splitPane.setContinuousLayout(true);
        splitPane.setOpaque(false);
        splitPane.setBorder(null);
        
        // Estilizar el divisor (UI específica de Swing)
        splitPane.setUI(new javax.swing.plaf.basic.BasicSplitPaneUI() {
            public javax.swing.plaf.basic.BasicSplitPaneDivider createDefaultDivider() {
                return new javax.swing.plaf.basic.BasicSplitPaneDivider(this) {
                    @Override
                    public void paint(Graphics g) {
                        g.setColor(COLOR_PANEL);
                        g.fillRect(0, 0, getSize().width, getSize().height);
                        super.paint(g);
                    }
                };
            }
        });

        add(splitPane, BorderLayout.CENTER);
        
        areaCodigo.setText(
            "MODULO Validacion\n" +
            "    VARIABLES\n" +
            "        TEXTO a = \"\"\n" +
            "        TEXTO c = \"\"\n" +
            "        ENTERO mi_variable = 10\n" +
            "        TEXTO a = \"\"\n" +
            "        ENTERO _inicio_guion = 5\n" +
            "        DECIMAL auto = 1.5\n" +
            "        ENTERO aux = \"\"\n" +
            "        ENTERO b = \"\"\n" +
            "        ENTERO b1 = \"\"\n\n" +
            "    FIN-VARIABLES\n" +
            "    PRINCIPAL\n" +
            "       c = a + \"hola @@@@@ &/&(mundo#\"\n" +
            "       a = 4\n" +
            "        \n" +
            "       IMPRIME(\"HOLA MUNDO\")\n" +
            "       IMPRIME(3.1223)\n" +
            "       IMPRIME(4)\n" +
            "       IMPRIME(a)\n\n" +
            "       SI ( ( a < 8 ) O ( a <= 5 ) ) ENTONCES\n" +
            "       SI ( ( a < 8 ) O ( a <= 5 ) ) ENTONCES\n" +
            "       SI ( ( 8 < 8 ) O ( 3 <= 5 ) ) ENTONCES\n\n" +
            "       a = a + 1\n" +
            "       \n" +
            "       FIN-SI\n" +
            "       FIN-SI\n" +
            "       FIN-SI\n\n" +
            "PARA ( aux = 1, aux <= 10, aux = aux + 1 )\n" +
            "    IMPRIME(aux)\n" +
            "FIN-PARA\n" +
            "PARA ( aux = a, aux <= 10, aux = aux + 1 )\n" +
            "    IMPRIME(aux)\n" +
            "FIN-PARA\n" +
            "PARA ( aux = 1, aux <= a, aux = aux + 1 )\n" +
            "    IMPRIME(aux)\n" +
            "FIN-PARA\n" +
            "PARA ( aux = a, aux <= b, aux = aux + 1 )\n" +
            "    IMPRIME(aux)\n" +
            "FIN-PARA\n" +
            "MIENTRAS ( a < b1 )\n" +
            "    IMPRIME(a)\n" +
            "    a = a + 1\n" +
            "FIN-MIENTRAS\n" +
            "MIENTRAS ( a < 5.5 )\n" +
            "    IMPRIME(a)\n" +
            "FIN-MIENTRAS\n" +
            "MIENTRAS ( 0.5 >= b )\n" +
            "    IMPRIME(b)\n" +
            "FIN-MIENTRAS\n" +
            "MIENTRAS ( 3 > 2 )\n" +
            "    IMPRIME(3)\n" +
            "FIN-MIENTRAS\n" +
            "LEER ( a )\n\n" +
            "    FIN-PRINCIPAL\n" +
            "FIN-MODULO"
        );
    }

    private void ejecutarCompilador() {
        areaSalida.setText("");
        String codigo = areaCodigo.getText();

        if (codigo.trim().isEmpty()) {
            areaSalida.append("Error: El area de codigo esta vacia.");
            return;
        }

        try {
            InputStream stream = new ByteArrayInputStream(codigo.getBytes());
            System.setIn(stream);

            AnalizadorLexico lexico = new AnalizadorLexico();
            lexico.setLineasFuente(codigo);
            AnalizadorSintactico sintactico = new AnalizadorSintactico(lexico);

            sintactico.analizar();

        } catch (ManejadorError err) {
            areaSalida.append(err.getMessage() + "\n");
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