package github.pityamskoy.musicbot.commands.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import javax.naming.SizeLimitExceededException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class TrackScheduler extends AudioEventAdapter {
    private final BlockingQueue<AudioTrack> queue = new LinkedBlockingQueue<>();
    private final AudioPlayer player;
    private boolean isTrackRepeat = false;
    private boolean isQueueRepeat = false;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            if (isTrackRepeat) {
                player.startTrack(track.makeClone(), false);
                return;
            }

            if (isQueueRepeat) {
                if (!queue.isEmpty()) {
                    AudioTrack nextTrack = queue.poll();
                    try {
                        enqueue(nextTrack);
                    } catch (SizeLimitExceededException _) {
                    }
                    player.startTrack(nextTrack.makeClone(), false);
                }
            } else {
                player.startTrack(queue.poll(), false);
            }
        }
    }

    public void enqueue(AudioTrack track) throws SizeLimitExceededException {
        if (!this.player.startTrack(track, true)) {
            boolean isCapableToPut = this.queue.offer(track);

            if (!isCapableToPut) {
                throw new SizeLimitExceededException("Failed to add this track to the queue");
            }
        }
    }

    public void skip() {
        this.onTrackEnd(this.player, this.player.getPlayingTrack(), AudioTrackEndReason.FINISHED);
    }

    public void clear() {
        queue.clear();
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