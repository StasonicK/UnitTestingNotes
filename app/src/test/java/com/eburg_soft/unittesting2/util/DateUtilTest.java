package com.eburg_soft.unittesting2.util;

import static com.eburg_soft.unittesting2.util.DateUtil.GET_MONTH_ERROR;
import static com.eburg_soft.unittesting2.util.DateUtil.getMonthFromNumber;
import static com.eburg_soft.unittesting2.util.DateUtil.monthNumbers;
import static com.eburg_soft.unittesting2.util.DateUtil.months;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

public class DateUtilTest {

    private static final String today = "05-2019";

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
    public void getMonthFromNumber_returnSuccess(int monthNumber, TestInfo testInfo, TestReporter testReporter) {
        assertEquals(months[monthNumber], DateUtil.getMonthFromNumber(monthNumbers[monthNumber]));
        System.out.println(monthNumbers[monthNumber] + " : " + months[monthNumber]);
    }

    @Test
    public void testGetCurrentTimestamp_returnedTimestamp() {
        assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                assertEquals(today, DateUtil.getCurrentTimeStamp());
                System.out.println("Timestamp is generated correctly");
            }
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
    public void testGetMonthFromNumber_returnError(int monthNumber, TestInfo testInfo, TestReporter testReporter) {
        int randomInt = new Random().nextInt(90) + 13;
        assertEquals(getMonthFromNumber(String.valueOf(monthNumber * randomInt)), GET_MONTH_ERROR);
        System.out.println(monthNumbers[monthNumber] + " : " + GET_MONTH_ERROR);
    }

}













