package com.xraymod.event.events.callables;

import com.xraymod.event.events.Cancellable;
import com.xraymod.event.events.Event;

public abstract class EventCancellable implements Event, Cancellable {
    private boolean cancelled;

    protected EventCancellable() {
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean state) {
        cancelled = state;
    }
}