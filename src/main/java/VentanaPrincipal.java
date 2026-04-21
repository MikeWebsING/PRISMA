import AnalizadorLexico.AnalizadorLexico;
import AnalizadorSintactico.AnalizadorSintactico;
import AnalizadorSintactico.ManejadorError;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.awt.Insets;
import java.util.ArrayList;

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
        areaSalida.setForeground(Color.WHITE);
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
            "MODULO SerieNumerica\n" +
            "    VARIABLES\n" +
            "        ENTERO n = 0\n" +
            "        ENTERO aux = 0\n" +
            "        ENTERO repetir = 1\n" +
            "    FIN-VARIABLES\n" +
            "    \n" +
            "    PRINCIPAL\n" +
            "        MIENTRAS ( repetir == 1 )\n" +
            "            IMPRIME ( \"INGRESA UN NUMERO (0-9):\" )\n" +
            "            LEER ( n )\n" +
            "            \n" +
            "            SI ( ( n > 0 ) Y ( n < 9 ) ) ENTONCES\n" +
            "                SI ( ( n % 2 ) == 0 ) ENTONCES\n" +
            "                    IMPRIME ( \"SERIE PARES (CICLO PARA):\" )\n" +
            "                    PARA ( aux = 0, aux <= 9, aux = aux + 1 )\n" +
            "                        SI ( ( aux % 2 ) == 0 ) ENTONCES\n" +
            "                            IMPRIME ( aux )\n" +
            "                        FIN-SI\n" +
            "                    FIN-PARA\n" +
            "                SINO\n" +
            "                    IMPRIME ( \"SERIE IMPARES (CICLO MIENTRAS):\" )\n" +
            "                    aux = 0\n" +
            "                    MIENTRAS ( aux <= 9 )\n" +
            "                        SI ( ( aux % 2 ) != 0 ) ENTONCES\n" +
            "                            IMPRIME ( aux )\n" +
            "                        FIN-SI\n" +
            "                        aux = aux + 1\n" +
            "                    FIN-MIENTRAS\n" +
            "                FIN-SI\n" +
            "            FIN-SI\n" +
            "            \n" +
            "            IMPRIME ( \"DESEA REPETIR? (1=SI, 0=NO)\" )\n" +
            "            LEER ( repetir )\n" +
            "        FIN-MIENTRAS\n" +
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

        AnalizadorSintactico sintactico = null;
        AnalizadorLexico lexico = null;
        String errorSintactico = null;

        try {
            InputStream stream = new ByteArrayInputStream(codigo.getBytes());
            System.setIn(stream);

            lexico = new AnalizadorLexico();
            lexico.setLineasFuente(codigo);
            sintactico = new AnalizadorSintactico(lexico);
            sintactico.analizar();
        } catch (ManejadorError err) {
            errorSintactico = err.getMessage();
        } catch (Exception ex) {
            areaSalida.append(">>> ERROR CRITICO: " + ex.getMessage() + "\n");
        }

        // --- REPORTE POR SECCIONES ---
        
        areaSalida.append("=== ANALISIS LEXICO ===\n");
        if (lexico != null && !lexico.erroresLexicos.isEmpty()) {
            for (String errL : lexico.erroresLexicos) areaSalida.append(errL + "\n");
        } else {
            areaSalida.append("Sin errores lexicos.\n");
        }

        areaSalida.append("\n=== ANALISIS SINTACTICO ===\n");
        if (errorSintactico != null) {
            areaSalida.append(errorSintactico + "\n");
        } else {
            areaSalida.append("Sin errores sintacticos.\n");
        }

        areaSalida.append("\n=== ANALISIS SEMANTICO ===\n");
        if (sintactico != null) {
            ArrayList<String> errS = sintactico.getSem().obtenerErroresMsg();
            if (errS.isEmpty()) {
                areaSalida.append("Sin errores semanticos.\n");
            } else {
                for (String msg : errS) areaSalida.append(msg + "\n");
            }
        } else {
            areaSalida.append("No se pudo completar el analisis semantico.\n");
        }
        
        areaSalida.append("\n=== FIN DEL ANALISIS ===\n");
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