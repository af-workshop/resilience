package com.af.resilience;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

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
class Bakery_4_YourBakerIsHotTest {

    @Mock
    private MixStation mixStationMock;

    @Mock
    private OvenStation ovenStationMock;

    @InjectMocks
    private Bakery_4 unitUnderTest;


    private long nextPossibleBakingTime = 0;


    private void setNextPossibleBakingTime(int deltaMilliseconds) {
        nextPossibleBakingTime = System.currentTimeMillis() + deltaMilliseconds;
    }

    @Test
    @DisplayName("Verify that your baker doesnt loose his spirits just because he burns cakes")
    void yourBakerIsHot() {
        Ingredients goodIngredients = new Ingredients("flour+egg+sugar");
        CakeMix cakeMix = new CakeMix("sockerkaka-mix");
        Cake cake = new Cake("sockerkaka");

        Ingredients badIngredients = new Ingredients("gasoline+flour+egg+sugar");
        CakeMix badCakeMix = new CakeMix("sockerkaka-mix-with-a-smell");

        when(mixStationMock.mix(eq(goodIngredients))).thenReturn(cakeMix);
        when(mixStationMock.mix(eq(badIngredients))).thenReturn(badCakeMix);
        when(ovenStationMock.bake(eq(cakeMix))).thenReturn(cake);
        when(ovenStationMock.bake(eq(badCakeMix))).thenAnswer(new Answer<Cake>() {

            private int callNumber = 0;
            @Override
            public Cake answer(InvocationOnMock invocationOnMock) throws Throwable {
                if(System.currentTimeMillis() < nextPossibleBakingTime){
                    throw new RuntimeException("Oh, it burns! It's still too hot!");
                }

                if(++callNumber % 2 == 0) {
                    return cake;
                } else {
                    setNextPossibleBakingTime(99);
                    throw new RuntimeException("Kaboooooom! Do I smell something burning?");
                }
            }
        });


        setNextPossibleBakingTime(-10);


        List<Cake> bakedCakes = IntStream.range(0, 40).mapToObj(i -> {
            Ingredients ingredients = i % 2 == 0 ? goodIngredients : badIngredients;
            try {
                Cake bakedCake = unitUnderTest.bakeCake(ingredients);
                System.out.println("Cake was baked...");
                return bakedCake;

            } catch (Exception ignore) {
                System.out.println(ignore.getMessage());
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());

        verify(mixStationMock, times(20)).mix(eq(goodIngredients));
        verify(mixStationMock, times(20)).mix(eq(badIngredients));
        verify(ovenStationMock, times(20)).bake(eq(cakeMix));
        verify(ovenStationMock, times(40)).bake(eq(badCakeMix));

        assertThat(bakedCakes).hasSize(40);
    }
}