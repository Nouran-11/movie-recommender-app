# Movie Recommendation System

**Course:** CSE337s: Software Testing - Fall 2025   
**Department:** Computer & Systems Engineering, Ain Shams University

## 1. Project Overview
This project implements a Java-based application for generating movie recommendations based on user preferences and genre matching. The system processes input data from text files (`movies.txt` and `users.txt`), enforces strict data validation rules, and produces a `recommendations.txt` output file.

The application is designed with a **"Validation-First" architecture**: strict input validation occurs prior to any processing logic. If any data integrity violation is detected, the system halts execution and reports the specific error message as defined in the requirements specification.

## 2. Steps to Clone
To set up this project locally, follow these steps:

1.  **Open your terminal.**
2.  **Navigate to your desired workspace directory.**
    ```bash
    cd /path/to/your/workspace
    ```
3.  **Clone the repository:**
    ```bash
    git clone <repository_url>
    ```
4.  **Navigate into the project directory:**
    ```bash
    cd movie-recommender-app
    ```

## 3. Usage Instructions
### Prerequisites
- **Java JDK 17+** (Ensure `java` and `javac` are in your PATH)
- **Maven** (Optional, for building/testing if using `pom.xml`)

### Running the Application
1.  **Prepare Input Files:**
    Ensure `movies.txt` and `users.txt` are present in the project root directory.
    - **`movies.txt` format:**
      ```text
      Movie Title,ID
      Genre1, Genre2
      ```
    - **`users.txt` format:**
      ```text
      UserName,UserID
      LikedMovieID1, LikedMovieID2
      ```

2.  **Compile the Code:**
    ```bash
    javac -d target/classes src/main/java/org/example/validator/*.java src/main/java/org/example/model/*.java src/main/java/org/example/io/*.java src/main/java/org/example/service/*.java src/main/java/org/example/Main.java
    ```

3.  **Run Main Class:**
    ```bash
    java -cp target/classes org.example.Main
    ```

4.  **Follow Interactive Prompts:**
    The application will ask for file paths. Press **Enter** to use defaults.
    ```text
    Please enter the path to the movies file (press Enter to use default 'movies.txt'):
    Please enter the path to the users file (press Enter to use default 'users.txt'):
    ```

5.  **View Output:**
    Check `recommendations.txt` in the project root for the results or error messages.

## 4. System Design
### Architecture Overview
The system follows a modular **Layered Architecture**:

1.  **Presentation Layer (`Main.java`):**
    - Handles user interaction via console (inputs/outputs).
    - Orchestrates the flow between file handling and recommendation service.

2.  **Input/Output Layer (`io` package):**
    - `FileHandler.java`: Responsible for reading files and converting raw text into domain objects. It strictly coordinates validation.

3.  **Domain Layer (`model` package):**
    - `Movie.java`: POJO representing a movie.
    - `User.java`: POJO representing a user.
    - **Note:** Validation logic is decoupled from these POJOs.

4.  **Validator Layer (`validator` package):**
    - `MovieValidator.java`: Contains static methods for validating movie titles and IDs.
    - `UserValidator.java`: Contains static methods for validating user names and IDs.
    - This separation promotes the **Single Responsibility Principle**.

5.  **Service Layer (`service` package):**
    - `Recommendation.java`: Contains the core business logic algorithm `generateRecommendations` which matches users to movies based on liked genres.

## 5. Implementation Details
### Validation Logic
The application employs strict **Regex-based** validation:
- **Movies:**
  - Titles must Start Capitalized.
  - IDs must match title initials + 3 unique digits (e.g., `Inception` -> `I123`).
- **Users:**
  - Names: Alphabets only, no leading spaces.
  - IDs: 9 Alphanumeric characters, must start with a digit.

### Recommendation Algorithm
1.  Identify genres liked by the user based on their `users.txt` history.
2.  Scan all available movies in `movies.txt`.
3.  Filter movies that match at least one liked genre.
4.  **Exclude** movies the user has already liked/watched.
5.  Format the output as `MovieTitle` in the recommendations file.

## 6. Testing Structure
Tests are refactored to enforce **One Assertion Per Test Method** for clarity and easier debugging.

### Test Classes (`src/test/java`)
- `MovieTest`: Tests `Movie` POJO and `MovieValidator`. Validates title format and ID constraints.
- `UserTest`: Tests `User` POJO and `UserValidator`. Validates name format and ID constraints.
- `RecommendationTest`: Tests the `Recommendation` service logic. 
  - **Happy Path:** Single user, Multiple users.
  - **Edge Cases:** User likes all movies, No users, Empty movie list.

## 7. Contributors
- [Nouran Atef] — ID: **[2200165]**
- [Fatma Mohamed] — ID: **[2200558]**
- [Aya Mohamed] — ID: **[2200716]**
- [Menna Mohamed] — ID: **[2200806]**
- [Menna Tarik] — ID: **[2200600]**
- [Aaesha Mahmod] — ID: **[2200510]**
- [Jumana Waleed] — ID: **[2200362]**
