# Parking project — answers to questions 1–5

These answers concern the **original `parking` package**, before refactoring.


## 1. Actors who can request changes to ParkingGarage

I identify **four actors**, grouping receipts and reports .

| Actor | Reason to request a change | Code affected in ParkingGarage |
| --- | --- | --- |
| Parking management / pricing team | Change hourly rates or introduce a vehicle category. | `fee`. |
| Payments / cashier team | Change payment processing, cancellation or desk refund rules. | `checkout` and `cancel` |
| Accounting / reporting team | Change receipt presentation or the daily report format. | Receipt construction; `dailyReport` |
| IT / storage team | Change the receipt storage destination, such as replacing the file with a database. | Concrete logger creation; logging call. |


## 2. SOLID code review

| Principle | Class | Line(s) of code or elements | Smell |
| --- | --- | --- | --- |
| **SRP — Single Responsibility Principle** | `ParkingGarage` | fee( ... ),checkout( ... ),cancel( ... ), dailyReport(),-logger | One class combines tariff rules, payment coordination, receipt presentation, logging coordination and report formatting. Different actors have reasons to change it. |
| **OCP — Open/Closed Principle** | `ParkingGarage` | fee(Vehicle vehicle, hours :int) | The `switch (vehicle.getType())` fixes the supported tariffs inside the service. Adding BUS requires editing existing pricing code. |
| **LSP — Liskov Substitution Principle** | `CashPayment`; `ParkingGarage` | `CashPayment`; `ParkingGarage` | `CashPayment.refund` throws `UnsupportedOperationException` instead of fulfilling the refund operation exposed by `PaymentMethod`. The client compensates by checking the concrete type with `instanceof`. |
| **ISP — Interface Segregation Principle** | `PaymentMethod`; `CashPayment` | `PaymentMethod`; `CashPayment`| Payment and refund capabilities share one interface. Cash must implement a refund operation it cannot support. A checkout client also depends on an interface containing a refund method it does not use. |
| **DIP — Dependency Inversion Principle** | `ParkingGarage` | -logger :
FileLogger | Business logic constructs and stores a concrete `FileLogger`. There is no injected log abstraction, so the storage detail cannot be replaced independently for testing. |

## 3. Existing files affected by each change request

This table distinguishes required edits to existing production files from new
classes. `Main.java` changes are needed to demonstrate the new feature in the
sample application.

| Request | Existing files to edit | Explanation / new file |
| --- | --- | --- |
| **a. BUS at 15 MAD per hour** | `src/parking/ParkingGarage.java`; `src/parking/Main.java` for the example. | Add `case "BUS" -> hours * 15.0` to `fee`, then create a BUS vehicle in `Main`. `Vehicle` already accepts any type string, so its implementation needs no change; its type comment can be updated. |
| **b. Refundable mobile wallet** | `src/parking/Main.java` for the example. | Add a new `MobileWalletPayment.java` implementing the original `PaymentMethod`, including `name`, `pay` and `refund`. `ParkingGarage` already calls these methods polymorphically. Since the wallet is not a `CashPayment`, cancellation follows the existing `else` branch. No edit to `ParkingGarage` or `PaymentMethod` is required for this particular extension. |
| **c. Database logging instead of file logging** | `src/parking/ParkingGarage.java`, if replacing the logger with a new database logger. | Add a database logger class, then replace the concrete `FileLogger` field and construction at line 9. There is no log interface in the original design. The lab does not specify a database technology, so no SQL or database library is needed to explain this dependency. |
| **d. Daily report in CSV format** | `src/parking/ParkingGarage.java`. | Replace the text generation in `dailyReport`. Since receipts are stored as preformatted strings, `checkout`'s receipt representation may also need changing to produce separate CSV columns reliably. Both methods are in the same file. `Main` can continue printing `dailyReport()` without changes. |

For request **c**, another possible implementation is to rewrite `FileLogger.java`
internally to use a database while retaining its public API. That would leave
`ParkingGarage` unchanged, but the class name and file-oriented constructor would
misrepresent its new responsibility. The table assumes a separate database logger.

