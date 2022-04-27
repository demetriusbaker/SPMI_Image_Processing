package laboratory;

import javax.swing.*;

public class MessageError {
    private Audio audio;

    public void getNoneExistMessage(Exception exception, boolean soundFlag) {
        if (soundFlag) {
            audio = new Audio("src\\laboratory\\raw\\oh-shit-iam-sorry.wav", 0.8);
            audio.sound();
        }

        String title = "Processing error";
        String errorMessage = "You should to apply any changes for this picture, " +
                "then you can build histograms!";
        JOptionPane.showMessageDialog(
                null,
                exception + "!\n" + errorMessage,
                title,
                JOptionPane.ERROR_MESSAGE
        );
    }

    public void getUnknownErrorMessage(Exception exception, boolean soundFlag) {
        if (soundFlag) {
            audio = new Audio("src\\laboratory\\raw\\fuck-you1.wav", 0.8);
            audio.sound();
        }

        JOptionPane.showMessageDialog(
                null,
                "What's the hell?!\n" +
                        exception + "\n" +
                        exception.getMessage() + "\n",
                "Oh shit error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public void getNotChosenMessage(boolean soundFlag) {
        if (soundFlag) {
            audio = new Audio("src\\laboratory\\raw\\attention.wav", 0.8);
            audio.sound();
        }

        JOptionPane.showMessageDialog(
                null,
                "You didn't choose anything jpeg file!\n",
                "Choose jpeg image",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void getIncorrectExtensionMessage(boolean soundFlag) {
        if (soundFlag) {
            audio = new Audio("src\\laboratory\\raw\\oh-shit-iam-sorry.wav", 0.8);
            audio.sound();
        }

        JOptionPane.showMessageDialog(
                null,
                "The file of wrong extension!\n" +
                        "Check please extension!",
                "Extension error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public void getNotValidNameMessage(boolean soundFlag) {
        if (soundFlag) {
            audio = new Audio("src\\laboratory\\raw\\woo.wav", 0.8);
            audio.sound();
        }

        JOptionPane.showMessageDialog(
                null,
                "The name of file is wrong!\n" +
                        "Please check name and use only latin alphabet!",
                "Name error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public void getNotSavedMessage(boolean soundFlag) {
        if (soundFlag) {
            audio = new Audio("src\\laboratory\\raw\\oh-shit-iam-sorry.wav", 0.8);
            audio.sound();
        }

        JOptionPane.showMessageDialog(
                null,
                "You didn't save image in currently directory!",
                "Save error",
                JOptionPane.QUESTION_MESSAGE
        );
    }
}
