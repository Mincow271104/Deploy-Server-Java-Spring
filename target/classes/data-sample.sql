-- ==================== DỮ LIỆU MẪU CHO CATEGORY ====================
INSERT INTO `Category` (name) VALUES 
    ('Công nghệ thông tin'),
    ('Kinh tế'),
    ('Ngoại ngữ');

-- ==================== DỮ LIỆU MẪU CHO COURSE ====================
INSERT INTO `Course` (name, image, credits, lecturer, category_id) VALUES
    ('Lập trình Java', 'https://upload.wikimedia.org/wikipedia/en/3/30/Java_programming_language_logo.svg', 3, 'Nguyễn Văn A', 1),
    ('Cơ sở dữ liệu', 'https://cdn-icons-png.flaticon.com/512/2906/2906274.png', 4, 'Trần Thị B', 1),
    ('Lập trình Web', 'https://cdn-icons-png.flaticon.com/512/1006/1006363.png', 3, 'Lê Văn C', 1),
    ('Kinh tế vi mô', 'https://cdn-icons-png.flaticon.com/512/3135/3135706.png', 3, 'Phạm Thị D', 2),
    ('Tiếng Anh cơ bản', 'https://cdn-icons-png.flaticon.com/512/3898/3898082.png', 2, 'Hoàng Văn E', 3);
