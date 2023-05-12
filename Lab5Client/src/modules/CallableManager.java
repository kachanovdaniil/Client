package modules;

import caller.Callable;

import java.util.List;

public class CallableManager {
    private List<Callable> callableList;

    public void callAll() {
        for (Callable callable : callableList) {
            callable.call();
        }
    }

    public void add(Callable callable) {
        callableList.add(callable);
    }

    public void clear() {
        callableList.clear();
    }
}
