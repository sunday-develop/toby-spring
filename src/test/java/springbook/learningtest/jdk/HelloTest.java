package springbook.learningtest.jdk;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HelloTest {

    @Test
    void simpleProxy() throws Exception {
        final Hello hello = new HelloTarget();

        assertThat(hello.sayHello("Toby")).isEqualTo("Hello Toby");
        assertThat(hello.sayHi("Toby")).isEqualTo("Hi Toby");
        assertThat(hello.sayThankYou("Toby")).isEqualTo("Thank You Toby");
    }

    @Test
    void upperProxy() throws Exception {
        final Hello hello = new HelloUppercase(new HelloTarget());

        assertThat(hello.sayHello("Toby")).isEqualTo("HELLO TOBY");
        assertThat(hello.sayHi("Toby")).isEqualTo("HI TOBY");
        assertThat(hello.sayThankYou("Toby")).isEqualTo("THANK YOU TOBY");
    }

}