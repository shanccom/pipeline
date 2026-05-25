import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

public class PipelineInterface extends JFrame {
    private final JButton startButton = new JButton("Iniciar Pipeline");
    private final JTextArea logArea = new JTextArea();
    private final DefaultTableModel initialTableModel = new DefaultTableModel(
            new Object[]{"Id", "Nombre", "Curso", "Promedio"}, 0
    );
    private final DefaultTableModel processedTableModel = new DefaultTableModel(
            new Object[]{"Id", "Nombre", "Curso", "Promedio", "Status"}, 0
    );

    private final JLabel initialDataStep = createStepLabel("Datos iniciales");
    private final JLabel lectorStep = createStepLabel("Lector");
    private final JLabel validadorStep = createStepLabel("Validador");
    private final JLabel transformadorStep = createStepLabel("Transformador");
    private final JLabel reporteStep = createStepLabel("Reporte");
    private final JLabel statusLabel = new JLabel("Listo para iniciar la pipeline", SwingConstants.CENTER);

    public PipelineInterface() {
        super("Pipeline de Procesamiento Academico");
        configureWindow();
        configureEvents();
    }

    private void configureWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(createPipelinePanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    private JPanel createPipelinePanel() {
        JPanel panel = new JPanel(new GridLayout(1, 9, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));

        panel.add(initialDataStep);
        panel.add(createArrowLabel());
        panel.add(lectorStep);
        panel.add(createArrowLabel());
        panel.add(validadorStep);
        panel.add(createArrowLabel());
        panel.add(transformadorStep);
        panel.add(createArrowLabel());
        panel.add(reporteStep);

        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));

        JTable initialTable = new JTable(initialTableModel);
        initialTable.setFillsViewportHeight(true);
        JScrollPane initialTableScroll = new JScrollPane(initialTable);
        initialTableScroll.setBorder(BorderFactory.createTitledBorder("Datos iniciales"));

        logArea.setEditable(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder("Logs de ejecucion"));

        JTable processedTable = new JTable(processedTableModel);
        processedTable.setFillsViewportHeight(true);
        JScrollPane processedTableScroll = new JScrollPane(processedTable);
        processedTableScroll.setBorder(BorderFactory.createTitledBorder("Estudiantes procesados correctamente"));

