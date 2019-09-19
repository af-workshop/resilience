package com.af.resilience;

import com.af.resilience.products.Cake;
import com.af.resilience.products.Ingredients;
import com.af.resilience.stations.MixStation;
import com.af.resilience.stations.OvenStation;
import io.github.resilience4j.timelimiter.TimeLimiter;

import javax.inject.Inject;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Bakery_3 {

    @Inject
    private MixStation mixStation;

    @Inject
    private OvenStation ovenStation;

    private final TimeLimiter timeLimiter;

    private final ExecutorService executor = Executors.newFixedThreadPool(1);

    /**
     * Welcome to your third bakery!
     *
     * You have now hired a mixer! Yay. However, you start to notice that your mixer is very
     * sleepy. Your mixer falls asleep every other time he mixes something
     *
     * Make sure to interrupt his sleep, otherwise you might be stuck in eternity. If you find him sleeping, then
     * there is nothing more to do since the ingredients needs to be super-fresh when mixing.
     * Let's hope he does not fall asleep again...
     *
     * Mission: Construct a Time Limiter that should interrupt your mixer if he has failed to mix the
     * ingredients in a reasonable time (typically 100 milliseconds).
     */
    private Bakery_3() {
        timeLimiter = null; // Implement me
    }

    Cake bakeCake(Ingredients ingredients) throws Exception { ;
        return null; // Implement me
    }
}
