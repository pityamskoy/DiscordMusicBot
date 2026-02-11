package github.pityamskoy.musicbot.commands.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.ByteBuffer;


public final class AudioPlayerSendHandler implements AudioSendHandler {
    private final AudioPlayer audioPlayer;
    private final ByteBuffer buffer = ByteBuffer.allocate(1024);
    private final MutableAudioFrame frame = new MutableAudioFrame();

    public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.frame.setBuffer(this.buffer);
    }

    @Override
    public boolean canProvide() {
        return this.audioPlayer.provide(this.frame);
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        return this.buffer.flip();
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}
