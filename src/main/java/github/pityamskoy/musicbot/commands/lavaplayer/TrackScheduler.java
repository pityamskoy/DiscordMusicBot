package github.pityamskoy.musicbot.commands.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class TrackScheduler extends AudioEventAdapter {
    private static HashMap<Long, TrackScheduler> schedulers;
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue = new LinkedBlockingQueue<>();
    private AudioTrack[] queueArray;
    byte indexOfCurrentTrackInArray = 0;
    private boolean isTrackRepeat = false;
    private boolean isQueueRepeat = false;

    public TrackScheduler(AudioPlayer player, Long guildId) {
        this.player = player;
        schedulers.put(guildId, this);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (this.isQueueRepeat) {
            player.startTrack(queueArray[indexOfCurrentTrackInArray], false);
            this.indexOfCurrentTrackInArray++;
        }
        if (this.isTrackRepeat) {
            player.startTrack(track.makeClone(), false);
        } else {
            player.startTrack(queue.poll(), false);
        }
    }

    public void queue(AudioTrack track) {
        boolean isCapableToPut = queue.offer(track);
        if(!isCapableToPut) {
            throw new RuntimeException("Failed to add track to queue");
        }
    }

    public void skip() {
        this.queue.poll();
    }

    public void clear() {
        this.queue.clear();
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
        if (repeat) {
            byte i = 0;
            queueArray = new AudioTrack[queue.size()];
            for (AudioTrack track : queue) {
                queueArray[i] = track;
                i++;
            }
        } else {
            this.queueArray = null;
        }
        isQueueRepeat = repeat;
    }
}