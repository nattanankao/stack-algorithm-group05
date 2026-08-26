import java.util.EnumMap;
import java.util.Map;

public class TransitionTable {
    private final Map<State, Map<Action, State>> table = new EnumMap<>(State.class);

    public TransitionTable() {
        add(State.NEW, Action.CALL_RECEIVED, State.RECEIVED);
        add(State.RECEIVED, Action.TEAM_ASSIGNED, State.ASSIGNED);
        add(State.ASSIGNED, Action.VEHICLE_DISPATCHED, State.DISPATCHED);
        add(State.DISPATCHED, Action.ARRIVED_AT_SCENE, State.ON_SCENE);
        add(State.ON_SCENE, Action.CASE_CLOSED, State.CLOSED);
    }

    private void add(State from, Action action, State to) {
        table.computeIfAbsent(from, k -> new EnumMap<>(Action.class)).put(action, to);
    }

    public State nextState(State current, Action action) {
        Map<Action, State> actions = table.get(current);
        return actions == null ? null : actions.get(action);
    }
}
