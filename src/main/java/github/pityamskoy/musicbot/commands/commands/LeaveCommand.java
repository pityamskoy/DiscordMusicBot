package github.pityamskoy.musicbot.commands.commands;

import github.pityamskoy.musicbot.commands.MusicBotCommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;

import static github.pityamskoy.musicbot.commands.commands.JoinCommand.isMemberConnectedToVoiceChannel;


@SuppressWarnings(value = {"DataFlowIssue"})
public class LeaveCommand implements MusicBotCommand {
    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        try {
            AudioManager audioManager = event.getGuild().getAudioManager();
            GuildVoiceState guildVoiceState = event.getMember().getVoiceState();

            if (!audioManager.isConnected()) {
                event.reply("I'm not connected to any voice channel").setEphemeral(true).queue();
                return;
            }

            if (!isMemberConnectedToVoiceChannel(event)) {
                event.reply("You're not connected to any voice channel").setEphemeral(true).queue();
                return;
            }

            if (audioManager.getConnectedChannel() !=  guildVoiceState.getChannel()) {
                event.reply("We are in different voice channels").setEphemeral(true).queue();
                return;
            }

            String connectedAudioChannelName = event.getMember().getVoiceState().getChannel().getName();

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

    @Nullable
    @Override
    public Optional<Collection<OptionData>> getOptions() {
        return Optional.empty();
    }
}
