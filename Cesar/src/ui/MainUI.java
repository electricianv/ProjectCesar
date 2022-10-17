package ui;

import main.Crypt;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

public class MainUI extends JFrame {
    private File sourceFile;

    public MainUI() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(350, 250);
        setTitle("Crypt program");
        setLayout(null);
        setLocation(200, 200);
        initUi();
        setVisible(true);
    }

    private void doCrypt(int key, String resultPath) {
        try {
            String raw = Files.readString(Paths.get(sourceFile.getAbsolutePath()));
            String crypted = Crypt.encrypt(raw, key);
            Files.writeString(Paths.get(resultPath), crypted);
            JOptionPane.showMessageDialog(null, "Ready!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void doDecrypt(int key, String resultPath) {
        try {
            String raw = Files.readString(Paths.get(sourceFile.getAbsolutePath()));
            String decrypted = Crypt.uncrypt(raw, key);
            try (PrintWriter out = new PrintWriter(resultPath)) {
                out.println(decrypted);
            }
            JOptionPane.showMessageDialog(null, "Ready!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void doBrute(String resultPath) {
        try {
            String raw = Files.readString(Paths.get(sourceFile.getAbsolutePath()));
            HashMap<Integer, String> decryptedTexts = new HashMap<>();

            for (int i = 1; i < Crypt.letters.length; i++) {
                String probablyDecrypted = Crypt.uncrypt(raw, i);
                int res = Crypt.checkResult(probablyDecrypted);
                decryptedTexts.put(res, probablyDecrypted);
            }

            try (PrintWriter out = new PrintWriter(resultPath)) {
                out.println(decryptedTexts.get(Collections.min(decryptedTexts.keySet())));
            }
            JOptionPane.showMessageDialog(null, "Ready!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void initUi() {
        // creating file input and setting its filters
        final JFileChooser fchooser = new JFileChooser();
        fchooser.setAcceptAllFileFilterUsed(false);
        fchooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                return getExt(f).equals("txt");
            }

            @Override
            public String getDescription() {
                return "txt";
            }
        });
        // creating menu with elements
        String[] menuElements = {
            "Encrypt",
            "Decrypt with key",
            "Decrypt with bruto force",
        };
        JComboBox<String> mainMenu = new JComboBox<>(menuElements);
        mainMenu.setLocation(10, 10);
        mainMenu.setSize(300, 30);
        mainMenu.setVisible(true);
        add(mainMenu);
        // creating result file path inputs
        JLabel loadResultLabel = new JLabel("Result file path:");
        loadResultLabel.setLocation(10, 45);
        loadResultLabel.setSize(150, 30);
        add(loadResultLabel);

        JTextField resultAddress = new JTextField();
        resultAddress.setText(System.getProperty("user.dir") + "\\res.txt");
        resultAddress.setLocation(10, 75);
        resultAddress.setSize(250, 30);
        add(resultAddress);

        JLabel keyLabel = new JLabel("Key:");
        keyLabel.setLocation(10, 100);
        keyLabel.setSize(100, 30);
        add(keyLabel);

        JTextField keyInput = new JTextField();
        keyInput.setLocation(10, 130);
        keyInput.setSize(100, 30);
        add(keyInput);

        JLabel loadSourceLabel = new JLabel("Source file:");
        loadSourceLabel.setLocation(120, 100);
        loadSourceLabel.setSize(100, 30);
        add(loadSourceLabel);
        // creating upload button and setting a callback with file dialog
        JButton loadSource = new JButton("Upload");
        loadSource.setLocation(120, 130);
        loadSource.setSize(100, 30);
        loadSource.addActionListener(e -> {
            int returnVal = fchooser.showOpenDialog(MainUI.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                sourceFile = fchooser.getSelectedFile();
            }
        });
        add(loadSource);
        // creating start button, checking requirements and run
        JButton startButton = new JButton("Start");
        startButton.setLocation(10, 170);
        startButton.setSize(150, 30);
        startButton.setBackground(Color.GREEN);
        startButton.addActionListener(e -> {
            if ((keyInput.getText().length() > 0 
                    || mainMenu.getSelectedIndex() == 2) 
                    && sourceFile != null 
                    && resultAddress.getText().length() > 0
            ) {
                try {
                    int key = 0;

                    if (mainMenu.getSelectedIndex() != 2) {
                        key = Integer.parseInt(keyInput.getText());
                    }
                    // runs method, depending on user menu select
                    if (Objects.equals(mainMenu.getSelectedItem(), menuElements[0])) {
                        doCrypt(key, resultAddress.getText());
                    } else if (Objects.equals(mainMenu.getSelectedItem(), menuElements[1])) {
                        doDecrypt(key, resultAddress.getText());
                    } else {
                        doBrute(resultAddress.getText());
                    }
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(null, "Key must be digital");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Fill all fields!");
            }
        });
        add(startButton);
    }
    // method to get extension from file name
    public static String getExt(File f) {
        String ext = "";
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }
}
