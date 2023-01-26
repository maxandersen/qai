import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.fasterxml.jackson.databind.JsonNode;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import java.util.List;
import java.util.Set;

@Path("/v1/completions")
@RegisterRestClient(configKey="completions-api")
public interface OpenAICompletions {

    static record Query(String model, String prompt, int max_tokens) {};

    static record Response(
    String id,
    String object,
    long created,
    String model,
    List<Choice> choices,
    Usage usage
) {
    record Choice(
        String text,
        int index,
        Object logprobs,
        String finish_reason
    ) {}

    record Usage(
        int prompt_tokens,
        int completion_tokens,
        int total_tokens
    ) {}
}

    @POST
    Response getCompletions(Query payload, @HeaderParam("Authorization") String token);
    
}