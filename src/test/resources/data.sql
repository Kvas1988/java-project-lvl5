INSERT INTO users (ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD) VALUES
    (1, 'John', 'Smith', 'johnsmith@gmail.com', 'password'),
    (2, 'Jack', 'Doe', 'jackdow@gmail.com', 'password'),
    (3, 'Jassica', 'Simpson', 'jassicasimpson@gmail.com', 'password'),
    (4, 'Robert', 'Lock', 'robertlock@gmail.com', 'password');

INSERT INTO tasks (ID, CREATED_AT, DESCRIPTION, NAME, AUTHOR_ID, EXECUTOR_ID, TASK_STATUS_ID) VALUES
    (2, '2022-01-01', 'init description task', 'init task', 1, 22, 1);

INSERT INTO task_status (ID, CREATED_AT, NAME) VALUES
    (2, '2022-01-01', 'test task status');