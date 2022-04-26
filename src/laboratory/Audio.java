package laboratory;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Audio {

    private final String track; // адрес трека(файла)
    private Clip clip = null;// ссылка на объект класса
    private FloatControl volumeC = null;// контролер громкости
    private double wt; //уровень громкости
    private boolean pl_audio;// воспроизведение звука

    private long timer_p = 0;// начальное время для таймера звучания
    private long timer_f = 0;// конечное время звучания
    private long timer_d;// (количество миллисекунд) длительность трека

    //конструктор (адрес файла, уровень громкости)
    public Audio(String track, double wt) {
        this.track = track;
        this.wt = wt;
        this.pl_audio = false;
    }

    //конструктор (адрес файла, уровень громкости, кол.милсек звук трек)
    public Audio(String track, double wt, long timer_d) {
        this.timer_d = timer_d;
        this.track = track;
        this.pl_audio = false;
        this.wt = wt;
    }


    public void sound() {
        File f = new File(this.track);// передаем звуковой файл в f
        //поток для записи и считывания
        AudioInputStream tr = null; // обьект потока AudioInputStream пуст
        try {
            tr = AudioSystem.getAudioInputStream(f); // Получаем AudioInputStream (нужный файл)
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        try {
            clip = AudioSystem.getClip();//Получаем реализацию интерфейса Clip
            clip.open(tr); //Загружаем наш звуковой поток в Clip
            //Получаем контроллер громкости
            volumeC = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            clip.setFramePosition(0); //устанавливаем указатель на старт
            clip.start(); //Поехали!!!

        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    // пероигрывать звук объкта одиночное проигрывание с stop()
    public void play() {
        File f = new File(this.track);// передаем звуковой файл в f
        //поток для записи и считывания
        AudioInputStream tr = null; // обьект потока AudioInputStream пуст
        try {
            tr = AudioSystem.getAudioInputStream(f); // Получаем AudioInputStream (нужный файл)
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        try {
            clip = AudioSystem.getClip();//Получаем реализацию интерфейса Clip
            clip.open(tr); //Загружаем наш звуковой поток в Clip
            //Получаем контроллер громкости
            volumeC = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            if (!this.pl_audio) {
                clip.setFramePosition(0); //устанавливаем указатель на старт
                clip.start(); //Поехали!!!
                this.pl_audio = true; // играет
            }

        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }


    }

    //стоп
    public void stop() {

        clip.stop();
        clip.close(); //Закрываем
        this.pl_audio = false;
    }

    //уровень громкости
    public void setVolume() {
        if (wt < 0) wt = 0;
        if (wt > 1) wt = 1;
        float min = volumeC.getMinimum();
        float max = volumeC.getMaximum();
        volumeC.setValue((max - min) * (float) wt + min);
    }

    // повтор (работает с треками запущенными методом play())
    public void repeat() {
        if (this.pl_audio)
            clip.loop(10); //повторить n раз
    }

    // таймер звука ( для треков запущ. play() и конструктор с timer_d)
    public void timer_play() {
        if (timer_p == 0) { //если таймер не запущен
            timer_p = System.currentTimeMillis();// получаем текущее время милсек
            timer_f = timer_p + timer_d; // конечное время трека
        }
        if (timer_f <= System.currentTimeMillis()) { //если время звучания меньше текущювремени
            this.stop();//стоп трек
            timer_p = 0;// обнуляем счетчик
        }
    }
}