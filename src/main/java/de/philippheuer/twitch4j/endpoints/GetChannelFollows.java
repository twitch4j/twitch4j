package de.philippheuer.twitch4j.endpoints;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import org.asynchttpclient.*;

import de.philippheuer.twitch4j.Twitch4J;
import de.philippheuer.twitch4j.model.*;

import lombok.*;


/**
 * 
 * @requires_scope none
 */
@Data
public class GetChannelFollows extends AbstractTwitchEndpoint {
	
	/**
	 * Get
	 */
	public GetChannelFollows(Twitch4J api, String channelId) {
		super(api);
		
		System.out.println("Preparing Request");
		
		BoundRequestBuilder builder = getApi().getHttpClient().getRequestBuilder(String.format("%s/channels/%s/follows", getApi().getTwitchEndpoint(), channelId));
		/*CompletableFuture<Response> promise = */
		builder.execute(new AsyncCompletionHandler<Response>(){
			
		    @Override
		    public Response onCompleted(Response response) throws Exception {
		    	//Channel value = objectMapper.readValue(response.getResponseBody(), Channel.class);
		    	
		    	System.out.println("Result: " + response.getResponseBody());
		    	//System.out.println("Result: " + value.toString());
		    	
		        return response;
		    }

		    @Override
		    public void onThrowable(Throwable t){
		        // Something wrong happened.
		    	System.out.println("Error: " + t.getMessage());
		    }
		});//.toCompletableFuture();
		
		// wait for completion
		// @TODO
		//promise.join();
	}
	
	
}
