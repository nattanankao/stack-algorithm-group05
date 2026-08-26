import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Action[] WORKFLOW = {
        Action.CALL_RECEIVED,
        Action.TEAM_ASSIGNED,
        Action.VEHICLE_DISPATCHED,
        Action.ARRIVED_AT_SCENE,
        Action.CASE_CLOSED
    };

    private static AlgorithmA algorithmA = new AlgorithmA();
    private static AlgorithmB algorithmB = new AlgorithmB();
    private static int selected = 2;

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println(" Emergency Workflow");
        System.out.println(" Event Stack + State Machine");
        System.out.println("========================================");

        while (true) {
            showMenu();
            int choice = readInt("เลือก: ");
            System.out.println();

            switch (choice) {
                case 1 -> selectAlgorithm();
                case 2 -> addAction();
                case 3 -> doUndo();
                case 4 -> doRedo();
                case 5 -> showStatus();
                case 6 -> reset();
                case 7 -> runTests();
                case 8 -> compareAlgorithms();
                case 9 -> performanceTest();
                case 0 -> {
                    System.out.println("จบการทำงาน");
                    return;
                }
                default -> System.out.println("กรุณาเลือก 0 - 9");
            }
        }
    }

    private static void showMenu() {
        System.out.println("\n------------- MENU -------------");
        System.out.println("Algorithm : " + (selected == 1 ? "A - Event Stack" : "B - Event Stack + State Machine"));
        System.out.println("State     : " + currentState());
        System.out.println("1. เลือก Algorithm");
        System.out.println("2. เพิ่ม Action");
        System.out.println("3. Undo");
        System.out.println("4. Redo");
        System.out.println("5. ดู Stack / State");
        System.out.println("6. Reset");
        System.out.println("7. Test Cases");
        System.out.println("8. เปรียบเทียบ A กับ B");
        System.out.println("9. Performance Test");
        System.out.println("0. Exit");
    }

    private static void selectAlgorithm() {
        System.out.println("1. Algorithm A: Event Stack");
        System.out.println("2. Algorithm B: Event Stack + State Machine");
        int n = readInt("เลือก: ");
        if (n != 1 && n != 2) {
            System.out.println("เลือกได้เฉพาะ 1 หรือ 2");
            return;
        }
        selected = n;
        System.out.println("เปลี่ยนเป็น Algorithm " + (selected == 1 ? "A" : "B") + " แล้ว");
    }

    private static void addAction() {
        System.out.println("Action:");
        for (int i = 0; i < WORKFLOW.length; i++) {
            System.out.println((i + 1) + ". " + WORKFLOW[i]);
        }
        int n = readInt("เลือก Action: ");
        if (n < 1 || n > WORKFLOW.length) {
            System.out.println("Action ไม่ถูกต้อง");
            return;
        }

        Action action = WORKFLOW[n - 1];
        boolean ok = selected == 1 ? algorithmA.addAction(action) : algorithmB.addAction(action);
        System.out.println(ok ? "เพิ่ม " + action + " สำเร็จ" : "เพิ่มไม่สำเร็จ: Action ผิดลำดับ");
        System.out.println("State ปัจจุบัน: " + currentState());
    }

    private static void doUndo() {
        boolean ok = selected == 1 ? algorithmA.undo() : algorithmB.undo();
        System.out.println(ok ? "Undo สำเร็จ" : "Undo ไม่ได้: Stack ว่าง");
        System.out.println("State ปัจจุบัน: " + currentState());
    }

    private static void doRedo() {
        boolean ok = selected == 1 ? algorithmA.redo() : algorithmB.redo();
        System.out.println(ok ? "Redo สำเร็จ" : "Redo ไม่ได้: Redo Stack ว่างหรือ State ไม่ถูกต้อง");
        System.out.println("State ปัจจุบัน: " + currentState());
    }

    private static void showStatus() {
        EventStack stack = selected == 1 ? algorithmA.getStack() : algorithmB.getStack();
        System.out.println("Algorithm " + (selected == 1 ? "A" : "B"));
        System.out.println("Current State: " + currentState());
        printList("Event Stack (บน -> ล่าง)", stack.eventsTopToBottom());
        printList("Redo Stack (บน -> ล่าง)", stack.redoTopToBottom());
    }

    private static void printList(String title, List<Event> events) {
        System.out.println(title);
        if (events.isEmpty()) {
            System.out.println("  [ว่าง]");
            return;
        }
        for (Event e : events) System.out.println("  " + e);
    }

    private static void reset() {
        if (selected == 1) algorithmA.reset();
        else algorithmB.reset();
        System.out.println("Reset แล้ว State = NEW");
    }

    private static State currentState() {
        return selected == 1 ? algorithmA.getCurrentState() : algorithmB.getCurrentState();
    }

    private static int readInt(String message) {
        while (true) {
            System.out.print(message);
            if (!SCANNER.hasNextLine()) return 0;
            try {
                return Integer.parseInt(SCANNER.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("กรุณาป้อนตัวเลข");
            }
        }
    }

    // -------------------- Tests --------------------

    private static void runTests() {
        int pass = 0;
        int total = 10;
        pass += test("1. Workflow ปกติ", testNormal());
        pass += test("2. Action ผิดลำดับ", testInvalid());
        pass += test("3. Undo 1 ครั้ง", testUndo());
        pass += test("4. Undo หลายครั้ง", testUndoMany());
        pass += test("5. Redo หลายครั้ง", testRedoMany());
        pass += test("6. เพิ่ม Action ใหม่แล้วล้าง Redo", testNewActionClearsRedo());
        pass += test("7. Undo/Redo ตอน Stack ว่าง", testEmptyStacks());
        pass += test("8. เพิ่ม Action หลัง CLOSED ไม่ได้", testAfterClosed());
        pass += test("9. Algorithm A/B ให้ผลเหมือนกัน", testAandB());
        pass += test("10. Undo แล้ว Redo กลับ Workflow เดิม", testRestore());
        System.out.println("--------------------------------");
        System.out.println("ผล: " + pass + "/" + total + " PASS");
    }

    private static int test(String name, boolean ok) {
        System.out.printf("%-42s %s%n", name, ok ? "PASS" : "FAIL");
        return ok ? 1 : 0;
    }

    private static boolean testNormal() {
        AlgorithmB b = fullB();
        return b.getCurrentState() == State.CLOSED && b.getStack().size() == 5;
    }

    private static boolean testInvalid() {
        AlgorithmB b = new AlgorithmB();
        return !b.addAction(Action.TEAM_ASSIGNED) && b.getCurrentState() == State.NEW && b.getStack().isEmpty();
    }

    private static boolean testUndo() {
        AlgorithmB b = first(3);
        return b.undo() && b.getCurrentState() == State.ASSIGNED && b.getStack().redoSize() == 1;
    }

    private static boolean testUndoMany() {
        AlgorithmB b = first(4);
        b.undo();
        b.undo();
        return b.getCurrentState() == State.ASSIGNED && b.getStack().size() == 2 && b.getStack().redoSize() == 2;
    }

    private static boolean testRedoMany() {
        AlgorithmB b = first(4);
        b.undo();
        b.undo();
        return b.redo() && b.redo() && b.getCurrentState() == State.ON_SCENE && b.getStack().redoSize() == 0;
    }

    private static boolean testNewActionClearsRedo() {
        AlgorithmB b = first(3);
        b.undo();
        return b.addAction(Action.VEHICLE_DISPATCHED)
                && b.getCurrentState() == State.DISPATCHED
                && b.getStack().redoSize() == 0;
    }

    private static boolean testEmptyStacks() {
        AlgorithmB b = new AlgorithmB();
        return !b.undo() && !b.redo() && b.getCurrentState() == State.NEW;
    }

    private static boolean testAfterClosed() {
        AlgorithmB b = fullB();
        return !b.addAction(Action.CALL_RECEIVED) && b.getCurrentState() == State.CLOSED;
    }

    private static boolean testAandB() {
        AlgorithmA a = new AlgorithmA();
        AlgorithmB b = new AlgorithmB();
        for (Action action : WORKFLOW) {
            if (a.addAction(action) != b.addAction(action)) return false;
            if (a.getCurrentState() != b.getCurrentState()) return false;
            if (!a.getStack().eventsTopToBottom().equals(b.getStack().eventsTopToBottom())) return false;
        }
        a.undo(); b.undo();
        a.undo(); b.undo();
        a.redo(); b.redo();
        return a.getCurrentState() == b.getCurrentState()
                && a.getStack().eventsTopToBottom().equals(b.getStack().eventsTopToBottom())
                && a.getStack().redoTopToBottom().equals(b.getStack().redoTopToBottom());
    }

    private static boolean testRestore() {
        AlgorithmB b = fullB();
        b.undo(); b.undo();
        if (b.getCurrentState() != State.DISPATCHED) return false;
        b.redo(); b.redo();
        return b.getCurrentState() == State.CLOSED && b.getStack().size() == 5 && b.getStack().redoSize() == 0;
    }

    private static AlgorithmB first(int count) {
        AlgorithmB b = new AlgorithmB();
        for (int i = 0; i < count; i++) b.addAction(WORKFLOW[i]);
        return b;
    }

    private static AlgorithmB fullB() {
        return first(WORKFLOW.length);
    }

    // -------------------- Comparison --------------------

    private static void compareAlgorithms() {
        AlgorithmA a = new AlgorithmA();
        AlgorithmB b = new AlgorithmB();
        boolean same = true;
        System.out.println("Action                  A State       B State");
        for (Action action : WORKFLOW) {
            boolean ra = a.addAction(action);
            boolean rb = b.addAction(action);
            same &= ra == rb && a.getCurrentState() == b.getCurrentState();
            System.out.printf("%-22s %-12s %-12s%n", action, a.getCurrentState(), b.getCurrentState());
        }
        System.out.println("ผลการเปรียบเทียบ: " + (same ? "PASS" : "FAIL"));
    }

    // -------------------- Performance --------------------

    private static void performanceTest() {
        int[] sizes = {100, 1000, 10000, 50000};
        int rounds = 5;
        System.out.println("n = จำนวน Action รวม และทดลอง 5 รอบ");
        System.out.printf("%-8s %-16s %-16s %-12s %-12s %-12s%n", "n", "A Avg(ns)", "B Avg(ns)", "Push", "Pop", "Compare");

        for (int n : sizes) {
            long timeA = 0, timeB = 0;
            long push = 0, pop = 0, compare = 0;
            for (int r = 0; r < rounds; r++) {
                Result a = measureA(n);
                Result b = measureB(n);
                timeA += a.time;
                timeB += b.time;
                push += a.push;
                pop += a.pop;
                compare += a.compare;
            }
            long divisor = rounds;
            System.out.printf("%-8d %-16d %-16d %-12d %-12d %-12d%n",
                    n, timeA / divisor, timeB / divisor,
                    push / divisor, pop / divisor, compare / divisor);
        }
        System.out.println("เวลาเป็น ns และอาจต่างกันตามเครื่อง/JVM");
    }

    private static Result measureA(int n) {
        AlgorithmA a = new AlgorithmA();
        long push = 0, pop = 0, compare = 0;
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            a.addAction(WORKFLOW[i % WORKFLOW.length]);
            if ((i + 1) % WORKFLOW.length == 0) {
                OperationCounter c = a.getCounter();
                push += c.getPush(); pop += c.getPop(); compare += c.getComparison();
                a.reset();
            }
        }
        long time = System.nanoTime() - start;
        return new Result(time, push, pop, compare);
    }

    private static Result measureB(int n) {
        AlgorithmB b = new AlgorithmB();
        long push = 0, pop = 0, compare = 0;
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            b.addAction(WORKFLOW[i % WORKFLOW.length]);
            if ((i + 1) % WORKFLOW.length == 0) {
                OperationCounter c = b.getCounter();
                push += c.getPush(); pop += c.getPop(); compare += c.getComparison();
                b.reset();
            }
        }
        long time = System.nanoTime() - start;
        return new Result(time, push, pop, compare);
    }

    private static class Result {
        long time, push, pop, compare;
        Result(long time, long push, long pop, long compare) {
            this.time = time; this.push = push; this.pop = pop; this.compare = compare;
        }
    }
}
