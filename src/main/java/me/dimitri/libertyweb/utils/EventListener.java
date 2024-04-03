package me.dimitri.libertyweb.utils;

import io.micronaut.runtime.server.event.ServerShutdownEvent;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import jakarta.inject.Inject;
import me.dimitri.libertyweb.api.LibertyWeb;

public class EventListener {
    public static LibertyWeb getLibertyWeb() {
        return libertyWeb;
    }

    private static LibertyWeb libertyWeb;

    @Inject
    public EventListener(LibertyWeb libertyWeb) {
        EventListener.libertyWeb = libertyWeb;
    }

    @io.micronaut.runtime.event.annotation.EventListener
    @SuppressWarnings("unused")
    public void onServerShutDownEvent(ServerShutdownEvent event) {
        libertyWeb.getBase().shutdown();
    }


    @io.micronaut.runtime.event.annotation.EventListener
    @SuppressWarnings("unused")
    public void onServerStartupEvent(ServerStartupEvent event) {
        libertyWeb.getBase().startup();
    }
}
