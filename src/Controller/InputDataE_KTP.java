package Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.jdatepicker.impl.*;
import java.util.Properties;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import Model.DateLabelFormatter;

public class InputDataE_KTP {
    private JFrame frameInput;
    private JFrame frameKTP;
    private JTextField nikField, namaField, tempatLahirField, alamatField, rtRwField, kelDesaField, kecamatanField,
            berlakuField, kotaField, dateMade, nikSearch;
    private JComboBox<String> agamaComboBox, statusComboBox;
    private JCheckBox swastaCheck, pnsCheck, wiraswastaCheck, akademisiCheck, pengangguranCheck;
    private JRadioButton priaButton, wanitaButton, golDarA, golDarB, golDarO, golDarAB, wniButton, wnaButton;
    private JTextField wnaField; // di-hide dulu, jika di-klik WNA baru muncul
    private JButton submitButton, fotoButton, ttdButton;
    private ButtonGroup genderGroup, kewarganegaraanGroup, golDarahGroup;
    private JDatePickerImpl tanggalLahir;
    private JLabel fotoPanel, tandaTanganPanel;
    private File selectedFoto, selectedTTD;
    private String pathPhoto, pathTtd;

    // Ukuran frame dan margin
    private static final int FRAME_WIDTH = 570;
    private static final int FRAME_HEIGHT = 470;
    private static final int MARGIN = 10;
    private static final int LINE_SPACING = 22;

    // Ukuran font
    private static final int TITLE_FONT_SIZE = 25;
    private static final int LABEL_FONT_SIZE = 12;
    private static final int NIK_FONT_SIZE = 15; // Ukuran font khusus untuk NIK

    // Ukuran foto
    private static final int FOTO_WIDTH = 170;
    private static final int FOTO_HEIGHT = 200;

    // Kolom data
    private static final int COLON_WIDTH = 10; // Lebar untuk ":"
    private static final int COLUMN_MARGIN = 155; // Margin kolom kanan untuk value

    public InputDataE_KTP() {
        showMainMenu();
    }

