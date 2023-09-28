# QUIZ_APPLICATION

## User stories:
- A Quiz is made of N Questions
- Every Question is related to a topic, and has a difficulty rank number
- Every Question has a content and a list of Response
- Every Response has a text and a boolean (correct)
- Questions can have more than 1 Response correct

## DATA LAYER:
- Database with relevant tables (provided by script .sql)
- a DaoQuestion class that will 
	- save Question into database.
	- Update Question with a new Question
	- Delete a Question
	- search Question by topic

## Prerequisites:
- Java (version 17.0.6)
- PostgreSQL (version 42.6.0)
	
## How to Run
1. Make sure you have Java and PostgreSQL installed on your system.
2. Clone the Repository:
   git clone https://github.com/KaimarLilienthal/QUIZ_APPLICATION.git
3. **Database Configuration**:
   Configure the PostgreSQL database settings in the DatabaseConfig class.
   It's necessary to have in table quiz two topics. Run under directory 'database'
   'import.sql'
   **Database Cleanup**:
   If needed, you can delete the database or reset it to its initial state
   under directory 'database' run
   'reset_database.sql'
   'create.sql'
   'import.sql'
   If you running Tests, database will be cleaned automatically.

## Testing:
 Run `DaoQuestionTest` to test the application.
- The database will be automatically cleaned after testing.

## Folder Structure:
- `src`: Contains the Java source code.
- `database`: Contains SQL scripts for database setup and reset.
- 
## Contact:
If you have any questions or feedback, please contact me at [kaimarlilienthal@gmail.com].
   
