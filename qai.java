///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 17+
// Update the Quarkus version to what you want here or run jbang with
// `-Dquarkus.version=<version>` to override it.
//DEPS io.quarkus.platform:quarkus-bom:${quarkus.version:2.15.2.Final}@pom
//DEPS io.quarkus:quarkus-rest-client-reactive-jackson
//DEPS io.quarkus:quarkus-picocli
//JAVAC_OPTIONS -parameters 

//FILES application.properties
//SOURCES *.java

import picocli.CommandLine;

import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import io.quarkus.runtime.annotations.QuarkusMain;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.Quarkus;

@CommandLine.Command
public class qai implements Runnable {

    @Inject
    Logger log;

    @CommandLine.Option(names = { "--token" }, defaultValue = "${OPENAI_TOKEN}", required = true, description = "OpenAI API token")
    String token;

    @CommandLine.Parameters(index = "0", description = "File stacktrace to explain, use `-` for stdin")
    String exception;

    @Inject
    CommandLine.IFactory factory;

    @RestClient 
    OpenAICompletions completions;

    static String seed = """
            Explain possible reasons and solutions to look for given the following stacktrace in a Quarkus Java application.

            """;

    @Override
    public void run() {
        out.println("Asking OpenAI to explain exception found at " + exception);
        String exceptionText = "";

        if("-".equals(exception)) {
            exceptionText = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8))
                                                                            .lines()
                                                                            .collect(Collectors.joining(
                                                                                    System.lineSeparator()));

        } else {
            try {
                exceptionText = Files.readString(Path.of(exception));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.exit(-1);
            }
        }

        log.debug("Exception text: " + exceptionText);

        out.println("Waiting for OpenAI to answer...");
        var result = completions.getCompletions(new OpenAICompletions.Query("text-davinci-003", 
        seed + exceptionText, 
        1000), "Bearer " + token);

        log.debug(result.choices().size() + " choices found");

        for (var choices : result.choices()) {
            out.println(choices.text());
            out.println("-----------------");
        }

        //log.error(result);

    }

}
