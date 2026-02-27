package routes;

import com.granitebayace.site.Main;

abstract class AbstractRouteTest implements RequestTester {
    protected void startDummyServer() {
        Main.main(new String[0]);
    }
}
