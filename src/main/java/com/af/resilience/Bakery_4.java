package com.af.resilience;

import com.af.resilience.products.Cake;
import com.af.resilience.products.CakeMix;
import com.af.resilience.products.Ingredients;
import com.af.resilience.stations.MixStation;
import com.af.resilience.stations.OvenStation;
import io.github.resilience4j.retry.Retry;

import javax.inject.Inject;

import io.github.resilience4j.retry.RetryConfig;

import java.time.Duration;

class Bakery_4 {

    @Inject
    private MixStation mixStation;

    @Inject
    private OvenStation ovenStation;

    private final Retry retry;

    /**
     * Welcome to your fourth bakery!
     *
     * Your baker seems to be burning a lot of cakes! It appears to be some kind of MYSTERIOUS FLAMMABLE CAKE MIX!?
     *
     * If the cake mix ignites in the oven, you need to quickly put the fire out.
     *
     * You cannot waste any cake mix, though. So you need to put the same mix (or what's left of it) into the oven again.
     * The cake mix usually doesn't ignite twice...
     *
     * Oh, one more thing. You must wait at least 100ms for things to cool down before retrying your cake mix.
     *
     * Mission: Construct a Retry that will try to bake the CakeMix again, after 100ms, if it did not work the first time.
     */
    private Bakery_4() {
        retry = null; // Implement me
    }

    Cake bakeCake(Ingredients ingredients) throws Exception {
        return null; // Implement me
    }
}
