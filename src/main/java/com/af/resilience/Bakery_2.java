package com.af.resilience;

import com.af.resilience.products.Cake;
import com.af.resilience.products.Ingredients;
import com.af.resilience.stations.MixStation;
import com.af.resilience.stations.OvenStation;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;

import javax.inject.Inject;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;

class Bakery_2 {

    private final CircuitBreaker circuitBreaker;

    @Inject
    private MixStation mixStation;

    @Inject
    private OvenStation ovenStation;

    /**
     * Welcome to your second bakery!
     *
     * You have now hired a baker! Yay. However, you start to notice that your baker is not very
     * good. Your baker burns half of all the cakes
     *
     * You don't want to waste precious Ingredients so you need to protect yourself.
     *
     * Mission: Construct a Circuit Breaker that should trip/open if your baker has failed to bake 20 cakes
     * out of the last 40 attempts. If the baker is soo bad, it's time to go home for the day.
     */
    private Bakery_2() {
        circuitBreaker = CircuitBreaker.of("Breaker",
                CircuitBreakerConfig
                        .custom()
                        .failureRateThreshold(50f)
                        .ringBufferSizeInClosedState(40)
                        .build())
        ;
    }

    Cake bakeCake(Ingredients ingredients) throws Exception {
        return circuitBreaker.executeCallable(() -> ovenStation.bake(mixStation.mix(ingredients)));
    }
}
