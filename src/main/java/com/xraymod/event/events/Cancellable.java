package com.xraymod.event.events;

public interface Cancellable {
    boolean isCancelled();
    void setCancelled(boolean state);
}
