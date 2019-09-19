package com.af.resilience;

//import io.github.resilience4j.circuitbreaker.CircuitBreakerOpenException;
import com.af.resilience.exceptions.RockSolidCakeException;
import com.af.resilience.products.Cake;
import com.af.resilience.products.CakeMix;
import com.af.resilience.products.Ingredients;
import com.af.resilience.stations.MixStation;
import com.af.resilience.stations.OvenStation;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
class Bakery_5_YouNeedToFireYourStaffTest {

    @Mock
    private MixStation mixStationMock;

    @Mock
    private OvenStation ovenStationMock;

    @InjectMocks
    private Bakery_5 unitUnderTest;

    @Test
    @DisplayName("Verify that your bakery can still function even though you probably should fire your staff")
    void youNeedToFireYourStaff() {
        Ingredients goodIngredients = new Ingredients("flour+egg+sugar");
        CakeMix cakeMix = new CakeMix("sockerkaka-mix");
        Cake cake = new Cake("sockerkaka");

        Ingredients badIngredients = new Ingredients("gravel+water+cement");
        CakeMix badCakeMix = new CakeMix("concrete-mix");

        Ingredients sleepyIngredients = new Ingredients("sleeping+powder");
        CakeMix sleepyCakeMix = new CakeMix("sleepy-mix");

        // The mixing
        when(mixStationMock.mix(eq(goodIngredients))).thenReturn(cakeMix);
        when(mixStationMock.mix(eq(badIngredients))).thenReturn(badCakeMix);
        when(mixStationMock.mix(eq(sleepyIngredients))).thenAnswer(answersWithDelay(Long.MAX_VALUE, null));

        when(ovenStationMock.bake(eq(cakeMix))).thenReturn(cake);
        when(ovenStationMock.bake(eq(badCakeMix))).thenThrow(new RockSolidCakeException());


        List<Cake> bakedCakes = IntStream.range(0, 100).mapToObj(i -> {
            Ingredients ingredients;
            if (i % 3 == 0) {
                ingredients = goodIngredients;
            } else if (i % 3 == 1) {
                ingredients = badIngredients;
            } else {
                ingredients = sleepyIngredients;
            }

            try {
                Cake bakedCake = unitUnderTest.bakeCake(ingredients);
                System.out.println("Cake was baked...");
                return bakedCake;
            } catch (CallNotPermittedException ignore) {
                System.out.println("Let's not bake more cakes today!");
                return null;
            } catch (RockSolidCakeException ignore){
                System.out.println("Failed to bake cake. That is not a cake!");
                return null;
            } catch (TimeoutException ignore) {
                System.out.println("Failed to bake cake, perhaps someone is sleeping...?");
                return null;
            } catch (Exception ignore) {
                throw new RuntimeException("Generic bakery problem...", ignore);
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());


        // This will sum up to 40, and there will be >50% failure when reaching it so CB will trip
        verify(mixStationMock, times(14)).mix(eq(goodIngredients));
        verify(mixStationMock, times(13)).mix(eq(badIngredients));
        verify(mixStationMock, times(13)).mix(eq(sleepyIngredients));

        // Same as above, but no baking for the sleepy mix
        verify(ovenStationMock, times(14)).bake(eq(cakeMix));
        verify(ovenStationMock, times(13)).bake(eq(badCakeMix));
        verify(ovenStationMock, times(0)).bake(eq(sleepyCakeMix));

        // 14 eatable cakes delivered!
        assertThat(bakedCakes).hasSize(14);
    }
}
