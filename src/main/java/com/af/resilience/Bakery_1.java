package com.af.resilience;

import javax.inject.Inject;

import com.af.resilience.products.Cake;
import com.af.resilience.products.Ingredients;
import com.af.resilience.stations.MixStation;
import com.af.resilience.stations.OvenStation;

class Bakery_1 {

    @Inject
    private MixStation mixStation;

    @Inject
    private OvenStation ovenStation;

    /**
     * Welcome to your first bakery!
     *
     * In order to verify that you in fact can bake, please bake a simple Cake.
     *
     * Mission: Mix the Ingredients into a CakeMix, and make it a Cake in the oven!
     */
    Cake bakeCake(Ingredients ingredients) {
         return null; // Implement me
    }
}
