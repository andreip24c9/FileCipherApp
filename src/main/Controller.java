package main;

import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import security.CryptoException;
import security.CryptoUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Controller {

    public Button btn_choose;
    public ImageView img_tick;
    public Button btn_encrypt;
    public Button btn_decrypt;
    public PasswordField tv_password;
    public TextArea ta_file;
    public Button btn_go;
    public Label lb_path;

    private File mFile;

    public void chooseFileCLicked() {
        final FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(btn_choose.getScene().getWindow());
        if (file != null) {
            openFile(file);
        }
    }

    public void encryptClicked() {
        btn_encrypt.setDefaultButton(true);
        btn_decrypt.setDefaultButton(false);

    }

    public void decryptClicked() {
        btn_encrypt.setDefaultButton(false);
        btn_decrypt.setDefaultButton(true);
    }

    public void goClicked() {
        String passPhrase = tv_password.getText();
        if (mFile != null) {
            if (passPhrase != null && passPhrase.length() > 0) {
                try {
                    if (btn_encrypt.isDefaultButton()) {
                        CryptoUtils.encrypt(passPhrase, mFile, mFile);
                    } else if (btn_decrypt.isDefaultButton()) {
                        CryptoUtils.decrypt(passPhrase, mFile, mFile);
                    }
                    openFile(mFile);
                } catch (CryptoException e) {
                    showError(e.getMessage());
                }
            } else {
                showError("Please provide a password");
            }
        } else {
            showError("Please choose a file");
        }
    }

    private void openFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
            ta_file.setText(everything);
            img_tick.setVisible(true);
            mFile = file;
            lb_path.setText(mFile.getPath());
        } catch (IOException e) {
            resetScene();
            showError(e.getMessage());
        }
    }

    private void showError(String error) {
        AlertBox.display("Error", error);
    }

    private void resetScene(){
        mFile = null;
        ta_file.setText("");
        lb_path.setText("");
        img_tick.setVisible(false);
    }

    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("View Files");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
    }
}
