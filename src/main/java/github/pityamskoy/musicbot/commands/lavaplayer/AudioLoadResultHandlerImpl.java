package github.pityamskoy.musicbot.commands.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import javax.naming.SizeLimitExceededException;

@SuppressWarnings(value = {"ClassCanBeRecord"})
public final class AudioLoadResultHandlerImpl implements AudioLoadResultHandler {
    private final TrackScheduler trackScheduler;

    public AudioLoadResultHandlerImpl(Long guildId) {
        this.trackScheduler = TrackScheduler.getGuildScheduler(guildId);
    }

    @Override
    public void trackLoaded(AudioTrack audioTrack) {
        try {
            trackScheduler.enqueue(audioTrack);
        } catch (SizeLimitExceededException _) {}
    }

    @Override
    public void playlistLoaded(AudioPlaylist audioPlaylist) {
        try {
            trackScheduler.enqueue(audioPlaylist.getTracks().getFirst());
        } catch (SizeLimitExceededException _) {}
    }

    @Override
    public void noMatches() {

    }

    @Override
    public void loadFailed(FriendlyException e) {

    }
}
