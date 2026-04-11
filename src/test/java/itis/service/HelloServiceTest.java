package itis.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HelloServiceTest {

    private final HelloService helloService = new HelloService();

    @Test
    void sayHello_returnsFormattedGreeting() {
        String result = helloService.sayHello("World");
        assertEquals("Hello, World", result);
    }

    @Test
    void sayHello_withNull_formatsNull() {
        String result = helloService.sayHello(null);
        assertEquals("Hello, null", result);
    }

    @Test
    void sayHello_withEmptyString_returnsHelloEmpty() {
        String result = helloService.sayHello("");
        assertEquals("Hello, ", result);
    }
}
