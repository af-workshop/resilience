package com.af.resilience;

import com.af.resilience.products.Cake;
import com.af.resilience.products.Ingredients;
import com.af.resilience.stations.MixStation;
import com.af.resilience.stations.OvenStation;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiter;

import javax.inject.Inject;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;

class Bakery_5 {

    @Inject
    private MixStation mixStation;

    @Inject
    private OvenStation ovenStation;

    private final TimeLimiter timeLimiter;

    private final CircuitBreaker circuitBreaker;

    private final ExecutorService executor = Executors.newFixedThreadPool(1);


    /**
     * Welcome to your final bakery!
     *
     * I can't believe you haven't fired your staff yet! They are still burning and sleeping on the job. You must really
     * love them.
     *
     * You need to make sure that your bakery can still function even though some sleeping AND burning is taking place.
     *
     * Mission: Construct a Time Limiter AND a Circuit Breaker working in conjunction.
     *
     * You may reuse your previous Time Limiter that should interrupt your mixer if he has failed to mix the ingredients
     * in a reasonable time (typically 100 milliseconds).
     *
     * You may reuse your previous Circuit Breaker that trips/opens if your staff fails to bake 20 cakes out of the last
     * 40 attempts, but REMEMBER that there could be more than one explanation for the failure in this mission!
     */
    private Bakery_5() {
        timeLimiter = null; // Implement me
        circuitBreaker = null; // Implement me
    }

    Cake bakeCake(Ingredients ingredients) throws Exception {
        return null; // Implement me
    }
}
