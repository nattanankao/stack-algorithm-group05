public class AlgorithmA {
    private final EventStack stack = new EventStack();
    private final TransitionTable table = new TransitionTable();
    private final OperationCounter counter = new OperationCounter();

    public boolean addAction(Action action) {
        if (action == null) return false;

        State current = getCurrentState();
        State next = table.nextState(current, action);
        counter.comparison();
        if (next == null) return false;

        stack.push(new Event(action, current, next));
        counter.push();
        stack.clearRedo();
        return true;
    }

    public boolean undo() {
        Event event = stack.pop();
        if (event == null) return false;
        counter.pop();
        stack.pushRedo(event);
        counter.push();
        return true;
    }

    public boolean redo() {
        Event event = stack.popRedo();
        if (event == null) return false;
        counter.pop();

        State current = getCurrentState();
        State next = table.nextState(current, event.getAction());
        counter.comparison();
        if (next != event.getNextState()) {
            stack.pushRedo(event);
            counter.push();
            return false;
        }

        stack.push(event);
        counter.push();
        return true;
    }

    public State getCurrentState() {
        Event top = stack.peek();
        return top == null ? State.NEW : top.getNextState();
    }

    public EventStack getStack() { return stack; }
    public OperationCounter getCounter() { return counter; }

    public void reset() {
        stack.clear();
        counter.reset();
    }
}
