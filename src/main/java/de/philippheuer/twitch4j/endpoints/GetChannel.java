package de.philippheuer.twitch4j.endpoints;

import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.Response;

import de.philippheuer.twitch4j.Twitch4J;

import lombok.*;


/**
 * Gets the Channel Info of the currently signed in user
 * @requires_scope channel_read
 */
public class GetChannel extends AbstractTwitchEndpoint {
	
	/**
	 * Get
	 */
	public GetChannel(Twitch4J api) {
		super(api);
		
		BoundRequestBuilder builder = getApi().getHttpClient().getRequestBuilder(String.format("%s/channel", getApi().getTwitchEndpoint()));
		builder.execute(new AsyncCompletionHandler<Response>(){

		    @Override
		    public Response onCompleted(Response response) throws Exception{
		        return response;
		    }

		    @Override
		    public void onThrowable(Throwable t){
		        // Something wrong happened.
		    }
		});
	}
	
	
}
