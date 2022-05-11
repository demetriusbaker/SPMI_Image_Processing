package com.company.useful_tools;

import javax.swing.*;
import java.awt.*;

public class Parameters {
    private final Tools tools;
    private JFrame frame;

    public int size = 15, sigmaX = 10, threshold1 = 20, threshold2 = 100, teeth = 3;

    public Parameters(Tools tools) {
        this.tools = tools;
    }

    public void setSize() {
        frame = tools.createNewChoiceWindow("Change nuclear size", 700, 200);

        JSlider slider = new JSlider(1, 15, 1);
        slider.setValue(size);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setPaintTrack(true);
        slider.setMinorTickSpacing(1);
        slider.setMajorTickSpacing(1);

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(2, 2));
        northPanel.add(new JLabel("Set from 1 to 15", SwingConstants.CENTER));
        northPanel.add(slider);
        frame.add(northPanel, BorderLayout.NORTH);

        JPanel southPanel = new JPanel();
        JButton button = new JButton("Confirm");
        button.addActionListener(e -> size = slider.getValue());
        southPanel.add(button);
        frame.add(southPanel, BorderLayout.SOUTH);
    }

    public void setSigmaX() {
        frame = tools.createNewChoiceWindow("Change sigma X", 700, 200);

        JSlider slider = new JSlider(0, 100, 1);
        slider.setValue(sigmaX);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setPaintTrack(true);
        slider.setMinorTickSpacing(5);
        slider.setMajorTickSpacing(10);

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(2, 2));
        northPanel.add(new JLabel("Set from 0 to 100", SwingConstants.CENTER));
        northPanel.add(slider);
        frame.add(northPanel, BorderLayout.NORTH);

        JPanel southPanel = new JPanel();
        JButton button = new JButton("Confirm");
        button.addActionListener(e -> sigmaX = slider.getValue());
        southPanel.add(button);
        frame.add(southPanel, BorderLayout.SOUTH);
    }

    public void setThreshold1() {
        frame = tools.createNewChoiceWindow("Change threshold 1", 700, 200);

        JSlider slider = new JSlider(0, 255, 1);
        slider.setValue(threshold1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setPaintTrack(true);
        slider.setMinorTickSpacing(5);
        slider.setMajorTickSpacing(10);

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(2, 2));
        northPanel.add(new JLabel("Set from 0 to 255", SwingConstants.CENTER));
        northPanel.add(slider);
        frame.add(northPanel, BorderLayout.NORTH);

        JPanel southPanel = new JPanel();
        JButton button = new JButton("Confirm");
        button.addActionListener(e -> threshold1 = slider.getValue());
        southPanel.add(button);
        frame.add(southPanel, BorderLayout.SOUTH);
    }

    public void setThreshold2() {
        frame = tools.createNewChoiceWindow("Change threshold 2", 700, 200);

        JSlider slider = new JSlider(0, 255, 1);
        slider.setValue(threshold2);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setPaintTrack(true);
        slider.setMinorTickSpacing(5);
        slider.setMajorTickSpacing(10);

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(2, 2));
        northPanel.add(new JLabel("Set from 0 to 255", SwingConstants.CENTER));
        northPanel.add(slider);
        frame.add(northPanel, BorderLayout.NORTH);

        JPanel southPanel = new JPanel();
        JButton button = new JButton("Confirm");
        button.addActionListener(e -> threshold2 = slider.getValue());
        southPanel.add(button);
        frame.add(southPanel, BorderLayout.SOUTH);
    }

    public void setTooth() {
        frame = tools.createNewChoiceWindow("Change tooth count", 500, 200);

        JSlider slider = new JSlider(1, 10, 1);
        slider.setValue(teeth);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setPaintTrack(true);
        slider.setMinorTickSpacing(1);
        slider.setMajorTickSpacing(1);

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(2, 2));
        northPanel.add(new JLabel("Set from 1 to 10", SwingConstants.CENTER));
        northPanel.add(slider);
        frame.add(northPanel, BorderLayout.NORTH);

        JPanel southPanel = new JPanel();
        JButton button = new JButton("Confirm");
        button.addActionListener(e -> teeth = slider.getValue());
        southPanel.add(button);
        frame.add(southPanel, BorderLayout.SOUTH);
    }

    public void showParameters() {
        Message.getParametersMessage(this, true);
    }

    @Override
    public String toString() {
        return "Parameters{" +
                "\nsize = " + size +
                "\nsigmaX = " + sigmaX +
                "\nthreshold1 = " + threshold1 +
                "\nthreshold2 = " + threshold2 +
                "\nteeth = " + teeth +
                "\n}";
    }
}
