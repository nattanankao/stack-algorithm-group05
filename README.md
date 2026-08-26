# Emergency Event Stack

โปรเจกต์ Java สำหรับงานกลุ่ม 5: Emergency Workflow: Event Stack และ State Machine

## ลำดับ Workflow
1. CALL_RECEIVED
2. TEAM_ASSIGNED
3. VEHICLE_DISPATCHED
4. ARRIVED_AT_SCENE
5. CASE_CLOSED

## Algorithm
- Algorithm A: Event Stack
- Algorithm B: Event Stack + State Machine

## วิธีรัน
เปิดโฟลเดอร์ `src` ใน VS Code แล้วรัน `Main.java`

ถ้าใช้ Terminal:

```bash
javac *.java
java Main
```

โค้ดชุดนี้เขียนให้ใช้ syntax ที่เข้ากันได้กับ Java 8 ขึ้นไป เพื่อหลีกเลี่ยงปัญหา syntax ของ Java รุ่นใหม่ใน VS Code

## เมนู
1. เลือก Algorithm
2. เพิ่ม Action
3. Undo
4. Redo
5. ดู Stack / State
6. Reset
7. Test Cases
8. เปรียบเทียบ A กับ B
9. Performance Test
0. Exit

## Test Cases
โปรแกรมมี 10 Test Cases ครอบคลุม workflow ปกติ, action ผิดลำดับ, undo/redo, redo หลายครั้ง, ล้าง redo หลังเพิ่ม action ใหม่, stack ว่าง, ปิด case แล้ว, เปรียบเทียบ A/B และการคืน workflow หลัง undo/redo

## Performance
ทดสอบ n = 100, 1,000, 10,000 และ 50,000 โดยทดลอง 5 รอบ และใช้ System.nanoTime()