    public void createInput(Boolean updateDelete, String data[]) {
        // Frame Input Data Penduduk
        frameInput = new JFrame("Form Data Penduduk");
        frameInput.setSize(850, 600);
        frameInput.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frameInput.setLocationRelativeTo(null);
        frameInput.setLayout(new BorderLayout());

        JLabel judulForm = new JLabel("Form Data Penduduk", SwingConstants.CENTER);
        judulForm.setFont(new Font("Arial", Font.BOLD, 27));
        frameInput.add(judulForm, BorderLayout.NORTH);

        // Panel Data Input
        JPanel panelDataInput = new JPanel(new GridLayout(21, 2));
        panelDataInput.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        // Input Fields
        addInputField(panelDataInput, "NIK:", nikField = new JTextField(data[0]));
        addInputField(panelDataInput, "Nama:", namaField = new JTextField(data[1]));
        addInputField(panelDataInput, "Tempat Lahir:", tempatLahirField = new JTextField(data[2]));

        tanggalLahir = createDatePicker(tanggalLahir);
        if (!data[3].equals("")) {
            setDatePickerTglLahir(data[3]);
        }
        addInputField(panelDataInput, "Tanggal Lahir:", tanggalLahir);
        
        addInputField(panelDataInput, "Jenis Kelamin:", createGenderPanel(data[4]));
        addInputField(panelDataInput, "Gol. Darah:", createGolDarahPanel(data[5]));
        addInputField(panelDataInput, "Alamat:", alamatField = new JTextField(data[6]));
        addInputField(panelDataInput, "RT/RW:", rtRwField = new JTextField(data[7]));
        addInputField(panelDataInput, "Kelurahan/Desa:", kelDesaField = new JTextField(data[8]));
        addInputField(panelDataInput, "Kecamatan:", kecamatanField = new JTextField(data[9]));

        agamaComboBox = createComboBox(
                new String[] { "Pilih Agama", "ISLAM", "KRISTEN", "KATOLIK", "HINDU", "BUDDHA", "KONGHUCU" });
        if (!data[10].equalsIgnoreCase("")) {
            if (data[10].equalsIgnoreCase("ISLAM")) {
                agamaComboBox.setSelectedItem("ISLAM");
            } else if (data[10].equalsIgnoreCase("KRISTEN")) {
                agamaComboBox.setSelectedItem("KRISTEN");
            } else if (data[10].equalsIgnoreCase("KATOLIK")) {
                agamaComboBox.setSelectedItem("KATOLIK");
            } else if (data[10].equalsIgnoreCase("HINDU")) {
                agamaComboBox.setSelectedItem("HINDU");
            } else if (data[10].equalsIgnoreCase("BUDDHA")) {
                agamaComboBox.setSelectedItem("BUDDHA");
            } else {
                agamaComboBox.setSelectedItem("KONGHUCU");
            }

        }
        addInputField(panelDataInput, "Agama:", agamaComboBox);

        statusComboBox = createComboBox(
                new String[] { "Pilih Status Perkawinan", "Belum Menikah", "Menikah", "Janda/Duda" });
        if (!data[11].equalsIgnoreCase("")) {
            if (data[11].equalsIgnoreCase("Belum Menikah")) {
                statusComboBox.setSelectedItem("Belum Menikah");
            } else if (data[11].equalsIgnoreCase("Menikah")) {
                statusComboBox.setSelectedItem("Menikah");
            } else {
                statusComboBox.setSelectedItem("Janda/Duda");
            }
        }
        addInputField(panelDataInput, "Status Perkawinan:", statusComboBox);
        addInputField(panelDataInput, "Pekerjaan:", createPekerjaanPanel(data[12]));

        // Membuat radio button kewarganegaraan
        panelDataInput.add(new JLabel("Kewarganegaraan: "));
        wniButton = new JRadioButton("WNI");
        wniButton.setActionCommand("WNI");
        wnaButton = new JRadioButton("WNA");
        wnaButton.setActionCommand("WNA");

        String negaraAsal = "Negara asal";
        if (!data[13].equals("")) {
            if (data[13].equalsIgnoreCase("WNI")) {
                wniButton.setSelected(true);
            } else {
                String dataWNA[] = data[13].split(" ");
                wnaButton.setSelected(true);
                dataWNA[1].replaceAll("\\(", "");
                negaraAsal = dataWNA[1].replaceAll("\\)", "");
            }
        }

        kewarganegaraanGroup = new ButtonGroup();
        kewarganegaraanGroup.add(wniButton);
        kewarganegaraanGroup.add(wnaButton);

        JPanel kewarganegaraanPanel = new JPanel();
        kewarganegaraanPanel.add(wniButton);
        kewarganegaraanPanel.add(wnaButton);

        panelDataInput.add(kewarganegaraanPanel);

        wnaField = new JTextField(negaraAsal);
        // wnaField.setEnabled(false);
        wnaField.setVisible(false);
        wnaButton.addActionListener(e -> wnaField.setVisible(true));
        wniButton.addActionListener(e -> wnaField.setVisible(false));

        // Menambahkan elemen kosong
        panelDataInput.add(new JPanel());
        panelDataInput.add(wnaField);
        // Tombol untuk input foto
        String pathFoto = "";
        // simpan path foto sebelumnya di variabel global
        pathPhoto = data[14];
        if (!data[14].equals("")) {
            String dataFoto[] = data[14].split("\\\\");
            pathFoto = dataFoto[dataFoto.length - 1];
        }
        panelDataInput.add(new JLabel("Foto:"));
        fotoButton = new JButton("Pilih Foto");
        JTextField fotoLabelName = new JTextField(pathFoto);
        fotoLabelName.setEditable(false);

        JPanel fotoPanelInput = new JPanel();
        fotoPanelInput.setLayout(new GridLayout(1, 2));
        fotoPanelInput.add(fotoLabelName);
        fotoPanelInput.add(fotoButton);

        panelDataInput.add(fotoPanelInput);

        // Action listener untuk tombol foto
        fotoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedFoto = pilihFileGambar();
                fotoLabelName.setText(selectedFoto.getName());
            }
        });

        // Tombol untuk input tanda tangan
        panelDataInput.add(new JLabel("Tanda Tangan:"));
        ttdButton = new JButton("Pilih Tanda Tangan");

        String pathTTD = "";
        pathTtd = data[15];
        if (!data[15].equals("")) {
            String dataTTD[] = data[15].split("\\\\");
            pathTTD = dataTTD[dataTTD.length - 1];
        }
        JTextField ttdLabelName = new JTextField(pathTTD);
        ttdLabelName.setEditable(false);

        JPanel ttdPanel = new JPanel();
        ttdPanel.setLayout(new GridLayout(1, 2));
        ttdPanel.add(ttdLabelName);
        ttdPanel.add(ttdButton);

        panelDataInput.add(ttdPanel);

        // Action listener untuk tombol tanda tangan
        ttdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedTTD = pilihFileGambar();
                ttdLabelName.setText(selectedTTD.getName());
            }
        });

        addInputField(panelDataInput, "Berlaku Hingga:", berlakuField = new JTextField(data[16]));
        addInputField(panelDataInput, "Kota Pembuatan:", kotaField = new JTextField(data[17]));
        addInputField(panelDataInput, "Tanggal Pembuatan KTP:", dateMade = createDateField(data[18]));

        if (updateDelete) {
            JPanel panelButton = new JPanel(new GridLayout(1, 2));
            panelButton.setSize(400, 35);
            // Tombol Update
            JButton updateButton = new JButton("Update Data");
            panelButton.add(updateButton);
            // Tombol Delete
            JButton deleteButton = new JButton("Delete Data");
            panelButton.add(deleteButton);

            frameInput.add(panelButton, BorderLayout.SOUTH);
            // Update Action
            updateButton.addActionListener(e -> handleUpdate());
            // Delete Action
            deleteButton.addActionListener(e -> handleDelete());

        } else {
            // Tombol Submit
            submitButton = new JButton("Insert Data");
            submitButton.setPreferredSize(new Dimension(400, 35));
            frameInput.add(submitButton, BorderLayout.SOUTH);
            // Submit Action
            submitButton.addActionListener(e -> handleSubmit());
        }

        // Untuk membuat padding dalam
        panelDataInput.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        // Untuk membuat scroll
        JScrollPane scrollPanel = new JScrollPane(panelDataInput);
        scrollPanel.setBorder(null);
        frameInput.add(scrollPanel);
        frameInput.setVisible(true);
    }

    public void setDatePickerTglLahir(String tglLahir){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = sdf.parse(tglLahir);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            // Set tanggal ke JDatePicker
            tanggalLahir.getModel().setDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            );
            tanggalLahir.getModel().setSelected(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Fungsi untuk menambahkan label dan komponen input ke panel
    private void addInputField(JPanel panel, String label, JComponent inputComponent) {
        panel.add(new JLabel(label));
        panel.add(inputComponent);
    }

    // Fungsi untuk membuat Date Field yang tidak bisa diedit
    private JTextField createDateField(String tanggalBuat) {
        JTextField dateField = new JTextField();
        if (!tanggalBuat.equals("")) {
            dateField.setText(tanggalBuat);
        } else {
            dateField.setText(String.valueOf(formatDate(new Date(), "dd-MM-yyyy")));
        }
        dateField.setEditable(false);
        return dateField;
    }

    // Fungsi untuk membuat ComboBox
    private JComboBox<String> createComboBox(String[] options) {
        return new JComboBox<>(options);
    }

    // Fungsi untuk membuat panel Gender
    private JPanel createGenderPanel(String gender) {
        priaButton = new JRadioButton("Laki-laki");
        priaButton.setActionCommand("LAKI-LAKI");
        wanitaButton = new JRadioButton("Perempuan");
        wanitaButton.setActionCommand("PEREMPUAN");

        genderGroup = new ButtonGroup();
        genderGroup.add(priaButton);
        genderGroup.add(wanitaButton);

        if (!gender.equals("")) {
            if (gender.equals("LAKI-LAKI")) {
                priaButton.setSelected(true);
            } else {
                wanitaButton.setSelected(true);
            }
        }

        JPanel genderPanel = new JPanel();
        genderPanel.add(priaButton);
        genderPanel.add(wanitaButton);
        return genderPanel;
    }

    // Fungsi untuk membuat panel Golongan Darah
    private JPanel createGolDarahPanel(String golDarah) {
        golDarA = new JRadioButton("A");
        golDarA.setActionCommand("A");
        golDarB = new JRadioButton("B");
        golDarB.setActionCommand("B");
        golDarO = new JRadioButton("O");
        golDarO.setActionCommand("O");
        golDarAB = new JRadioButton("AB");
        golDarAB.setActionCommand("AB");

        golDarahGroup = new ButtonGroup();
        golDarahGroup.add(golDarA);
        golDarahGroup.add(golDarB);
        golDarahGroup.add(golDarO);
        golDarahGroup.add(golDarAB);

        if (!golDarah.equals("")) {
            if (golDarah.equalsIgnoreCase("A")) {
                golDarA.setSelected(true);
            } else if (golDarah.equalsIgnoreCase("B")) {
                golDarB.setSelected(true);
            } else if (golDarah.equalsIgnoreCase("O")) {
                golDarO.setSelected(true);
            } else {
                golDarAB.setSelected(true);
            }
        }

        JPanel golDarPanel = new JPanel();
        golDarPanel.add(golDarA);
        golDarPanel.add(golDarB);
        golDarPanel.add(golDarO);
        golDarPanel.add(golDarAB);

        return golDarPanel;
    }

    // Fungsi untuk membuat panel Pekerjaan
    private JPanel createPekerjaanPanel(String jobs) {
        swastaCheck = new JCheckBox("Karyawan Swasta");
        pnsCheck = new JCheckBox("PNS");
        wiraswastaCheck = new JCheckBox("Wiraswasta");
        akademisiCheck = new JCheckBox("Akademisi");
        pengangguranCheck = new JCheckBox("Pengangguran");

        if (!jobs.equals("")) {
            String[] jobsSplit = jobs.split(", ");

            for (String job : jobsSplit) {
                if (job.equalsIgnoreCase("Karyawan Swasta")) {
                    swastaCheck.setSelected(true);
                } else if (job.equalsIgnoreCase("PNS")) {
                    pnsCheck.setSelected(true);
                } else if (job.equalsIgnoreCase("Wiraswasta")) {
                    wiraswastaCheck.setSelected(true);
                } else if (job.equalsIgnoreCase("Akademisi")) {
                    akademisiCheck.setSelected(true);
                } else {
                    pengangguranCheck.setSelected(true);
                }
            }

        }
        pengangguranCheck.addActionListener(e -> togglePekerjaan(pengangguranCheck.isSelected()));

        JPanel pekerjaanPanel = new JPanel(new GridLayout(2, 3));
        pekerjaanPanel.add(swastaCheck);
        pekerjaanPanel.add(pnsCheck);
        pekerjaanPanel.add(wiraswastaCheck);
        pekerjaanPanel.add(akademisiCheck);
        pekerjaanPanel.add(pengangguranCheck);

        return pekerjaanPanel;
    }

    // Fungsi untuk memilih dan menyimpan file
    private File pilihFileGambar() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Filter hanya untuk file gambar
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                String name = f.getName().toLowerCase();
                return f.isDirectory() || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png");
            }

            @Override
            public String getDescription() {
                return "File Gambar (*.jpg, *.jpeg, *.png)";
            }
        });

        int result = fileChooser.showOpenDialog(null);

        // Jika pengguna memilih file
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }

        return null;
    }

    // Fungsi untuk menampilkan gambar ke JLabel
    private void tampilkanGambar(JLabel fotoPanel, String pathFile) {
        // Menampilkan gambar pada JLabel
        ImageIcon imageIcon = new ImageIcon(pathFile);
        // Menyesuaikan ukuran gambar dengan ukuran label
        Image image = imageIcon.getImage().getScaledInstance(fotoPanel.getWidth(), fotoPanel.getHeight(),
                Image.SCALE_SMOOTH);
        fotoPanel.setIcon(new ImageIcon(image));
    }

    // Untuk membuat datePicker
    private JDatePickerImpl createDatePicker(JDatePickerImpl datePicker) {
        // Properti untuk Date Picker
        Properties properties = new Properties();
        properties.put("text.today", "Today");
        properties.put("text.month", "Month");
        properties.put("text.year", "Year");

        // Membuat Date Picker
        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model, properties);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        return datePicker;
    }

    // Fungsi untuk buat label teks
    private static int tambahLabel(JPanel panel, String text, int fontSize, int y, int margin, boolean center) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, fontSize));
        if (center) {
            label.setBounds(margin, y, FRAME_WIDTH - 2 * margin, 30);
            label.setHorizontalAlignment(SwingConstants.CENTER);
        } else {
            label.setBounds(margin, y, FRAME_WIDTH - margin, 20);
        }
        panel.add(label);
        return y + 30;
    }

    // Fungsi agar data dengan format "Key : Value" rapih dan sejajar
    private static int tambahData(JPanel panel, String key, String value, int y, int fontSize) {
        int labelX = MARGIN;
        int colonX = COLUMN_MARGIN;
        int valueX = COLUMN_MARGIN + COLON_WIDTH;

        JLabel lblKey = new JLabel(key);
        lblKey.setFont(new Font("Arial", Font.BOLD, fontSize));
        lblKey.setBounds(labelX, y, colonX - labelX, 20);
        panel.add(lblKey);

        JLabel lblColon = new JLabel(":");
        lblColon.setFont(new Font("Arial", Font.BOLD, fontSize));
        lblColon.setBounds(colonX, y, COLON_WIDTH, 20);
        panel.add(lblColon);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Arial", Font.BOLD, fontSize));
        lblValue.setBounds(valueX, y, FRAME_WIDTH - valueX - MARGIN, 20);
        panel.add(lblValue);

        return y + LINE_SPACING; // untuk tambahin spasi antar baris
    }

    // Fungsi untuk bagian pekerjaan
    private void togglePekerjaan(boolean disable) {
        swastaCheck.setEnabled(!disable);
        pnsCheck.setEnabled(!disable);
        wiraswastaCheck.setEnabled(!disable);
        akademisiCheck.setEnabled(!disable);
    }

    // Fungsi untuk tombol submit
    private void handleSubmit() {
        if (checkDataForm()) {
            JOptionPane.showMessageDialog(frameInput, "Semua data harus diisi!", "Error",
            JOptionPane.ERROR_MESSAGE);
        } else {
            frameInput.dispose();
            insertUpdateDeleteData(false, false);
        }
    }

    private void handleUpdate() {
        frameInput.dispose();
        insertUpdateDeleteData(false, true);
    }

    private void handleDelete() {
        int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus data?", "Konfirmasi",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            frameInput.dispose();
            insertUpdateDeleteData(true, false);
        }
    }

    private boolean checkDataForm() {
        if (nikField.getText().isEmpty() ||
                namaField.getText().isEmpty() ||
                tempatLahirField.getText().isEmpty() ||
                alamatField.getText().isEmpty() ||
                rtRwField.getText().isEmpty() ||
                kelDesaField.getText().isEmpty() ||
                kecamatanField.getText().isEmpty() ||
                berlakuField.getText().isEmpty() ||
                kotaField.getText().isEmpty() ||
                agamaComboBox.getSelectedItem().toString().equalsIgnoreCase("Pilih Agama") ||
                statusComboBox.getSelectedItem().toString().equalsIgnoreCase("Pilih Status Perkawinan") ||

                (!swastaCheck.isSelected() && !pnsCheck.isSelected() && !wiraswastaCheck.isSelected()
                        && !akademisiCheck.isSelected() && !pengangguranCheck.isSelected())
                ||

                (!priaButton.isSelected() && !wanitaButton.isSelected()) ||

                (!golDarA.isSelected() && !golDarAB.isSelected() && !golDarB.isSelected() && !golDarO.isSelected()) ||

                (!wniButton.isSelected() && !wnaButton.isSelected())) {

            if (wnaButton.isSelected() && wnaField.getText().isEmpty()) {
                return true;
            }
            return true;
        } else {
            return false;
        }
    }

    private String getDataPekerjaan() {
        String data = "";
        if (swastaCheck.isSelected()) {
            data += swastaCheck.getText() + ", ";
        }
        if (pnsCheck.isSelected()) {
            data += pnsCheck.getText() + ", ";
        }
        if (wiraswastaCheck.isSelected()) {
            data += wiraswastaCheck.getText() + ", ";
        }
        if (akademisiCheck.isSelected()) {
            data += akademisiCheck.getText() + ", ";
        }
        if (pengangguranCheck.isSelected()) {
            data = pengangguranCheck.getText();
        }

        return data.replaceFirst(", $", "");
    }

    private String getDataKewarganegaraan() {
        if (wnaButton.isSelected()) {
            return wnaButton.getActionCommand() + " (" + wnaField.getText() + ")";
        }
        return wniButton.getActionCommand();
    }

    private String formatDate(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    private void showKTP(String nik) {
        // Hilangkan frame input
        frameInput.dispose();

        // Membuat frame utama
        frameKTP = new JFrame("KTP Display");
        frameKTP.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frameKTP.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frameKTP.setResizable(false);
        frameKTP.setLocationRelativeTo(null);

        // Panel utama
        JPanel panelUtama = new JPanel(null);
        panelUtama.setBackground(new Color(173, 216, 230)); // Warna biru muda

        // Variabel posisi awal
        int currentY = MARGIN;

        // Label untuk teks "PROVINSI DKI JAKARTA"
        currentY = tambahLabel(panelUtama, "Republik Harapan Bangsa", TITLE_FONT_SIZE, currentY, MARGIN, true) + 30;

        // Ambil data KTP & Tampilin Datanya
        Connection conn = DatabaseManager.connect();
        if (conn != null) {
            try {
                // Query SQL untuk mengambil data
                String sql = "SELECT * FROM data_ktp WHERE nik = '" + nik + "'";

                // Membuat statement
                Statement stmt = conn.createStatement();

                // Eksekusi query
                ResultSet rs = stmt.executeQuery(sql);

                // Format untuk tanggal
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                // Iterasi hasil
                while (rs.next()) {
                    Date tglLahir = rs.getDate("tanggal_lahir");
                    String formattedDate = tglLahir != null ? dateFormat.format(tglLahir) : "NULL";

                    String[][] data = {
                            { "NIK", rs.getString("nik") },
                            { "Nama", rs.getString("nama") },
                            { "Tempat/Tgl Lahir", rs.getString("tempat_lahir") + ", " + formattedDate },
                            { "Jenis Kelamin", rs.getString("jenis_kelamin") + "    Gol. Darah: "
                                    + rs.getString("golongan_darah") },
                            { "Alamat", rs.getString("alamat") },
                            { "    RT/RW", rs.getString("rt_rw") },
                            { "    Kel/Desa", rs.getString("kelurahan") },
                            { "    Kecamatan", rs.getString("kecamatan") },
                            { "Agama", rs.getString("agama") },
                            { "Status Perkawinan", rs.getString("status_perkawinan") },
                            { "Pekerjaan", rs.getString("pekerjaan") },
                            { "Kewarganegaraan", rs.getString("kewarganegaraan") },
                            { "Berlaku Hingga", rs.getString("berlaku_hingga") }
                    };

                    // Menampilkan data
                    for (String[] row : data) {
                        boolean isNik = row[0].equals("NIK");
                        currentY = tambahData(panelUtama, row[0], row[1], currentY,
                                isNik ? NIK_FONT_SIZE : LABEL_FONT_SIZE);
                    }

                    // Panel untuk foto
                    int fotoX = COLUMN_MARGIN + (FRAME_WIDTH - FOTO_WIDTH) / 2;
                    fotoPanel = new JLabel();
                    fotoPanel.setBounds(fotoX, MARGIN + 55, FOTO_WIDTH, FOTO_HEIGHT);
                    tampilkanGambar(fotoPanel, rs.getString("path_foto"));
                    panelUtama.add(fotoPanel);

                    // Label untuk tanggal hari ini
                    JLabel lblTempatBuat = new JLabel(rs.getString("tempat_dikeluarkan"));
                    lblTempatBuat.setFont(new Font("Arial", Font.BOLD, LABEL_FONT_SIZE + 5));
                    lblTempatBuat.setBounds(fotoX, fotoPanel.getY() + FOTO_HEIGHT + 5, FOTO_WIDTH, 20);
                    lblTempatBuat.setHorizontalAlignment(SwingConstants.CENTER);
                    panelUtama.add(lblTempatBuat);

                    JLabel lblTanggalBuat = new JLabel(rs.getString("tanggal_dikeluarkan"));
                    lblTanggalBuat.setFont(new Font("Arial", Font.BOLD, LABEL_FONT_SIZE));
                    lblTanggalBuat.setBounds(fotoX, fotoPanel.getY() + lblTempatBuat.getHeight() + FOTO_HEIGHT + 3,
                            FOTO_WIDTH, 20);
                    lblTanggalBuat.setHorizontalAlignment(SwingConstants.CENTER);
                    panelUtama.add(lblTanggalBuat);

                    // Panel untuk tanda tangan
                    tandaTanganPanel = new JLabel();
                    tandaTanganPanel.setBounds(fotoX, lblTempatBuat.getY() + 40, FOTO_WIDTH, 40);
                    tampilkanGambar(tandaTanganPanel, rs.getString("path_tanda_tangan"));
                    panelUtama.add(tandaTanganPanel);
                }

                // Tutup koneksi
                rs.close();
                stmt.close();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Tombol back to main menu
        JButton rekamButton = new JButton("Back To Main Menu");

        // Lebar dan tinggi tombol
        int buttonWidth = 200;
        int buttonHeight = 30;

        int buttonX = (FRAME_WIDTH - buttonWidth) / 2;
        int buttonY = FRAME_HEIGHT - buttonHeight - MARGIN - 50; // kurangin 50 supaya terlihat buttonnya

        rekamButton.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
        panelUtama.add(rekamButton);

        // Rekam Button Action
        rekamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameKTP.dispose();
                showMainMenu();
            }
        });

        // Menambahkan panel utama ke frame
        frameKTP.add(panelUtama);
        frameKTP.setVisible(true);
    }

    public String[] getDataFromDB(boolean isSearch) {
        String[] data = { "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" };

        if (isSearch) {
            Connection conn = DatabaseManager.connect();
            if (conn != null) {
                try {
                    // Query SQL untuk mengambil data
                    String sql = "SELECT * FROM data_ktp WHERE nik = '" + nikSearch.getText() + "'";

                    // Membuat statement
                    Statement stmt = conn.createStatement();

                    // Eksekusi query
                    ResultSet rs = stmt.executeQuery(sql);

                    // Format untuk tanggal
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                    // Iterasi hasil
                    while (rs.next()) {
                        Date tglLahir = rs.getDate("tanggal_lahir");
                        String formattedDate = tglLahir != null ? dateFormat.format(tglLahir) : "NULL";

                        data[0] = rs.getString("nik");
                        data[1] = rs.getString("nama");
                        data[2] = rs.getString("tempat_lahir");
                        data[3] = formattedDate;
                        data[4] = rs.getString("jenis_kelamin");
                        data[5] = rs.getString("golongan_darah");
                        data[6] = rs.getString("alamat");
                        data[7] = rs.getString("rt_rw");
                        data[8] = rs.getString("kelurahan");
                        data[9] = rs.getString("kecamatan");
                        data[10] = rs.getString("agama");
                        data[11] = rs.getString("status_perkawinan");
                        data[12] = rs.getString("pekerjaan");
                        data[13] = rs.getString("kewarganegaraan");
                        data[14] = rs.getString("path_foto");
                        data[15] = rs.getString("path_tanda_tangan");
                        data[16] = rs.getString("berlaku_hingga");
                        data[17] = rs.getString("tempat_dikeluarkan");
                        data[18] = rs.getString("tanggal_dikeluarkan");
                    }

                    // Tutup koneksi
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return data;
    }

    public void showSearchFrame() {
        JFrame searchFrame = new JFrame();
        searchFrame.setSize(300, 200);
        searchFrame.setLayout(new GridLayout(3, 1));
        searchFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        searchFrame.setLocationRelativeTo(null);
        searchFrame.setResizable(false);

        JLabel titleLabel = new JLabel("SEARCH E-KTP", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        searchFrame.add(titleLabel);

        JPanel panelSearch = new JPanel(new GridLayout(1, 2));
        panelSearch.add(new JLabel("MASUKKAN NIK"));
        nikSearch = new JTextField();
        panelSearch.add(nikSearch);
        searchFrame.add(panelSearch);

        // Tombol Search
        JButton searchButton = new JButton("Search");
        searchFrame.add(searchButton);

        // Search Button Action
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchFrame.dispose();
                searchNIK();
            }
        });

        searchFrame.setVisible(true);
    }

    public void searchNIK() {
        Connection conn = DatabaseManager.connect();
        if (conn != null) {
            try {
                // Query SQL untuk mengambil data
                String sql = "SELECT * FROM data_ktp WHERE nik = '" + nikSearch.getText() + "'";

                // Membuat statement
                Statement stmt = conn.createStatement();

                if (stmt.execute(sql)) {
                    createInput(true, getDataFromDB(true));
                } else {
                    JOptionPane.showMessageDialog(null, "Maaf NIK tidak terdaftar", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                }

                // Tutup koneksi
                stmt.close();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showMainMenu() {
        JFrame frameMainMenu = new JFrame();
        frameMainMenu.setSize(300, 200);
        frameMainMenu.setLayout(new GridLayout(4, 1));
        frameMainMenu.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frameMainMenu.setLocationRelativeTo(null);
        frameMainMenu.setResizable(false);

        JLabel titleLabel = new JLabel("DATABASE E-KTP", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        frameMainMenu.add(titleLabel);

        // Tombol Perekaman
        JButton rekamButton = new JButton("Perekaman");
        frameMainMenu.add(rekamButton);

        // Rekam Button Action
        rekamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameMainMenu.dispose();
                createInput(false, getDataFromDB(false));
            }
        });

        // Tombol Pencarian
        JButton searchButton = new JButton("Pencarian");
        frameMainMenu.add(searchButton);

        // Search Button Action
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameMainMenu.dispose();
                showSearchFrame();
            }
        });

        // Tombol Exit
        JButton exitButton = new JButton("Exit");
        frameMainMenu.add(exitButton);

        // Rekam Button Action
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Menutup JFrame
                frameMainMenu.dispose();
                System.exit(0);
            }
        });

        frameMainMenu.setVisible(true);

    }

    private void insertUpdateDeleteData(boolean delete, boolean update) {
        // Data KTP
        // Data tanggal Lahir
        Date tglLahir = (Date) ((UtilDateModel) (tanggalLahir.getModel())).getValue();
        String formattedTglLahir = "";
        if (tglLahir != null) {
            formattedTglLahir = formatDate(tglLahir, "yyyy-MM-dd");
        }

        String dateMadeStr = dateMade.getText(); // Mendapatkan teks dari input
        String formattedDateMade = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date dateMadeDate = sdf.parse(dateMadeStr);
            formattedDateMade = new SimpleDateFormat("yyyy-MM-dd").format(dateMadeDate);
            // System.out.println("Formatted Date: " + formattedDateMade); // Buat cek Error
        } catch (ParseException e) {
            System.err.println("Invalid date format: " + e.getMessage());
        }

        String fotoPath = pathPhoto;
        if (selectedFoto != null) {
            fotoPath = selectedFoto.getAbsolutePath();
        }

        String ttdPath = pathTtd;
        if (selectedTTD != null) {
            ttdPath = selectedTTD.getAbsolutePath();
        }
        String dataKTP[] = {
                nikField.getText().toUpperCase(),
                namaField.getText().toUpperCase(),
                tempatLahirField.getText().toUpperCase(),
                formattedTglLahir,
                genderGroup.getSelection().getActionCommand(),
                golDarahGroup.getSelection().getActionCommand(),
                alamatField.getText().toUpperCase(),
                rtRwField.getText(),
                kelDesaField.getText().toUpperCase(),
                kecamatanField.getText().toUpperCase(),
                agamaComboBox.getSelectedItem().toString(),
                statusComboBox.getSelectedItem().toString(),
                getDataPekerjaan().toUpperCase(),
                getDataKewarganegaraan().toUpperCase(),
                berlakuField.getText(),
                formattedDateMade,
                kotaField.getText().toUpperCase(),
                fotoPath,
                ttdPath
        };

        // Menyimpan ke database
        Connection conn = DatabaseManager.connect();
        String sql = "";

        if (update) {
            sql = "UPDATE data_ktp SET " +
                    "nik = ?, " +
                    "nama = ?, " +
                    "tempat_lahir = ?, " +
                    "tanggal_lahir = ?, " +
                    "jenis_kelamin = ?, " +
                    "golongan_darah = ?, " +
                    "alamat = ?, " +
                    "rt_rw = ?, " +
                    "kelurahan = ?, " +
                    "kecamatan = ?, " +
                    "agama = ?, " +
                    "status_perkawinan = ?, " +
                    "pekerjaan = ?, " +
                    "kewarganegaraan = ?, " +
                    "berlaku_hingga = ?, " +
                    "tanggal_dikeluarkan = ?, " +
                    "tempat_dikeluarkan = ?, " +
                    "path_foto = ?, " +
                    "path_tanda_tangan = ? " +
                    "WHERE nik = ?";

        } else if (delete) {
            sql = "DELETE FROM data_ktp WHERE nik = ?;";
        } else {
            sql = "INSERT INTO data_ktp (nik, nama, tempat_lahir, tanggal_lahir, jenis_kelamin, golongan_darah, alamat, rt_rw, kelurahan, kecamatan, agama, status_perkawinan, pekerjaan, kewarganegaraan, berlaku_hingga, tanggal_dikeluarkan, tempat_dikeluarkan, path_foto, path_tanda_tangan) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            if (delete) {
                pstmt.setString(1, nikSearch.getText());

            } else {

                pstmt.setString(1, dataKTP[0]);
                pstmt.setString(2, dataKTP[1]);
                pstmt.setString(3, dataKTP[2]);
                pstmt.setDate(4, java.sql.Date.valueOf(dataKTP[3])); // Konversi ke tipe DATE
                pstmt.setString(5, dataKTP[4]);
                ;
                pstmt.setString(6, dataKTP[5]);
                pstmt.setString(7, dataKTP[6]);
                pstmt.setString(8, dataKTP[7]);
                pstmt.setString(9, dataKTP[8]);
                pstmt.setString(10, dataKTP[9]);
                pstmt.setString(11, dataKTP[10]);
                pstmt.setString(12, dataKTP[11]);
                pstmt.setString(13, dataKTP[12]);
                pstmt.setString(14, dataKTP[13]);
                pstmt.setString(15, dataKTP[14]);
                pstmt.setDate(16, java.sql.Date.valueOf(dataKTP[15]));
                pstmt.setString(17, dataKTP[16]);
                pstmt.setString(18, dataKTP[17]);
                pstmt.setString(19, dataKTP[18]);

                if (update) {
                    pstmt.setString(20, nikSearch.getText());
                }
            }

            pstmt.executeUpdate();
            if (update) {
                JOptionPane.showMessageDialog(frameInput, "Data berhasil diupdate!");
                showKTP(nikSearch.getText());
            } else if (delete) {
                JOptionPane.showMessageDialog(frameInput, "Data berhasil dihapus!");
                showMainMenu();
            } else {
                JOptionPane.showMessageDialog(frameInput, "Data berhasil disimpan!");
                // tampilin hasil ktp-nya
                showKTP(nikField.getText());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frameInput, "Gagal melakukan operasi data!");
        }
    }

    public static void main(String[] args) {
        new InputDataE_KTP();
    }
}
