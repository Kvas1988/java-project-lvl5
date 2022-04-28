INSERT INTO users (ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD) VALUES
    (51, 'John', 'Smith', 'johnsmith@gmail.com', 'password'),
    (52, 'Jack', 'Doe', 'jackdow@gmail.com', 'password'),
    (53, 'Jassica', 'Simpson', 'jassicasimpson@gmail.com', 'password'),
    (54, 'Robert', 'Lock', 'robertlock@gmail.com', 'password');

INSERT INTO task_status (ID, CREATED_AT, NAME) VALUES
    (51, '2022-01-01', 'new'),
    (52, '2022-01-01', 'wip'),
    (53, '2022-01-01', 'review'),
    (54, '2022-01-01', 'done');

INSERT INTO labels (ID, NAME, CREATED_AT) VALUES
    (51, 'BUG', '2022-01-01'),
    (52, 'DOC', '2022-01-01'),
    (53, 'ENH', '2022-01-01'),
    (54, 'first good issue', '2022-01-01');

INSERT INTO tasks (ID, CREATED_AT, DESCRIPTION, NAME, AUTHOR_ID, EXECUTOR_ID, TASK_STATUS_ID) VALUES
    (51, '2022-01-01', 'init description task', 'init task', 53, 52, 51),
    (52, '2022-01-02', 'another init task desc', 'another init task', 53, 54, 52);
