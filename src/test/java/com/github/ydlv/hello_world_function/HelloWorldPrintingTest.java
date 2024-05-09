package com.github.ydlv.hello_world_function;

import com.github.ydlv.hello_world_string.HelloWorldString;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


public class HelloWorldPrintingTest {

    private ByteArrayOutputStream inspectableOutputStream;

    @BeforeEach
    public void setUp() {
        FileOutputStream regularStdout = new FileOutputStream(FileDescriptor.out);
        inspectableOutputStream = new ByteArrayOutputStream();
        TeeOutputStream tee = new TeeOutputStream(regularStdout, inspectableOutputStream);
        System.setOut(new PrintStream(tee));
    }

    @Test
    public void testPrintHelloWorld() {
        HelloWorldPrinting.printHelloWorld();
        String out = inspectableOutputStream.toString();
        assertTrue(
                out.contains(HelloWorldString.HELLO_WORLD),
                "expected output to contain \"" +
                        HelloWorldString.HELLO_WORLD + "\" but was \"" + out + "\""
        );
    }


    @AfterEach
    public void tearDown() throws IOException {
        inspectableOutputStream.close();
    }

    class TeeOutputStream extends OutputStream {
        private final OutputStream[] streams;

        public TeeOutputStream(OutputStream... streams) {
            this.streams = Arrays.copyOf(streams, streams.length);
        }

        @Override
        public void write(int b) throws java.io.IOException {
            for (OutputStream stream : streams) {
                stream.write(b);
            }
        }
    }

}