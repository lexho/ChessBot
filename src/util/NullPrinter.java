package util;

import java.io.OutputStream;
import java.io.PrintStream;

public class NullPrinter {
    public static final PrintStream out = new PrintStream(new OutputStream() {
        public void write(int b) {
        }
    });
}
