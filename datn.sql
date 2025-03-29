CREATE DATABASE demodatn;
GO
USE demodatn;

--1. Bảng roles: Lưu thông tin vai trò
CREATE TABLE roles (
  id INT IDENTITY(1,1) PRIMARY KEY,
  name NVARCHAR(50) NOT NULL UNIQUE -- student, instructor, employee, moderator, admin
);

--2. Bảng students: Lưu thông tin cá nhân của học viên
CREATE TABLE students (
  id INT IDENTITY(1,1) PRIMARY KEY,
  email NVARCHAR(255) UNIQUE NOT NULL,
  password NVARCHAR(255) NOT NULL,
  name NVARCHAR(255) NOT NULL,
  phone NVARCHAR(20),
  avatar NVARCHAR(255),
  created_at DATETIME DEFAULT GETDATE(),
  role_id INT NOT NULL,
  FOREIGN KEY (role_id) REFERENCES roles(id)
);

--3. Bảng instructors: Lưu thông tin giảng viên
CREATE TABLE instructors (
  id INT IDENTITY(1,1) PRIMARY KEY,
  email NVARCHAR(255) UNIQUE NOT NULL,
  password NVARCHAR(255) NOT NULL,
  name NVARCHAR(255) NOT NULL,
  phone NVARCHAR(20),
  avatar NVARCHAR(255),
  bio NVARCHAR(MAX),
  experience INT,
  created_at DATETIME DEFAULT GETDATE(),
  role_id INT NOT NULL,
  FOREIGN KEY (role_id) REFERENCES roles(id)
);

--4. Bảng employees: Lưu thông tin nhân viên
CREATE TABLE employees (
  id INT IDENTITY(1,1) PRIMARY KEY,
  email NVARCHAR(255) UNIQUE NOT NULL,
  password NVARCHAR(255) NOT NULL,
  name NVARCHAR(255) NOT NULL,
  phone NVARCHAR(20),
  department NVARCHAR(255),
  salary DECIMAL(10,2),
  created_at DATETIME DEFAULT GETDATE(),
  role_id INT NOT NULL,
  FOREIGN KEY (role_id) REFERENCES roles(id)
);

--5. Bảng moderators: Lưu thông tin kiểm duyệt viên
CREATE TABLE moderators (
  id INT IDENTITY(1,1) PRIMARY KEY,
  email NVARCHAR(255) UNIQUE NOT NULL,
  password NVARCHAR(255) NOT NULL,
  name NVARCHAR(255) NOT NULL,
  phone NVARCHAR(20),
  specialization NVARCHAR(255),
  created_at DATETIME DEFAULT GETDATE(),
  role_id INT NOT NULL,
  FOREIGN KEY (role_id) REFERENCES roles(id)
);

--6. Bảng admins: Lưu thông tin quản trị viên
CREATE TABLE admins (
  id INT IDENTITY(1,1) PRIMARY KEY,
  email NVARCHAR(255) UNIQUE NOT NULL,
  password NVARCHAR(255) NOT NULL,
  name NVARCHAR(255) NOT NULL,
  phone NVARCHAR(20),
  position NVARCHAR(255),
  created_at DATETIME DEFAULT GETDATE(),
  role_id INT NOT NULL,
  FOREIGN KEY (role_id) REFERENCES roles(id)
);

--7. Bảng category: Danh mục khóa học
CREATE TABLE category (
  id INT IDENTITY(1,1) PRIMARY KEY,
  name NVARCHAR(255) NOT NULL UNIQUE
);

--8. Bảng courses: Thông tin khóa học
CREATE TABLE courses (
  id INT IDENTITY(1,1) PRIMARY KEY,
  title NVARCHAR(255) NOT NULL,
  description NVARCHAR(MAX),
  price DECIMAL(10,2),
  category_id INT,
  instructor_id INT NOT NULL,
  created_at DATETIME DEFAULT GETDATE(),
  FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE SET NULL,
  FOREIGN KEY (instructor_id) REFERENCES instructors(id)
);

--9. Bảng lessons: Bài giảng trong khóa học
CREATE TABLE lessons (
  id INT IDENTITY(1,1) PRIMARY KEY,
  course_id INT NOT NULL,
  title NVARCHAR(255) NOT NULL,
  content NVARCHAR(MAX),
  video_url NVARCHAR(255),
  created_at DATETIME DEFAULT GETDATE(),
  FOREIGN KEY (course_id) REFERENCES courses(id)
);

--10. Bảng enrollment: Đăng ký khóa học của học viên
CREATE TABLE enrollment (
  id INT IDENTITY(1,1) PRIMARY KEY,
  student_id INT NOT NULL,
  course_id INT NOT NULL,
  enrolled_at DATETIME DEFAULT GETDATE(),
  FOREIGN KEY (student_id) REFERENCES students(id),
  FOREIGN KEY (course_id) REFERENCES courses(id)
);

--11. Bảng progress: Tiến độ học tập của học viên
CREATE TABLE progress (
  id INT IDENTITY(1,1) PRIMARY KEY,
  student_id INT NOT NULL,
  lesson_id INT NOT NULL,
  status VARCHAR(20) DEFAULT 'not_started',
  completed_at DATETIME NULL,
  FOREIGN KEY (student_id) REFERENCES students(id),
  FOREIGN KEY (lesson_id) REFERENCES lessons(id)
);

--12. Bảng reviews: Đánh giá khóa học
CREATE TABLE reviews (
  id INT IDENTITY(1,1) PRIMARY KEY,
  student_id INT NOT NULL,
  course_id INT NOT NULL,
  rating INT CHECK (rating BETWEEN 1 AND 5),
  comment NVARCHAR(MAX),
  created_at DATETIME DEFAULT GETDATE(),
  FOREIGN KEY (student_id) REFERENCES students(id),
  FOREIGN KEY (course_id) REFERENCES courses(id)
);

--13. Bảng messages: Tin nhắn giữa người dùng
CREATE TABLE messages (
  id INT IDENTITY(1,1) PRIMARY KEY,
  sender_id INT NOT NULL,
  receiver_id INT NOT NULL,
  content NVARCHAR(MAX) NOT NULL,
  sent_at DATETIME DEFAULT GETDATE(),
  FOREIGN KEY (sender_id) REFERENCES students(id),
  FOREIGN KEY (receiver_id) REFERENCES students(id)
);

--14. Bảng orders: Lưu đơn hàng đã thanh toán
CREATE TABLE orders (
  id INT IDENTITY(1,1) PRIMARY KEY,
  student_id INT NOT NULL,
  total DECIMAL(10,2) NOT NULL,
  created_at DATETIME DEFAULT GETDATE(),
  FOREIGN KEY (student_id) REFERENCES students(id)
);

--15. Bảng payments: Thanh toán khóa học
CREATE TABLE payments (
  id INT IDENTITY(1,1) PRIMARY KEY,
  student_id INT NOT NULL,
  order_id INT NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  payment_method NVARCHAR(50),
  status VARCHAR(20) DEFAULT 'pending',
  paid_at DATETIME NULL,
  FOREIGN KEY (student_id) REFERENCES students(id),
  FOREIGN KEY (order_id) REFERENCES orders(id)
);

--16. Bảng wishlists: Danh sách yêu thích của học viên
CREATE TABLE wishlists (
  id INT IDENTITY(1,1) PRIMARY KEY,
  student_id INT NOT NULL,
  course_id INT NOT NULL,
  created_at DATETIME DEFAULT GETDATE(),
  FOREIGN KEY (student_id) REFERENCES students(id),
  FOREIGN KEY (course_id) REFERENCES courses(id)
);
