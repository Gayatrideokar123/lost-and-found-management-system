CREATE DATABASE IF NOT EXISTS lost_found_db;
USE lost_found_db;
DROP VIEW IF EXISTS available_found_items;
DROP TRIGGER IF EXISTS trg_found_status_history;
DROP PROCEDURE IF EXISTS search_found_items;
SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS notifications,item_status_history,verification,claims,found_items,lost_items,locations,categories,users;
SET FOREIGN_KEY_CHECKS=1;

CREATE TABLE users(user_id INT AUTO_INCREMENT PRIMARY KEY,name VARCHAR(100) NOT NULL,email VARCHAR(120) NOT NULL UNIQUE,phone VARCHAR(20),password VARCHAR(100) NOT NULL,role ENUM('USER','ADMIN') DEFAULT 'USER',created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);
CREATE TABLE categories(category_id INT AUTO_INCREMENT PRIMARY KEY,category_name VARCHAR(60) NOT NULL UNIQUE,description VARCHAR(255));
CREATE TABLE locations(location_id INT AUTO_INCREMENT PRIMARY KEY,location_name VARCHAR(100) NOT NULL UNIQUE,description VARCHAR(255));
CREATE TABLE lost_items(lost_id INT AUTO_INCREMENT PRIMARY KEY,user_id INT NOT NULL,category_id INT NOT NULL,location_id INT NOT NULL,item_name VARCHAR(100) NOT NULL,description TEXT,lost_date DATE NOT NULL,status VARCHAR(30) DEFAULT 'Lost',created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,FOREIGN KEY(user_id) REFERENCES users(user_id),FOREIGN KEY(category_id) REFERENCES categories(category_id),FOREIGN KEY(location_id) REFERENCES locations(location_id));
CREATE TABLE found_items(found_id INT AUTO_INCREMENT PRIMARY KEY,user_id INT NOT NULL,category_id INT NOT NULL,location_id INT NOT NULL,item_name VARCHAR(100) NOT NULL,description TEXT,found_date DATE NOT NULL,status VARCHAR(30) DEFAULT 'Available',created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,FOREIGN KEY(user_id) REFERENCES users(user_id),FOREIGN KEY(category_id) REFERENCES categories(category_id),FOREIGN KEY(location_id) REFERENCES locations(location_id));
CREATE TABLE claims(claim_id INT AUTO_INCREMENT PRIMARY KEY,found_id INT NOT NULL,user_id INT NOT NULL,claim_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,claim_description TEXT NOT NULL,status ENUM('Pending','Approved','Rejected') DEFAULT 'Pending',FOREIGN KEY(found_id) REFERENCES found_items(found_id),FOREIGN KEY(user_id) REFERENCES users(user_id));
CREATE TABLE verification(verification_id INT AUTO_INCREMENT PRIMARY KEY,claim_id INT NOT NULL,admin_id INT NOT NULL,verification_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,verification_status ENUM('Verified','Rejected') NOT NULL,remarks VARCHAR(255),FOREIGN KEY(claim_id) REFERENCES claims(claim_id),FOREIGN KEY(admin_id) REFERENCES users(user_id));
CREATE TABLE notifications(notification_id INT AUTO_INCREMENT PRIMARY KEY,user_id INT NOT NULL,message VARCHAR(500) NOT NULL,notification_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,is_read BOOLEAN DEFAULT FALSE,FOREIGN KEY(user_id) REFERENCES users(user_id));
CREATE TABLE item_status_history(history_id INT AUTO_INCREMENT PRIMARY KEY,found_id INT NOT NULL,old_status VARCHAR(30),new_status VARCHAR(30),changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,FOREIGN KEY(found_id) REFERENCES found_items(found_id));

INSERT INTO users(name,email,phone,password,role) VALUES('Admin','admin@vit.edu','9999999999','admin123','ADMIN'),('Student One','student1@gmail.com','9876543210','1234','USER'),('Student Two','student2@gmail.com','9876543211','1234','USER');
INSERT INTO categories(category_name,description) VALUES('Electronics','Phones and electronic devices'),('Documents','IDs and documents'),('Books','Books and notebooks'),('Accessories','Bags, wallets and keys'),('Clothing','Clothes'),('Others','Other items');
INSERT INTO locations(location_name,description) VALUES('Main Gate','College entrance'),('Library','Library'),('Canteen','Canteen'),('Classroom','Classroom'),('Parking','Parking'),('Computer Lab','Computer laboratory'),('Hostel','Hostel'),('Sports Ground','Sports ground');
INSERT INTO lost_items(user_id,category_id,location_id,item_name,description,lost_date) VALUES(2,1,2,'Black Calculator','Scientific calculator','2026-09-10'),(3,4,3,'Blue Wallet','Small blue wallet','2026-09-11');
INSERT INTO found_items(user_id,category_id,location_id,item_name,description,found_date) VALUES(2,2,2,'College ID Card','ID card','2026-09-12'),(3,4,3,'Black Bag','Backpack','2026-09-12'),(2,1,6,'USB Drive','32GB USB','2026-09-13');

CREATE VIEW available_found_items AS SELECT f.found_id,f.item_name,c.category_name,l.location_name,f.found_date,f.status FROM found_items f JOIN categories c ON f.category_id=c.category_id JOIN locations l ON f.location_id=l.location_id WHERE f.status='Available';

DELIMITER //
CREATE TRIGGER trg_found_status_history AFTER UPDATE ON found_items FOR EACH ROW
BEGIN
 IF OLD.status <> NEW.status THEN INSERT INTO item_status_history(found_id,old_status,new_status) VALUES(NEW.found_id,OLD.status,NEW.status); END IF;
END//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE search_found_items(IN p_keyword VARCHAR(100))
BEGIN
 SELECT f.found_id,f.item_name,c.category_name,l.location_name,f.found_date,f.status
 FROM found_items f JOIN categories c ON f.category_id=c.category_id JOIN locations l ON f.location_id=l.location_id
 WHERE f.item_name LIKE CONCAT('%',p_keyword,'%') OR f.description LIKE CONCAT('%',p_keyword,'%') OR c.category_name LIKE CONCAT('%',p_keyword,'%') OR l.location_name LIKE CONCAT('%',p_keyword,'%');
END//
DELIMITER ;

-- JOIN example
SELECT f.item_name,u.name AS reported_by,c.category_name,l.location_name FROM found_items f JOIN users u ON f.user_id=u.user_id JOIN categories c ON f.category_id=c.category_id JOIN locations l ON f.location_id=l.location_id;
-- SUBQUERY example
SELECT * FROM found_items WHERE category_id IN (SELECT category_id FROM categories WHERE category_name='Electronics');