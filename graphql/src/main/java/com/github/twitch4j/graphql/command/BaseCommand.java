package com.github.twitch4j.graphql.command;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Error;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CountDownLatch;

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
    private volatile T resultData;

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
    protected abstract ApolloCall<T> getGraphQLCall();

    /**
     * Run Command
     *
     * @return T
     */
    @Override
    protected T run() {
        CountDownLatch latch = new CountDownLatch(1);

        // Run GraphQL Call
        getGraphQLCall().enqueue(new ApolloCall.Callback<T>() {
            @Override
            public void onResponse(@NotNull Response<T> response) {
                List<Error> errors = response.getErrors();
                if (errors != null && errors.size() > 0) {
                    try {
                        throw new RuntimeException("GraphQL API: " + errors);
                    } finally {
                        latch.countDown();
                    }
                }

                resultData = response.getData();
                latch.countDown();
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                try {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            }
        });

        // block until we've got a result
        try {
            latch.await();
        } catch (Exception ex) {
            // nothing
        }

        return resultData;
    }
}
