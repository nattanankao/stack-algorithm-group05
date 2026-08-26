public class AlgorithmB {
    private final EventStack stack = new EventStack();
    private final TransitionTable table = new TransitionTable();
    private final OperationCounter counter = new OperationCounter();
    private State currentState = State.NEW;

    public boolean addAction(Action action) {
        if (action == null) return false;

        State next = table.nextState(currentState, action);
        counter.comparison();
        if (next == null) return false;

        stack.push(new Event(action, currentState, next));
        counter.push();
        currentState = next;
        stack.clearRedo();
        return true;
    }

    public boolean undo() {
        Event event = stack.pop();
        if (event == null) return false;
        counter.pop();
        stack.pushRedo(event);
        counter.push();
        currentState = event.getPreviousState();
        return true;
    }

    public boolean redo() {
        Event event = stack.popRedo();
        if (event == null) return false;
        counter.pop();

        State next = table.nextState(currentState, event.getAction());
        counter.comparison();
        if (next != event.getNextState()) {
            stack.pushRedo(event);
            counter.push();
            return false;
        }

        stack.push(event);
        counter.push();
        currentState = next;
        return true;
    }

    public State getCurrentState() { return currentState; }
    public EventStack getStack() { return stack; }
    public OperationCounter getCounter() { return counter; }

    public void reset() {
        stack.clear();
        currentState = State.NEW;
        counter.reset();
    }
}
