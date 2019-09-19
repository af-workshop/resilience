package com.af.resilience.stations;

import com.af.resilience.products.Cake;
import com.af.resilience.products.CakeMix;

public interface OvenStation {

    Cake bake(CakeMix mix);
}
