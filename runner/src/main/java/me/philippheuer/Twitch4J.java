package me.philippheuer;

import com.beust.jcommander.JCommander;
import com.jcabi.log.Logger;
import lombok.RequiredArgsConstructor;
import me.philippheuer.runner.Arguments;
import me.philippheuer.twitch4j.Builder;
import me.philippheuer.twitch4j.IClient;
import me.philippheuer.twitch4j.web.DefaultServletAppContext;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
@RequiredArgsConstructor
public class Twitch4J implements ApplicationRunner {

    private Arguments arguments = new Arguments();

    public static void main(String[] argv) {
        Thread.currentThread().setUncaughtExceptionHandler((thread, throwable) -> {
            if (throwable != null) {
                Logger.error(thread.getStackTrace()[0].getClassName(), ExceptionUtils.getStackTrace(throwable));
            }
        });
		ConfigurableApplicationContext context = SpringApplication.run(Twitch4J.class, argv);
        Runtime.getRuntime().addShutdownHook(new Thread(context::close));
    }

    @Override
    public void run(ApplicationArguments args) {
        JCommander.newBuilder()
				.args(args.getSourceArgs())
                .build()
				.addObject(arguments);
    }

    @Bean("twitchClient")
	public IClient client() {
    	return Builder.Client.init()
				.withClientId(arguments.getClientId())
				.withClientSecret(arguments.getClientSecret())
				.withBotAuthorization(
						Builder.Credential.init()
						.withAccessToken(arguments.getBotToken())
						.build()
				)
				.withAppContext(new DefaultServletAppContext(arguments.getPort()))
				.connect();
	}
}
