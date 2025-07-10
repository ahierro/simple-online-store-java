-- Create test users
INSERT INTO "users" (ID, EMAIL, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, active, locked, authorities, created_at)
VALUES
-- Active user with USER role
('eb815b97-3473-4c24-b770-24ec47e8086d', 'user@example.com', 'testuser',
'$2a$10$KH2HnS9Uyrnjkuup8AMIG.gutPpiel94GigQVzafJHSVKDwHNj7Pm', 'Test', 'User',
true, false, '["ROLE_USER"]', CURRENT_TIMESTAMP),
-- Inactive user awaiting confirmation
('bfceecf0-95e2-4564-8516-2fea75d8fc63', 'inactive@example.com', 'inactiveuser',
'$2a$10$PE5uO/ONX2KF.GMGcj0qpu2Bsq0PyXAA7pin8FocA.ScYcaFQCXWK', 'Inactive', 'User',
false, false, '["ROLE_USER"]', CURRENT_TIMESTAMP),
-- Locked user
('b22004af-e8ad-4e66-ba4d-e9df21fe35eb', 'locked@example.com', 'lockeduser',
'$2a$10$Z4iSvgdCddRRx9JrOYwHyudMq0.ib25m0NxlsBWJAxsYw9zCk0yaK', 'Locked', 'User',
true, true, '["ROLE_USER"]', CURRENT_TIMESTAMP),
-- Admin user
('2fffef3f-cdad-4036-8acf-eeca5b661177', 'admin@example.com', 'adminuser',
'$2a$10$Wv0daiYo1NtEHYAI62JuHO3p6JnxNU2teCt7wvn7ev1UIaX6k8Hf6', 'Admin', 'User',
true, false, '["ROLE_ADMIN"]', CURRENT_TIMESTAMP);

