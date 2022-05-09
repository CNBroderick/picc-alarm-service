package ape.alarm.entity.url;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApeAlarmUrlWeightExpressionCalculatorTest {

    @Test
    void calc() {
        assertTrue(new AlarmUrlWeightExpressionCalculator().setExpression("=16").calc(16));
        assertFalse(new AlarmUrlWeightExpressionCalculator().setExpression(">16").calc(16));

    }
}
