package com.af.resilience;

import com.af.resilience.exceptions.RockSolidCakeException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class Bakery_2_YourBakerIsBadTest {

    @Mock
    private MixStation mixStationMock;

    @Mock
    private OvenStation ovenStationMock;

    @InjectMocks
    private Bakery_2 unitUnderTest;

    @Test
    @DisplayName("Verify that your bakery doesn't crumble due to your asshat baker")
    void yourBakerIsBad() {
        Ingredients goodIngredients = new Ingredients("flour+egg+sugar");
        CakeMix cakeMix = new CakeMix("sockerkaka-mix");
        Cake cake = new Cake("sockerkaka");

        Ingredients badIngredients = new Ingredients("gravel+water+cement");
        CakeMix badCakeMix = new CakeMix("concrete-mix");

        when(mixStationMock.mix(eq(goodIngredients))).thenReturn(cakeMix);
        when(mixStationMock.mix(eq(badIngredients))).thenReturn(badCakeMix);
        when(ovenStationMock.bake(eq(cakeMix))).thenReturn(cake);
        when(ovenStationMock.bake(eq(badCakeMix))).thenThrow(new RockSolidCakeException());

        List<Cake> bakedCakes = IntStream.range(0, 100).mapToObj(i -> {

            Ingredients ingredients = i % 2 == 0 ? goodIngredients : badIngredients;

            try {
                Cake bakedCake = unitUnderTest.bakeCake(ingredients);
                System.out.println("Cake was baked...");
                return bakedCake;
            } catch (CallNotPermittedException ignore) {
                System.out.println("Let's not bake more cakes today!");
                return null;
            } catch (RockSolidCakeException ignore) {
                System.out.println("Failed to bake cake. That is not a cake!");
                return null;
            } catch (Exception ignore) {
                throw new RuntimeException("Generic bakery problem...", ignore);
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());

        verify(mixStationMock, times(20)).mix(eq(goodIngredients));
        verify(mixStationMock, times(20)).mix(eq(badIngredients));
        verify(ovenStationMock, times(20)).bake(eq(cakeMix));
        verify(ovenStationMock, times(20)).bake(eq(badCakeMix));

        assertThat(bakedCakes).hasSize(20);
    }
}
