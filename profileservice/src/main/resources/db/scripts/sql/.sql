-- File .sql
-- Thực hiện câu lệnh SELECT trước
SELECT 1;

-- Sau đó thực hiện câu lệnh INSERT
INSERT INTO profile (id, email, status, name, role) VALUES
(1, 'dev@gmail.com', 'ACTIVE', 'Le Quang Van', 'DEVELOPER'),
(2, 'customer@gmail.com', 'ACTIVE', 'Khach hang', 'CUSTOMER');