package springbook.learningtest.junit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = EmptyConfig.class)
public class JUnitTest {

    @Autowired
    private ApplicationContext context;

    private static Set<JUnitTest> testObjects = new HashSet<>();
    private static ApplicationContext contextObject = null;

    @Test
    void test1() throws Exception {
        assertThat(testObjects).isNotIn(this);
        testObjects.add(this);

        assertThat(contextObject == null || contextObject == context).isTrue();
        contextObject = context;
    }

    @Test
    void test2() throws Exception {
        assertThat(testObjects).isNotIn(this);
        testObjects.add(this);

        assertThat(contextObject == null || contextObject == context).isTrue();
        contextObject = context;
    }

    @Test
    void test3() throws Exception {
        assertThat(testObjects).isNotIn(this);
        testObjects.add(this);

        assertThat(contextObject == null || contextObject == context).isTrue();
        contextObject = context;
    }

}
