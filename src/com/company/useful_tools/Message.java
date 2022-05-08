package com.company.useful_tools;

import javax.swing.JOptionPane;

public class Message {
    public void getNoneExistMessage(boolean soundFlag) {
        if (soundFlag)
            getSound("src\\com\\company\\raw\\h.wav");

        String title = "None exist error";
        String errorMessage = "Make sure the image exists!";
        JOptionPane.showMessageDialog(
                null,
                errorMessage,
                title,
                JOptionPane.ERROR_MESSAGE
        );
    }

    public void getNotChosenMessage(boolean soundFlag) {
        if (soundFlag)
            getSound("src\\com\\company\\raw\\attention.wav");

        JOptionPane.showMessageDialog(
                null,
                "You didn't choose anything jpeg file!\n",
                "Not chosen error",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void getIncorrectExtensionMessage(boolean soundFlag) {
        if (soundFlag)
            getSound("src\\com\\company\\raw\\oh_shit_iam_sorry.wav");

        JOptionPane.showMessageDialog(
                null,
                "The file of wrong extension!\n" +
                        "Check please extension!",
                "Extension error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public void getNotValidNameMessage(boolean soundFlag) {
        if (soundFlag)
            getSound("src\\com\\company\\raw\\woo.wav");

        JOptionPane.showMessageDialog(
                null,
                "The name of file is wrong!\n" +
                        "Please check name and use only latin alphabet!",
                "Name error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public void getNotSavedMessage(boolean soundFlag) {
        if (soundFlag)
            getSound("src\\com\\company\\raw\\fuck_you1.wav");

        JOptionPane.showMessageDialog(
                null,
                "You didn't save image in currently directory!",
                "Save error",
                JOptionPane.QUESTION_MESSAGE
        );
    }

    public void getParametersErrorMessage(Parameters parameters, boolean soundFlag) {
        if (soundFlag)
            getSound("src\\com\\company\\raw\\do_you_like_what_you_see.wav");

        JOptionPane.showMessageDialog(
                null,
                "Incorrect parameter values: " + parameters + ". Check please values!",
                "Parameters error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void getParametersMessage(Parameters parameters, boolean soundFlag) {
        if (soundFlag)
            getSound("src\\com\\company\\raw\\do_you_like_what_you_see.wav");

        JOptionPane.showMessageDialog(
                null,
                parameters,
                "Parameters",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    private static void getSound(String path) {
        try {
            Audio audio = new Audio(path);
            audio.sound();
        } catch (Exception ignored) {
        }
    }
}
