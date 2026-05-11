import AnalizadorLexico.AnalizadorLexico;
import AnalizadorSintactico.AnalizadorSintactico;
import AnalizadorSintactico.ManejadorError;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import javax.swing.undo.UndoManager;
import javax.swing.event.UndoableEditListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

public class VentanaPrincipal extends JFrame {
    private JTextArea areaCodigo;
    private JTextArea areaSalida;
    private JButton botonEjecutar;
    private UndoManager gestorDeshacer;

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
        
        gestorDeshacer = new UndoManager();
        areaCodigo.getDocument().addUndoableEditListener(evento -> gestorDeshacer.addEdit(evento.getEdit()));
        
        areaCodigo.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK), "deshacerAccion");
        areaCodigo.getActionMap().put("deshacerAccion", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                if (gestorDeshacer.canUndo()) {
                    gestorDeshacer.undo();
                }
            }
        });

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
        cargarCodigoInicial();
    }

    private void cargarCodigoInicial() {
        File archivo = new File("programa_por_defecto.txt");
        if (archivo.exists()) {
            try {
                BufferedReader lector = new BufferedReader(new FileReader(archivo));
                StringBuilder contenido = new StringBuilder();
                String linea;
                while ((linea = lector.readLine()) != null) {
                    contenido.append(linea).append("\n");
                }
                lector.close();
                areaCodigo.setText(contenido.toString());
            } catch (IOException e) {
                areaCodigo.setText("MODULO Error\nPRINCIPAL\nIMPRIME(\"Error al leer archivo\")\nFIN-PRINCIPAL\nFIN-MODULO");
            }
        } else {
            areaCodigo.setText("MODULO Inicio\nPRINCIPAL\nIMPRIME(\"Crea programa_por_defecto.txt\")\nFIN-PRINCIPAL\nFIN-MODULO");
        }
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
            lexico.definirLineasFuente(contenidoCodigo.toCharArray());
            sintactico = new AnalizadorSintactico(lexico);
            sintactico.iniciarAnalisis();
        } catch (ManejadorError errorCapturado) {
            mensajeErrorSintactico = errorCapturado.getMessage();
        } catch (Exception excepcion) {
            areaSalida.append("Fallo en el proceso: " + excepcion.getMessage() + "\n");
        }

        if (lexico != null) {
            char[][] erroresLexChar = lexico.obtenerErroresLexicos();
            if (erroresLexChar.length > 0) {
                areaSalida.append("--- ANALISIS LEXICO ---\n");
                for (int i = 0; i < erroresLexChar.length; i++) {
                    areaSalida.append(new String(erroresLexChar[i]) + "\n");
                }
            }
        }

        if (mensajeErrorSintactico != null) {
            areaSalida.append("\n--- ANALISIS SINTACTICO ---\n");
            areaSalida.append(mensajeErrorSintactico + "\n");
        }

        if (sintactico != null) {
            String[] listaErroresSem = sintactico.obtenerSemantico().obtenerMensajesDeError();
            if (listaErroresSem.length > 0) {
                areaSalida.append("\n--- ANALISIS SEMANTICO ---\n");
                for (int j = 0; j < listaErroresSem.length; j++) {
                    areaSalida.append(listaErroresSem[j] + "\n");
                }
            }
        }

        boolean sinErroresLexicos = (lexico != null && lexico.obtenerErroresLexicos().length == 0);
        boolean sinErroresSintacticos = (mensajeErrorSintactico == null);
        boolean sinErroresSemanticos = (sintactico != null && sintactico.obtenerSemantico().obtenerMensajesDeError().length == 0);

        if (sinErroresLexicos && sinErroresSintacticos && sinErroresSemanticos) {
            areaSalida.append("\n>>> COMPILACION EXITOSA <<<\n");
            generarCodigoEnsamblador(contenidoCodigo);
        } else {
            areaSalida.append("\n>>> COMPILACION FALLIDA: No se genero codigo ensamblador <<<\n");
        }
    }

    private void generarCodigoEnsamblador(String codigo) {
        try {
            char[] resultadoAsmChars = Generador.traducir(codigo.toCharArray());
            String resultadoAsm = new String(resultadoAsmChars);

            File carpetaTemporal = new File("temporal");
            if (!carpetaTemporal.exists()) {
                carpetaTemporal.mkdir();
            }

            FileWriter escritor = new FileWriter("temporal/programa.asm");
            escritor.write(resultadoAsm);
            escritor.close();

            areaSalida.append("\n[SISTEMA] Codigo ensamblador generado en: temporal/programa.asm\n");
        } catch (Exception error) {
            areaSalida.append("\n[ERROR] Fallo al generar el archivo .asm: " + error.getMessage() + "\n");
        }
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