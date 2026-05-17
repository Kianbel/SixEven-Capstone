# T(n)-spector

## Group Members
- Alonte, John Prince L.
- Belciña, Kirk Ian F.
- De la Cerna, Josh Gabriel E.
- Miscreola, Hertz Lenin C.
- Ondon, Veneerick Joel M.

## Project Description
T(n)-spector is an easy-to-use desktop application designed to help users, especially students, learn and analyze the efficiency of algorithms through visual programming. Instead of typing raw code, users use a block-based programming approach to assemble the algorithm in modular form. The app can then inspect and analyze the assembled algorithm and calculate its theoretical runtime. Users can also save, share, and view the code snippets they and other people have made to a centralized code repository built within the app.

## Features
- **Block-Based Programming Approach:** Build algorithms visually using intuitive, drag-and-drop functional blocks (variable definitions, loops, assignments, operators, etc.).
- **Theoretical Running Time Analyzer:** Automatically compute the theoretical running time of the assembled algorithm.
- **Centralized Code Snippet Repository:** Save your analyzed algorithms to a centralized repository along with useful information such as the algorithm's creator, programming language used, date of creation, and the calculated theoretical running time.

## Planned Technologies
- Java
- JavaFX
- JDBC
- MySQL

## Evaluation Criteria Mapping

### 1. Object-Oriented Programming (OOP)
The core architecture of the application relies strictly on OOP principles to represent program logic as data structures:
- **Inheritance, Polymorphism, & Abstraction:** An abstract `Statement` class serves as the foundation for multi-line blocks (`MultiLineStatement`) and single-line expressions (`SingleLineStatement`). Which further brances to `Loop`, `Declaration`, and `Assignment` classes. Additionally, the `Operator` class branches into `AssignmentOperator`, `ArithmeticOperator`, `LogicalOperator`, and `IndexOperator`.

- **Encapsulation:** The `CodeSnippet` class encapsulates relational database records—packaging metadata (`codeid`, `title`, `runtime`, `language`, `datecreated`, `userid`) into self-contained data models. The `User` class also does the same thing housing a user's data (`uid`, `username`, `firstname`, `lastname`).

### 2. Graphical User Interface (GUI)
- Built entirely using **JavaFX** along with **FXML** layouts to separate presentation design from logic.
- Implements interactive, draggable custom visual components to mimic a block-based programming workspace alongside a dynamic data-table view for the code snippet repository.

### 3. Unified Modeling Language (UML)
- **Class Diagram:** Full structural mapping is established, representing the relationships between operators (`ArithmeticOperator`, `LogicalOperator`), operand expressions (`Binary`, `Unary`, `Variable`, `Value`), structural statements (`Loop`, `Assignment`), and user authentication profiles (`User`, `CodeSnippet`).
- **Use-Case Diagram:** Defines primary actor `User` capabilities such as account register and login, algorithm assembly through block-based approach, calculation of theoretical runtime, and saving and viewing the code repository. It also defines the `Database` actor as being responsible for uploading the code details to an external repository.

### 4. Design Patterns
- **Singleton Pattern:** Applied to the database access layer (`MySQLConnection`). The application ensures that only a single, globally accessible instance of the database driver and initial connection payload exists at any time. This reduces resource consumption by channeling all queries through a single connection pipeline.