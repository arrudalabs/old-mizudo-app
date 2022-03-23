package io.github.arrudalabs.mizudo;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.transaction.Transactional;
import java.util.function.Supplier;

@ApplicationScoped
public class TestSupport {
    @Transactional
    public void execute(Runnable runnable) {
        runnable.run();
    }

    @Transactional
    public <R> R executeAndGet(Supplier<R> supplier) {
        return supplier.get();
    }

}
