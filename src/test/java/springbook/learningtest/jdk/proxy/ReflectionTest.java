package springbook.learningtest.jdk.proxy;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {

    @Test
    void invokeMethod() throws Exception {
        final String name = "Spring";

        assertThat(name.length()).isEqualTo(6);

        final Method lengthMethod = String.class.getMethod("length");
        assertThat(lengthMethod.invoke(name)).isEqualTo(6);

        assertThat(name.charAt(0)).isEqualTo('S');

        final Method charAtMethod = String.class.getMethod("charAt", int.class);
        assertThat(charAtMethod.invoke(name, 0)).isEqualTo('S');
    }

}
