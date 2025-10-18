package github.pityamskoy.musicbot.commands.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

@SuppressWarnings(value = {"ClassCanBeRecord"})
public class AudioLoadResultHandlerImpl implements AudioLoadResultHandler {
    private final TrackScheduler trackScheduler;

    public AudioLoadResultHandlerImpl(AudioPlayer audioPlayer) {
        this.trackScheduler = new TrackScheduler(audioPlayer);
    }

    @Override
    public void trackLoaded(AudioTrack audioTrack) {
        trackScheduler.queue(audioTrack);
    }

    @Override
    public void playlistLoaded(AudioPlaylist audioPlaylist) {
        trackScheduler.queue(audioPlaylist.getTracks().get(0));
    }

    @Override
    public void noMatches() {

    }

    @Override
    public void loadFailed(FriendlyException e) {

    }
}
