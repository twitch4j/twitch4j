package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.jetbrains.annotations.NotNull;

/**
 * GraphQL Base HystrixCommand
 */
public abstract class BaseCommand<T extends Operation.Data> extends HystrixCommand<T> {

    /**
     * Holds the apolloClient
     */
    protected final ApolloClient apolloClient;

    /**
     * Holds the result
     */
    private T resultData;

    /**
     * Constructor
     *
     * @param apolloClient Apollo Client
     */
    public BaseCommand(ApolloClient apolloClient) {
        super(HystrixCommandGroupKey.Factory.asKey("GraphQL"));
        this.apolloClient = apolloClient;
    }

    /**
     * Abstract GraphQL Call
     *
     * @return ApolloCall
     */
    protected abstract ApolloCall getGraphQLCall();

    /**
     * Run Command
     *
     * @return T
     */
    @Override
    protected T run() {
        // Run GraphQL Call
        getGraphQLCall().enqueue(new ApolloCall.Callback<T>() {
            @Override
            public void onResponse(@NotNull Response<T> response) {
                if (response.errors().size() > 0) {
                    throw new RuntimeException("GraphQL API: " + response.errors().toString());
                }

                resultData = response.data();
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                throw new RuntimeException(e);
            }
        });

        // block until we've got a result
        block();
        return resultData;
    }

    /**
     * Block until we get a result
     */
    private void block() {
        do {
            if (resultData != null) {
                return;
            }

            try {
                Thread.sleep(100);
            } catch (Exception ex) {
                // nothing
            }
        } while (true);
    }
}
