import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class EventStack {
    private final Deque<Event> events = new ArrayDeque<>();
    private final Deque<Event> redo = new ArrayDeque<>();

    public void push(Event event) { events.push(event); }
    public Event pop() { return events.poll(); }
    public Event peek() { return events.peek(); }

    public void pushRedo(Event event) { redo.push(event); }
    public Event popRedo() { return redo.poll(); }

    public void clearRedo() { redo.clear(); }
    public void clear() { events.clear(); redo.clear(); }

    public boolean isEmpty() { return events.isEmpty(); }
    public boolean isRedoEmpty() { return redo.isEmpty(); }
    public int size() { return events.size(); }
    public int redoSize() { return redo.size(); }

    public List<Event> eventsTopToBottom() {
        return new ArrayList<>(events);
    }

    public List<Event> redoTopToBottom() {
        return new ArrayList<>(redo);
    }
}
