# Movie Recommendation System

**Course:** CSE337s: Software Testing - Fall 2025   
**Department:** Computer & Systems Engineering, Ain Shams University

## 1. Project Overview
This project implements a Java-based application for generating movie recommendations based on user preferences and genre matching. The system processes input data from text files (`movies.txt` and `users.txt`), enforces strict data validation rules, and produces a `recommendations.txt` output file.

The application is designed with a **"Validation-First" architecture**: strict input validation occurs prior to any processing logic . If any data integrity violation is detected, the system halts execution and reports the specific error message as defined in the requirements specification.

## 2. Project Architecture
The source code follows a standard Maven directory layout with a modular design pattern to facilitate unit testing and separation of concerns:

```text
src/
├── main/java/org/example/
│   ├── model/                  # Domain Entities & Validation Logic
│   │   ├── Movie.java          # Encapsulates Movie data and Regex validation
│   │   └── User.java           # Encapsulates User data and Regex validation
│   ├── io/                     # Input/Output Operations
│   │   └── FileHandler.java    # Handles file parsing and exception throwing
│   ├── service/                # Business Logic
│   │   └── Recommendation.java # Implements the genre-matching algorithm
│   └── Main.java               # Application Entry Point
│
├── test/java/org/example/      # JUnit 5 Test Suite
│   ├── model/
│   │   ├── MovieTest.java      # Unit tests for Movie constraints
│   │   └── UserTest.java       # Unit tests for User constraints
│   ├── io/
│   │   └── FileHandlerTest.java # Integration tests for file parsing
│   └── service/
│       └── RecommendationTest.java # Unit tests for logic
...
```

# 3. Implemented Validation Rules

The following validation constraints have been implemented using Java Regular Expressions (Regex) and logic checks:

## 3.1. Movie Data Validation

- **Title Format:**  
  Validated to ensure every word begins with a capital letter.

- **ID Format:**  
  Validated to ensure it contains all capital letters derived from the title, followed by **exactly three unique digits**.

- **Uniqueness Check:**  
  The system verifies that the numeric portion of the ID contains **distinct digits** (e.g., `123` is valid; `112` is invalid).

## 3.2. User Data Validation

- **Name Format:**  
  Validated to contain only alphabetic characters and spaces. Leading spaces are strictly prohibited.

- **ID Format:**  
  Validated to be exactly **9 alphanumeric characters**. It must:
  - Commence with a digit  
  - May end with **at most one alphabetic character**

---

# 4. Testing Strategy

A comprehensive automated testing suite has been developed using **JUnit 5**

## 4.1. Test Class Breakdown

### **MovieTest — Movie**
- Verifies strict capitalization rules (rejects lowercase titles)  
- Ensures ID prefixes match capital letters from the title  
- Tests boundary conditions for the “3 unique numbers” ID rule  

### **UserTest — User**
- Tests boundary values for User ID length (8 vs 9 chars)  
- Fails invalid name formats (numeric prefixes, special characters)  
- Enforces **no leading space** rule  

### **FileHandlerTest — FileHandler**
Integration test validating:
- Real-world file parsing using temporary files  
- Correct error messages for edge cases: empty files, missing genre lines, malformed IDs  

### **RecommendationTest — Recommendation**
- Validates genre-matching algorithm  
- Excludes movies already watched by the user  
- Handles boundary cases: empty movie list, user watched all movies  

---

# 5. Bug Tracking & Resolution

Some of the critical defects identified during integration testing were documented in Jira and resolved.

## Resolved Defects

### **Validation Bypass (Leading Whitespace)**
- **Issue:** Parser trimmed whitespace, allowing names like `" John"` to pass.  
- **Fix:** Removed automatic trimming to enforce **No leading space**.

### **Silent Failure on Empty Input**
- **Issue:** Empty `movies.txt` resulted in “successful” execution with no output.  
- **Fix:** Added a check to throw:  
  `ERROR: Movie Title is wrong`  
  when the file is empty.

### **Error Message Compliance**
- **Issue:** Error messages were generic (e.g., `"Error in movie id letters"`).  
- **Fix:** Updated to match exact required format, e.g.:  
  `Movie Id letters {id} are wrong`

---

# 6. Usage Instruction

- **Prerequisites:**  
  Java **JDK 17+ (or 8+)** and **Maven**

- **Input Files:**  
  Place `movies.txt` and `users.txt` in the project root

- **Execution:**  
  Run the `Main` class

- **Output:**  
  Review `recommendations.txt` for final recommendations or errors

---

# 7. Contributors

- [Nouran Atef] — ID: **[2200165]**  
- [Fatma Mohamed] — ID: **[2200558]**  
- [Aya Mohamed] — ID: **[2200716]**  
- [Menna Mohamed] — ID: **[2200806]**  
- [Menna Tarik] — ID: **[2200600]**  
- [Aaesha Mahmod] — ID: **[2200510]**
- [Jumana Waleed] — ID: **[2200362]**

