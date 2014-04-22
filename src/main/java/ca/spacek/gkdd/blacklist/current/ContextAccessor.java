package ca.spacek.gkdd.blacklist.current;

import android.content.Context;

import com.google.common.base.Optional;

/**
 * Created by temp on 22/04/14.
 */
public class ContextAccessor {
    private Optional<Context> currentContext;

    public Optional<Context> get() {
        return currentContext;
    }

    public void set(Context currentContext) {
        this.currentContext = Optional.of(currentContext);
    }

    public void clear() {
        currentContext = Optional.absent();
    }
}