        panel.add(initialTableScroll);
        panel.add(logScroll);
        panel.add(processedTableScroll);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));

        statusLabel.setBorder(BorderFactory.createEtchedBorder());
        statusLabel.setPreferredSize(new Dimension(100, 32));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(startButton);

        panel.add(statusLabel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.EAST);

        return panel;
    }

    private void configureEvents() {
        startButton.addActionListener(event -> startPipeline());
    }

    private void startPipeline() {
        startButton.setEnabled(false);
        initialTableModel.setRowCount(0);
        processedTableModel.setRowCount(0);
        logArea.setText("");
        resetSteps();

        PipelineWorker worker = new PipelineWorker();
        worker.execute();
    }

    private JLabel createStepLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        label.setBackground(new Color(230, 230, 230));
        return label;
    }

    private JLabel createArrowLabel() {
        JLabel label = new JLabel("->", SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 18f));
        return label;
    }

    private void resetSteps() {
        setStepInactive(initialDataStep);
        setStepInactive(lectorStep);
        setStepInactive(validadorStep);
        setStepInactive(transformadorStep);
        setStepInactive(reporteStep);
        statusLabel.setText("Ejecutando pipeline...");
    }

    private void setActiveStep(JLabel activeStep, String message) {
        setStepInactive(initialDataStep);
        setStepInactive(lectorStep);
        setStepInactive(validadorStep);
        setStepInactive(transformadorStep);
        setStepInactive(reporteStep);

        activeStep.setBackground(new Color(255, 243, 176));
        activeStep.setBorder(BorderFactory.createLineBorder(new Color(200, 150, 0), 2));
        statusLabel.setText(message);
    }

    private void setStepInactive(JLabel label) {
        label.setBackground(new Color(230, 230, 230));
        label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    private void appendLog(String message) {
        logArea.append(message + System.lineSeparator());
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void addInitialRecord(AcademicRecord record) {
        initialTableModel.addRow(new Object[]{
                record.getId(),
                record.getName(),
                record.getCourse(),
                record.getAverage()
        });
    }

    private void addProcessedRecord(AcademicRecord record) {
        processedTableModel.addRow(new Object[]{
                record.getId(),
                record.getName(),
                record.getCourse(),
                record.getAverage(),
                record.getStatus()
        });
    }

    private class PipelineWorker extends SwingWorker<Void, Runnable> {
        @Override
        protected Void doInBackground() {
            try {
                // La interfaz consume el lector existente; no interpreta el CSV por su cuenta.
                CsvReader reader = new CsvReader();
                publish(() -> setActiveStep(lectorStep, "Leyendo datos..."));
                publish(() -> appendLog("[Lector] Leyendo datos desde academic_records.csv"));
                List<AcademicRecord> records = reader.readAll();
                publish(() -> appendLog("[Lector] Registros leidos: " + records.size()));
                pause();

                // Esta tabla muestra los datos antes de pasar por validacion o transformacion.
                publish(() -> setActiveStep(initialDataStep, "Mostrando datos iniciales..."));
                for (AcademicRecord record : records) {
                    publish(() -> addInitialRecord(record));
                }
                pause();

                // La interfaz reutiliza el validador existente para conservar las reglas del backend.
                RecordValidator validator = new RecordValidator();
                publish(() -> setActiveStep(validadorStep, "Validando estudiantes..."));
                for (AcademicRecord record : records) {
                    AcademicRecord validated = validator.validate(record);
                    if (validated.isValid()) {
                        publish(() -> appendLog("[Validador] Registro valido: " + validated.getId()));
                    } else {
                        publish(() -> appendLog("[Validador] Registro invalido: "
                                + validated.getId() + " - " + validated.getError()));
                    }
                }
                pause();

                // La interfaz llama al transformador existente; solo muestra el resultado en pantalla.
                RecordTransformer transformer = new RecordTransformer();
                publish(() -> setActiveStep(transformadorStep, "Transformando informacion..."));
                for (AcademicRecord record : records) {
                    AcademicRecord transformed = transformer.transform(record);
                    publish(() -> appendLog("[Transformador] Estado asignado a "
                            + transformed.getId() + ": " + transformed.getStatus()));
                }
                pause();

                // La interfaz usa el generador de reporte existente para acumular las estadisticas.
                ReportGenerator generator = new ReportGenerator();
                publish(() -> setActiveStep(reporteStep, "Generando reporte..."));
                for (AcademicRecord record : records) {
                    ReportGenerator.ReportStats stats = generator.addRecord(record);
                    if (record.isValid() && !"ERROR".equalsIgnoreCase(record.getStatus())) {
                        publish(() -> addProcessedRecord(record));
                    }
                    publish(() -> appendLog("[Reporte] Totales -> Aprobados: " + stats.getApproved()
                            + " | Desaprobados: " + stats.getFailed()
                            + " | Errores: " + stats.getErrors()));
                }
                publish(() -> statusLabel.setText("Pipeline finalizada correctamente"));
            } catch (IOException ex) {
                publish(() -> appendLog("[Interfaz] Error leyendo datos: " + ex.getMessage()));
                publish(() -> statusLabel.setText("Error al ejecutar la pipeline"));
            }
            return null;
        }

        @Override
        protected void process(List<Runnable> chunks) {
            for (Runnable action : chunks) {
                action.run();
            }
        }

        @Override
        protected void done() {
            startButton.setEnabled(true);
        }

        private void pause() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PipelineInterface().setVisible(true));
    }
}
