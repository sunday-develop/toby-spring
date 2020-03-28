package springbook.user.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toUnmodifiableMap;

public enum Level {
    GOLD(3, null), SILVER(2, GOLD), BASIC(1, SILVER);

    private static final Map<Integer, Level> levels;

    static {
        levels = Arrays.stream(Level.values())
                .collect(toUnmodifiableMap(Level::intValue, Function.identity()));
    }

    private final int value;
    private final Level next;

    Level(int value, Level next) {
        this.value = value;
        this.next = next;
    }

    public int intValue() {
        return value;
    }

    public Optional<Level> nextLevel() {
        return Optional.ofNullable(next);
    }

    public static Level valueOf(int value) {
        final Level level = levels.get(value);
        if (level == null) {
            throw new IllegalArgumentException("Unknown value : [" + value + "]");
        }
        return level;
    }

}
