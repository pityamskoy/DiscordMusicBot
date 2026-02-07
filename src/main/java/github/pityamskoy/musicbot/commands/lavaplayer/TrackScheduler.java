package github.pityamskoy.musicbot.commands.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import javax.naming.SizeLimitExceededException;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class TrackScheduler extends AudioEventAdapter {
    private static final HashMap<Long, TrackScheduler> schedulers = new HashMap<>();
    private final BlockingQueue<AudioTrack> queue = new LinkedBlockingQueue<>();
    private final AudioPlayer player;
    private boolean isTrackRepeat = false;
    private boolean isQueueRepeat = false;

    public TrackScheduler(Long guildId, AudioPlayer player) {
        this.player = player;
        schedulers.put(guildId, this);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (isTrackRepeat) {
            player.startTrack(track.makeClone(), false);
            return;
        }

        if (isQueueRepeat) {
            player.startTrack(queue.poll(), false);
            try {
                enqueue(track);
            } catch (SizeLimitExceededException _) {}
        } else {
            player.startTrack(queue.poll(), false);
        }
    }

    public void enqueue(AudioTrack track) throws SizeLimitExceededException {
        boolean isCapableToPut = queue.offer(track);

        if (!isCapableToPut) {
            throw new SizeLimitExceededException("Failed to add this track to the queue");
        }
    }

    public void skip() {
        queue.poll();
    }

    public void clear() {
        queue.clear();
    }

    public static TrackScheduler getGuildScheduler(Long guildId) {
        return schedulers.get(guildId);
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public BlockingQueue<AudioTrack> getQueue() {
        return queue;
    }

    public boolean isTrackRepeat() {
        return isTrackRepeat;
    }

    public void setTrackRepeat(boolean repeat) {
        isTrackRepeat = repeat;
    }

    public boolean isQueueRepeat() {
        return isQueueRepeat;
    }

    public void setQueueRepeat(boolean repeat) {
        isQueueRepeat = repeat;
    }
}