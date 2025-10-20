package github.pityamskoy.musicbot.commands.commands;

import github.pityamskoy.musicbot.commands.MusicBotCommand;
import github.pityamskoy.musicbot.commands.lavaplayer.TrackScheduler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

import static github.pityamskoy.musicbot.Utility.handleIfImpossibleToExecuteMusicCommand;


@SuppressWarnings(value = {"DataFlowIssue"})
public class SkipCommand implements MusicBotCommand {
    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        try {
            if (!handleIfImpossibleToExecuteMusicCommand(event)) {
                return;
            }

            Long guildId = event.getGuild().getIdLong();
            TrackScheduler trackScheduler = TrackScheduler.getGuildScheduler(guildId);

            if (trackScheduler == null) {
                event.reply("I haven't played music on this server").setEphemeral(true).queue();
                return;
            }

            if (trackScheduler.getQueue().isEmpty()) {
                event.reply("I'm not playing anything").setEphemeral(true).queue();
                return;
            }

            trackScheduler.skip();
        } catch (NullPointerException e) {
            event.reply("I'm sorry, a error has been occurred").setEphemeral(true).queue();
        }
    }

    @NotNull
    @Override
    public String getName() {
        return "skip";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Skips current track";
    }

    @NotNull
    @Override
    public Optional<Collection<OptionData>> getOptions() {
        return Optional.empty();
    }
}
