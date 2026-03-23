-- Thêm cột avatar, full_name, phone vào bảng Student
ALTER TABLE `Student` ADD COLUMN `avatar` VARCHAR(255) DEFAULT NULL;
ALTER TABLE `Student` ADD COLUMN `full_name` VARCHAR(100) DEFAULT NULL;
ALTER TABLE `Student` ADD COLUMN `phone` VARCHAR(20) DEFAULT NULL;
