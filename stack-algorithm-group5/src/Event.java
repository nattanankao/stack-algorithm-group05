import java.util.Objects;

public class Event {
    private final Action action;
    private final State previousState;
    private final State nextState;

    public Event(Action action, State previousState, State nextState) {
        this.action = action;
        this.previousState = previousState;
        this.nextState = nextState;
    }

    public Action getAction() { return action; }
    public State getPreviousState() { return previousState; }
    public State getNextState() { return nextState; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Event other)) return false;
        return action == other.action
                && previousState == other.previousState
                && nextState == other.nextState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(action, previousState, nextState);
    }

    @Override
    public String toString() {
        return action + " : " + previousState + " -> " + nextState;
    }
}
