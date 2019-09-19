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

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.answersWithDelay;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class Bakery_3_YourMixerIsSleepingTest {

    @Mock
    private MixStation mixStationMock;

    @Mock
    private OvenStation ovenStationMock;

    @InjectMocks
    private Bakery_3 unitUnderTest;

    @Test
    @DisplayName("Verify that your mixer can be awoken if he falls asleep at the mixer")
    void yourMixerIsSleeping() {
        Ingredients goodIngredients = new Ingredients("flour+egg+sugar");
        CakeMix cakeMix = new CakeMix("sockerkaka-mix");
        Cake cake = new Cake("sockerkaka");

        Ingredients sleepyIngredients = new Ingredients("sleeping+powder");
        CakeMix sleepyCakeMix = new CakeMix("sleepy-mix");

        when(mixStationMock.mix(eq(goodIngredients))).thenReturn(cakeMix);
        when(mixStationMock.mix(eq(sleepyIngredients))).thenAnswer(answersWithDelay(Long.MAX_VALUE, null));
        when(ovenStationMock.bake(eq(cakeMix))).thenReturn(cake);

        List<Cake> bakedCakes = IntStream.range(0, 40).mapToObj(i -> {
            Ingredients ingredients = i % 2 == 0 ? goodIngredients : sleepyIngredients;
            try {
                Cake bakedCake = unitUnderTest.bakeCake(ingredients);
                System.out.println("Cake was baked...");
                return bakedCake;
            } catch (TimeoutException ignore) {
                System.out.println("Failed to bake cake, perhaps someone is sleeping?");
                return null;
            } catch (Exception ignore) {
                throw new RuntimeException("Generic bakery problem...", ignore);
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());

        verify(mixStationMock, times(20)).mix(eq(goodIngredients));
        verify(mixStationMock, times(20)).mix(eq(sleepyIngredients));
        verify(ovenStationMock, times(20)).bake(eq(cakeMix));
        verify(ovenStationMock, times(0)).bake(eq(sleepyCakeMix));

        assertThat(bakedCakes).hasSize(20);
    }
}
