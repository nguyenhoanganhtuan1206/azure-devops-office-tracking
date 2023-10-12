package com.openwt.officetracking.fake;

import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.IntStream;


@UtilityClass
public class CommonFakes {

    public static int randomInteger(final int toValue) {
        final Random random = new Random();
        return random.nextInt(toValue) + 1;
    }

    public static <T> List<T> buildList(final Supplier<T> supplier) {
        return IntStream.range(1, 5)
                .mapToObj(_ignored -> supplier.get())
                .toList();
    }

    public static Instant randomInstant() {
        return Instant.ofEpochSecond(ThreadLocalRandom.current().nextInt());
    }
}
