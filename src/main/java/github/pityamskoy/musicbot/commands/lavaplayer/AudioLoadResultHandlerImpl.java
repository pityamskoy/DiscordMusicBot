package github.pityamskoy.musicbot.commands.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

@SuppressWarnings(value = {"ClassCanBeRecord"})
public final class AudioLoadResultHandlerImpl implements AudioLoadResultHandler {
    private final TrackScheduler trackScheduler;

    public AudioLoadResultHandlerImpl(Long guildId) {
        this.trackScheduler = TrackScheduler.getGuildScheduler(guildId);
    }

    @Override
    public void trackLoaded(AudioTrack audioTrack) {
        try {
            trackScheduler.queue(audioTrack);
        } catch (RuntimeException e) {
            //add exception class to transfer error to higher level
            return;
        }
    }

    @Override
    public void playlistLoaded(AudioPlaylist audioPlaylist) {
        trackScheduler.queue(audioPlaylist.getTracks().getFirst());
    }

    @Override
    public void noMatches() {

    }

    @Override
    public void loadFailed(FriendlyException e) {

    }
}
