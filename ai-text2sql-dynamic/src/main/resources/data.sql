INSERT INTO TBL_USER (id, username, email, password)
VALUES
  (1, 'user1', 'user1@example.com', 'password1'),
  (2, 'user2', 'user2@example.com', 'password2'),
  (3, 'user3', 'user3@example.com', 'password3'),
  (4, 'user4', 'user4@example.com', 'password4'),
  (5, 'user5', 'user5@example.com', 'password5'),
  (6, 'user6', 'user6@example.com', 'password6'),
  (7, 'user7', 'user7@example.com', 'password7'),
  (8, 'user8', 'user8@example.com', 'password8'),
  (9, 'user9', 'user9@example.com', 'password9'),
  (10, 'user10', 'user10@example.com', 'password10');

INSERT INTO TBL_ACCOUNT (id, accountNumber, user_id, balance, openDate)
VALUES
  (1, 'ACC001', 1, 1000.00, '2024-07-09'),
  (2, 'ACC002', 1, 500.00, '2024-07-10'),
  (3, 'ACC003', 2, 1500.00, '2024-07-09'),
  (4, 'ACC004', 2, 200.00, '2024-07-10'),
  (5, 'ACC005', 3, 800.00, '2024-07-09'),
  (6, 'ACC006', 4, 3000.00, '2024-07-09'),
  (7, 'ACC007', 4, 100.00, '2024-07-10'),
  (8, 'ACC008', 5, 250.00, '2024-07-09'),
  (9, 'ACC009', 6, 1800.00, '2024-07-09'),
  (10, 'ACC010', 6, 700.00, '2024-07-10'),
  (11, 'ACC011', 7, 500.00, '2024-07-09'),
  (12, 'ACC012', 8, 1200.00, '2024-07-09'),
  (13, 'ACC013', 9, 900.00, '2024-07-09'),
  (14, 'ACC014', 9, 300.00, '2024-07-10'),
  (15, 'ACC015', 10, 2000.00, '2024-07-09');