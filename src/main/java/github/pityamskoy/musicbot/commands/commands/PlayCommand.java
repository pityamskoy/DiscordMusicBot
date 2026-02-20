package github.pityamskoy.musicbot.commands.commands;

import github.pityamskoy.musicbot.commands.MusicBotCommand;
import github.pityamskoy.musicbot.commands.lavaplayer.*;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static github.pityamskoy.musicbot.Utility.isPossibleToExecuteCommandAndReplyIfFalse;
import static github.pityamskoy.musicbot.commands.commands.JoinCommand.connectToVoiceChannel;


@SuppressWarnings(value = {"DataFlowIssue"})
public final class PlayCommand implements MusicBotCommand {
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        try {
            if (!isPossibleToExecuteCommandAndReplyIfFalse(event)) {
                return;
            }

            if (!event.getGuild().getAudioManager().isConnected()) {
                connectToVoiceChannel(event);
            }

            Message.Attachment file = event.getOption("file").getAsAttachment();
            PlayerManager.getInstance().loadAndPlay(file.getUrl(), event.getChannel().asTextChannel());

            event.reply(MessageFormat.format("Playing {0}", file.getFileName())).queue();
        } catch (NullPointerException e) {
            event.reply("I'm sorry. A error has been occurred").setEphemeral(true).queue();
        }
    }

    @NotNull
    @Override
    public String getName() {
        return "play";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Plays music files with .mp3, .wav extensions";
    }

    @NotNull
    @Override
    public Optional<Collection<OptionData>> getOptions() {
        OptionData file = new OptionData(OptionType.ATTACHMENT, "file",
                "Supports only .mp3 / .wav files", true);
        return Optional.of(List.of(file));
    }
}