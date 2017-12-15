package com.arrive.conor.arrive;


import com.arrive.conor.arrive.Silencers.SilencerMathSums;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class MathSumSilencerTest {

    private SilencerMathSums silencerMathSums;
    int num;
    @Before
    public void setUp() {
        silencerMathSums = new SilencerMathSums();
        num = Integer.parseInt(silencerMathSums.randomNumber());
    }

    @Test
    public void validateRandomNumberRange() throws Exception {
        boolean lessThanFifty = num < 51;
        boolean greaterThanZero = num > 0;

        assertThat(lessThanFifty, is(true));
        assertThat(greaterThanZero, is(true));
    }
}
