import AnalizadorLexico.AnalizadorLexico;
import AnalizadorSintactico.AnalizadorSintactico;
import AnalizadorSintactico.ManejadorError;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;

public class VentanaPrincipal extends JFrame {
    private JTextArea areaCodigo;
    private JTextArea areaSalida;
    private JButton botonEjecutar;

    private final Color COLOR_FONDO = new Color(20, 35, 45);
    private final Color COLOR_PANEL = new Color(30, 50, 60);
    private final Color COLOR_TEXTO = new Color(200, 220, 220);
    private final Color COLOR_ACENTO = new Color(40, 180, 130);

    public VentanaPrincipal() {
        setTitle("PRISMA");
        setSize(900, 700);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout(10, 10));

        configurarPanelSuperior();
        configurarPanelesDeTexto();

        ((JPanel) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));
    }

    private void configurarPanelSuperior() {
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        panelSuperior.setBorder(new EmptyBorder(0, 5, 10, 5));

        JLabel tituloPrisma = new JLabel("PRISMA");
        tituloPrisma.setFont(new Font("Segoe UI", Font.BOLD, 24));
        tituloPrisma.setForeground(COLOR_ACENTO);
        panelSuperior.add(tituloPrisma, BorderLayout.WEST);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelAcciones.setOpaque(false);

        botonEjecutar = new JButton("EJECUTAR");
        botonEjecutar.setFocusPainted(false);
        botonEjecutar.setBackground(new Color(40, 180, 130));
        botonEjecutar.setForeground(new Color(10, 25, 20));
        botonEjecutar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botonEjecutar.setPreferredSize(new Dimension(110, 45));
        botonEjecutar.setBorder(BorderFactory.createLineBorder(new Color(10, 25, 20), 2));
        botonEjecutar.setContentAreaFilled(true);
        botonEjecutar.setOpaque(true);

        botonEjecutar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evento) {
                botonEjecutar.setBackground(COLOR_ACENTO.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evento) {
                botonEjecutar.setBackground(COLOR_ACENTO);
            }
        });

        botonEjecutar.addActionListener(evento -> iniciarProcesoCompilacion());
        panelAcciones.add(botonEjecutar);
        
        panelSuperior.add(panelAcciones, BorderLayout.EAST);
        add(panelSuperior, BorderLayout.NORTH);
    }

    private void configurarPanelesDeTexto() {
        areaCodigo = new JTextArea();
        areaCodigo.setBackground(COLOR_PANEL);
        areaCodigo.setForeground(COLOR_TEXTO);
        areaCodigo.setCaretColor(Color.WHITE);
        areaCodigo.setFont(new Font("Consolas", Font.PLAIN, 16));
        areaCodigo.setMargin(new Insets(10, 10, 10, 10));

        JTextArea areaNumerosLinea = new JTextArea();
        areaNumerosLinea.setBackground(COLOR_FONDO);
        areaNumerosLinea.setForeground(new Color(100, 120, 130));
        areaNumerosLinea.setFont(new Font("Consolas", Font.PLAIN, 16));
        areaNumerosLinea.setEditable(false);
        areaNumerosLinea.setMargin(new Insets(10, 10, 10, 5));
        areaNumerosLinea.setFocusable(false);
        
        StringBuilder numeros = new StringBuilder();
        for (int i = 1; i <= 200; i++) {
            numeros.append(i).append("\n");
        }
        areaNumerosLinea.setText(numeros.toString());

        JScrollPane barraDesplazamientoCodigo = new JScrollPane(areaCodigo);
        barraDesplazamientoCodigo.setRowHeaderView(areaNumerosLinea);
        barraDesplazamientoCodigo.setBorder(BorderFactory.createLineBorder(COLOR_PANEL.brighter(), 1));

        areaSalida = new JTextArea(12, 0);
        areaSalida.setEditable(false);
        areaSalida.setBackground(new Color(15, 25, 30));
        areaSalida.setForeground(Color.WHITE);
        areaSalida.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaSalida.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane barraDesplazamientoSalida = new JScrollPane(areaSalida);
        barraDesplazamientoSalida.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_PANEL), "RESULTADOS", 0, 0, null, COLOR_TEXTO));

        JSplitPane panelDivision = new JSplitPane(JSplitPane.VERTICAL_SPLIT, barraDesplazamientoCodigo, barraDesplazamientoSalida);
        panelDivision.setDividerLocation(450);
        panelDivision.setDividerSize(8);
        panelDivision.setContinuousLayout(true);
        panelDivision.setOpaque(false);
        panelDivision.setBorder(null);
        
        panelDivision.setUI(new javax.swing.plaf.basic.BasicSplitPaneUI() {
            public javax.swing.plaf.basic.BasicSplitPaneDivider createDefaultDivider() {
                return new javax.swing.plaf.basic.BasicSplitPaneDivider(this) {
                    @Override
                    public void paint(Graphics graficos) {
                        graficos.setColor(COLOR_PANEL);
                        graficos.fillRect(0, 0, getSize().width, getSize().height);
                        super.paint(graficos);
                    }
                };
            }
        });

        add(panelDivision, BorderLayout.CENTER);
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

    private void iniciarProcesoCompilacion() {
        areaSalida.setText("");
        String contenidoCodigo = areaCodigo.getText();

        if (contenidoCodigo.trim().isEmpty()) {
            areaSalida.append("Error: Sin contenido para analizar.");
            return;
        }

        AnalizadorSintactico sintactico = null;
        AnalizadorLexico lexico = null;
        String mensajeErrorSintactico = null;

        try {
            InputStream flujoEntrada = new ByteArrayInputStream(contenidoCodigo.getBytes());
            System.setIn(flujoEntrada);

            lexico = new AnalizadorLexico();
            lexico.definirLineasFuente(contenidoCodigo);
            sintactico = new AnalizadorSintactico(lexico);
            sintactico.iniciarAnalisis();
        } catch (ManejadorError errorCapturado) {
            mensajeErrorSintactico = errorCapturado.getMessage();
        } catch (Exception excepcion) {
            areaSalida.append("Fallo en el proceso: " + excepcion.getMessage() + "\n");
        }

        areaSalida.append("--- ANALISIS LEXICO ---\n");
        if (lexico != null) {
            String[] erroresLex = lexico.obtenerErroresLexicos();
            if (erroresLex.length > 0) {
                for (int i = 0; i < erroresLex.length; i++) {
                    areaSalida.append(erroresLex[i] + "\n");
                }
            } else {
                areaSalida.append("Correcto.\n");
            }
        }

        areaSalida.append("\n--- ANALISIS SINTACTICO ---\n");
        if (mensajeErrorSintactico != null) {
            areaSalida.append(mensajeErrorSintactico + "\n");
        } else {
            areaSalida.append("Correcto.\n");
        }

        areaSalida.append("\n--- ANALISIS SEMANTICO ---\n");
        if (sintactico != null) {
            String[] listaErroresSem = sintactico.obtenerSemantico().obtenerMensajesDeError();
            if (listaErroresSem.length == 0) {
                areaSalida.append("Correcto.\n");
            } else {
                for (int j = 0; j < listaErroresSem.length; j++) {
                    areaSalida.append(listaErroresSem[j] + "\n");
                }
            }
        } else {
            areaSalida.append("No completado.\n");
        }
        
        areaSalida.append("\n--- FINALIZADO ---\n");
    }

    public static void main(String[] argumentos) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception error) {
        }

        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}