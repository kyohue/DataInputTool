package kall.util;

import java.io.IOException;

public class ClearConsole {
    public static void clear() {
        try {
            ProcessBuilder pb = new ProcessBuilder("/bin/sh", "-c", "clear");
            pb.environment().put("TERM", "xterm-256color");
            pb.inheritIO().start().waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void clear2() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void clear3() {
        for (int i = 0; i < 40; i++) {
            System.out.println();
        }
    }
}
