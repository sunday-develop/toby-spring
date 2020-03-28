package springbook.user.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toUnmodifiableMap;

public enum Level {
    BASIC(1), SILVER(2), GOLD(3);

    private static final Map<Integer, Level> levels;

    static {
        levels = Arrays.stream(Level.values())
                .collect(toUnmodifiableMap(Level::intValue, Function.identity()));
    }

    private final int value;

    Level(int value) {
        this.value = value;
    }

    public int intValue() {
        return value;
    }

    public static Level valueOf(int value) {
        final Level level = levels.get(value);
        if (level == null) {
            throw new IllegalArgumentException("Unknown value : [" + value + "]");
        }
        return level;
    }

}
