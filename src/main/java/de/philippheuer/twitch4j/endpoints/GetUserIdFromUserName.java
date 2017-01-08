package de.philippheuer.twitch4j.endpoints;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import org.asynchttpclient.*;

import de.philippheuer.twitch4j.Twitch4J;
import de.philippheuer.twitch4j.model.*;

import lombok.*;


/**
 * 
 * @requires_scope channel_read
 */
@Data
public class GetUserIdFromUserName extends AbstractTwitchEndpoint {
	
	/**
	 * Get
	 */
	public GetUserIdFromUserName(Twitch4J api, String userName) {
		super(api);
		
		BoundRequestBuilder builder = getApi().getHttpClient().getRequestBuilder(String.format("%s/users/%s", getApi().getTwitchEndpoint(), userName));
		CompletableFuture<Response> promise = 
			builder.execute(new AsyncCompletionHandler<Response>(){
			
		    @Override
		    public Response onCompleted(Response response) throws Exception {
		    	
		    	System.out.println("Result: " + response.getResponseBody());
		    	
		        return response;
		    }

		    @Override
		    public void onThrowable(Throwable t){
		        // Something wrong happened.
		    	System.out.println("Error: " + t.getMessage());
		    }
		}).toCompletableFuture();
		
		promise.join();
	}
	
	
}
