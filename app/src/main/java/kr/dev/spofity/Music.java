package kr.dev.spofity;

public class Music {

    int music;
    String musicName;
    int image;


    public Music(int music, String musicName, int image) {
        this.music = music;
        this.musicName = musicName;
        this.image = image;
    }

    public int getMusic() {
        return music;
    }

    public void setMusic(int music) {
        this.music = music;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Music{" +
                "music=" + music +
                ", musicName='" + musicName + '\'' +
                ", image=" + image +
                '}';
    }
}
