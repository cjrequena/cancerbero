-- Create T_USER table
CREATE TABLE T_USER (
    --user_id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    user_name VARCHAR(50) UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE
);

-- Create T_ROLE table
CREATE TABLE T_ROLE (
    role_id INT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(50) UNIQUE,
    description VARCHAR(255),
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create T_PERMISSION table
CREATE TABLE T_PERMISSION (
    permission_id INT PRIMARY KEY AUTO_INCREMENT,
    permission_name VARCHAR(50) UNIQUE,
    description VARCHAR(255),
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create T_USER_ROLE table (junction table for many-to-many relationship between users and roles)
CREATE TABLE T_USER_ROLE (
    user_id INT,
    role_id INT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES T_USER(user_id),
    FOREIGN KEY (role_id) REFERENCES T_ROLE(role_id)
);

-- Create T_ROLE_PERMISSION table (junction table for many-to-many relationship between roles and permissions)
CREATE TABLE T_ROLE_PERMISSION (
    role_id INT,
    permission_id INT,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES T_ROLE(role_id),
    FOREIGN KEY (permission_id) REFERENCES T_PERMISSION(permission_id)
);

-- Inserting users
INSERT INTO T_USER (user_name, password, email) VALUES
('admin', '$2a$12$BvuRpES5el2HOX0pZt.k1Oenr/B7uIexOwwYgWXxGhSjW64J07AMS', 'admin@example.com'), --admin_password
('app_user', '$2a$12$RkvGkDeqeglaAurhWDby/umxhgHJ25VkYH7Xe61UZc6SbvVEJeb3i', 'user@example.com'), --user_password
('app_tester', '$2a$12$yMu.ZOQekXbllapC0l7npeAC3uMqvxhSLsyR1hBguKT2k8XCBJIte', 'tester@example.com'); --tester_password

-- Inserting roles
INSERT INTO T_ROLE (role_name, description) VALUES
('admin', 'Administrator role'),
('user', 'Regular user role'),
('tester', 'Tester role');

-- Inserting permissions
INSERT INTO T_PERMISSION (permission_name, description) VALUES
('create_permission', 'Permission to create'),
('read_permission', 'Permission to read'),
('update_permission', 'Permission to update'),
('ROLE_admin', 'Administrator permission wildcard');

-- Assigning roles to users
INSERT INTO T_USER_ROLE (user_id, role_id) VALUES
(1, 1), -- admin_user gets admin role
(2, 2), -- app_user gets user role
(3, 3); -- app_tester gets tester role

-- Assigning permissions to roles
-- admin permissions
INSERT INTO T_ROLE_PERMISSION (role_id, permission_id) VALUES
(1, 1), -- admin has create_permission
(1, 2), -- admin has read_permission
(1, 3), -- admin has update_permission
(1, 4); -- admin_user has wildcard ROLE_admin permission


-- user permissions
INSERT INTO T_ROLE_PERMISSION (role_id, permission_id) VALUES
(2, 2), -- user has read_permission
(2, 3); -- user has update_permission

-- tester permissions
INSERT INTO T_ROLE_PERMISSION (role_id, permission_id) VALUES
(3, 1), -- tester has create_permission
(3, 2); -- tester has read_permission

