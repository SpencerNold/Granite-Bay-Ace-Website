package routes;

import com.granitebayace.site.Main;
import me.spencernold.kwaf.impl.HttpxWebServer;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

abstract class AbstractRouteTest implements RequestTester {

    protected static HttpxWebServer server;
    private static ExecutorService service;

    private static PrintStream stdOut, stdErr;

    protected static void startDummyServer() {
        disableStdPrinting();
        server = (HttpxWebServer) Main.buildServer();
        service = Executors.newSingleThreadExecutor();
        service.execute(server::start);
        try {
            while (!server.running())
                Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        enableStdPrinting();
    }

    protected static void stopDummyServer() {
        disableStdPrinting();
        server.stop();
        service.close();
        enableStdPrinting();
    }

    private static void disableStdPrinting() {
        // Shut up the stdout for noisy startup
        stdOut = System.out;
        stdErr = System.err;
        System.setOut(new PrintStream(OutputStream.nullOutputStream()));
        System.setErr(new PrintStream(OutputStream.nullOutputStream()));
    }

    private static void enableStdPrinting() {
        System.setOut(stdOut);
        System.setErr(stdErr);
    }
}
