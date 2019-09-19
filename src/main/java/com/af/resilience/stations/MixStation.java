package com.af.resilience.stations;

import com.af.resilience.products.CakeMix;
import com.af.resilience.products.Ingredients;

public interface MixStation {

    CakeMix mix(Ingredients ingredients);
}
