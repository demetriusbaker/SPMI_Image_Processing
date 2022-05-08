package com.company.useful_tools;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public record Audio(String track) {

    public void sound() {
        File f = new File(this.track);

        AudioInputStream tr = null;
        try {
            tr = AudioSystem.getAudioInputStream(f);
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(tr);

            clip.setFramePosition(0);
            clip.start();

        } catch (LineUnavailableException | IOException ignored) {
        }
    }
}