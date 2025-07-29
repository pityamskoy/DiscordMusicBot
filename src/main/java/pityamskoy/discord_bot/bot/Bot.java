package pityamskoy.discord_bot.bot;

import javax.security.auth.login.LoginException;
import pityamskoy.discord_bot.bot.listeners.EventListener;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import static pityamskoy.discord_bot.bot.utility.Lists.gatewayIntentArrayList;


public class Bot {

    private final Dotenv config;
    private final ShardManager shardManager;

    public Bot() throws LoginException {
        config = Dotenv.configure().ignoreIfMissing().load();
        String token = config.get("TOKEN");
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("music | /help"));
        builder.enableIntents(gatewayIntentArrayList);
        shardManager = builder.build();
        shardManager.addEventListener(new EventListener());
    }

    public Dotenv getconfig() {
        return config;
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    public static void main(String[] arguments) {
        try {
            Bot bot = new Bot();
        } catch (LoginException e) {
            System.out.println("ERROR: Provided bot token is invalid!");
        }
    }
}
