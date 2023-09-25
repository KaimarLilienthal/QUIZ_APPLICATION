-- Create the 'quiz' table
CREATE TABLE quiz (
                      id SERIAL PRIMARY KEY,
                      topic VARCHAR(255) NOT NULL,
                      difficulty INT NOT NULL
);

-- Create the 'question' table
CREATE TABLE question (
                          id SERIAL PRIMARY KEY,
                          content TEXT NOT NULL,
                          quiz_id INT REFERENCES quiz(id) ON DELETE CASCADE
);

-- Create the 'response' table
CREATE TABLE response (
                          id SERIAL PRIMARY KEY,
                          text TEXT NOT NULL,
                          correct BOOLEAN NOT NULL,
                          question_id INT REFERENCES question(id) ON DELETE CASCADE
);
-- Drop the existing foreign key constraint
ALTER TABLE question
    DROP CONSTRAINT IF EXISTS question_quiz_id_fkey;

-- Recreate the foreign key constraint
ALTER TABLE question
    ADD CONSTRAINT question_quiz_id_fkey
        FOREIGN KEY (quiz_id)
            REFERENCES quiz(id)
            ON DELETE CASCADE;
