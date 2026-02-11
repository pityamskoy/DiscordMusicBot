package github.pityamskoy.musicbot.commands.commands;

import github.pityamskoy.musicbot.commands.MusicBotCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

import static github.pityamskoy.musicbot.Utility.isPossibleToExecuteCommandAndReplyIfFalse;


@SuppressWarnings(value = {"DataFlowIssue"})
public final class LeaveCommand implements MusicBotCommand {
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        try {
            if (!isPossibleToExecuteCommandAndReplyIfFalse(event)) {
                return;
            }

            if (!event.getGuild().getAudioManager().isConnected()) {
                event.reply("I'm not connected to a voice channel").setEphemeral(true).queue();
                return;
            }

            AudioManager audioManager = event.getGuild().getAudioManager();
            String connectedAudioChannelName = event.getMember().getVoiceState().getChannel().getName();

            //add off loop and clear enqueue when leaving
            audioManager.closeAudioConnection();
            event.reply(String.format("Connection to '%s' is successfully closed", connectedAudioChannelName)).queue();
        } catch (NullPointerException e) {
            event.reply("I'm sorry. A error has been occurred").setEphemeral(true).queue();
        }
    }

    @NotNull
    @Override
    public String getName() {
        return "leave";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Leaves a voice channel it is connected to";
    }

    @NotNull
    @Override
    public Optional<Collection<OptionData>> getOptions() {
        return Optional.empty();
    }
}
