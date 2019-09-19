package com.af.resilience;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.af.resilience.products.Cake;
import com.af.resilience.products.CakeMix;
import com.af.resilience.products.Ingredients;
import com.af.resilience.stations.MixStation;
import com.af.resilience.stations.OvenStation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class Bakery_1_YourFirstBakeryTest {

    @Mock
    private MixStation mixStationMock;

    @Mock
    private OvenStation ovenStationMock;

    @InjectMocks
    private Bakery_1 unitUnderTest;

    @Test
    @DisplayName("Verify that you actually know how to bake")
    void yourFirstBakery() {
        Ingredients ingredients = new Ingredients("flour+egg+sugar");
        CakeMix cakeMix = new CakeMix("sockerkaka-mix");
        Cake cake = new Cake("sockerkaka");

        when(mixStationMock.mix(eq(ingredients))).thenReturn(cakeMix);
        when(ovenStationMock.bake(eq(cakeMix))).thenReturn(cake);

        Cake bakedCake = unitUnderTest.bakeCake(ingredients);

        assertThat(bakedCake).isEqualTo(cake);

        verify(mixStationMock, times(1)).mix(eq(ingredients));
        verify(ovenStationMock, times(1)).bake(eq(cakeMix));
        System.out.println("Cake was baked...");
    }
}
