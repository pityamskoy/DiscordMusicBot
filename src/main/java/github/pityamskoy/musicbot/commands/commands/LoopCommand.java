package github.pityamskoy.musicbot.commands.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import github.pityamskoy.musicbot.commands.MusicBotCommand;
import github.pityamskoy.musicbot.commands.lavaplayer.TrackScheduler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;

import static github.pityamskoy.musicbot.Utility.handleIfImpossibleToExecuteMusicCommand;


@SuppressWarnings(value = {"DataFlowIssue"})
public final class LoopCommand implements MusicBotCommand {
    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        try {
            if (!handleIfImpossibleToExecuteMusicCommand(event)) {
                return;
            }

            Long guildId = event.getGuild().getIdLong();
            TrackScheduler trackScheduler = TrackScheduler.getGuildScheduler(guildId);

            if (trackScheduler == null) {
                event.reply("I have not played music on this server").setEphemeral(true).queue();
                return;
            }
            //fix input. Make only two options.
            String type = event.getOption("type").getAsString();

            BlockingQueue<AudioTrack> queue = trackScheduler.getQueue();
            boolean isQueueRepeat = trackScheduler.isQueueRepeat();
            boolean isTrackRepeat = trackScheduler.isTrackRepeat();

            if (queue.isEmpty()) {
                event.reply("I cannot loop the queue or a track because I'm not playing anything").setEphemeral(true).queue();
                return;
            }

            if (type.equals("queue")) {
                if (!isQueueRepeat) {
                    event.reply("Repeating the queue").queue();
                } else {
                    event.reply("Stopped repeating the queue").queue();
                }
                trackScheduler.setQueueRepeat(!isQueueRepeat);
            } else {
                if (!isTrackRepeat) {
                    event.reply("Repeating the current track").queue();
                } else {
                    event.reply("Stopped repeating the current track").queue();
                }
                trackScheduler.setTrackRepeat(!isTrackRepeat);
            }

        } catch (NullPointerException e) {
            event.reply("I'm sorry. A error has been occurred").setEphemeral(true).queue();
        }
    }

    @NotNull
    @Override
    public String getName() {
        return "loop";
    }


    @NotNull
    @Override
    public String getDescription() {
        return "Repeats current track or queue";
    }

    @NotNull
    @Override
    public Optional<Collection<OptionData>> getOptions() {
        OptionData loopType = new OptionData(OptionType.STRING, "type",
                "By default, it repeats current track", false);

        return Optional.of(List.of(loopType));
    }
}
