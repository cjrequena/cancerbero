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
('admin', '$2a$12$m/YFN4bHk4qQp.v.1sFJb.BQgWbURFOt3CN.PwxBdbdUdnzOlN0Ji', 'admin@example.com'), --user: admin pass: admin
('app_user', '$2a$12$3dPlzcqpi5vs6Tl7rF53gOL9Uz2cvejnkKhYNfHDfOesmz/0ucM3m', 'user@example.com'), --user: app_user pass: app_user
('app_tester', '$2a$12$njskpoqucFPK79/xQlmeBej00feVG1jiEv7wlX6UVarGBpS/pmxC2', 'tester@example.com'); --user: app_tester pass: app_tester

-- Inserting roles
INSERT INTO T_ROLE (role_name, description) VALUES
('admin', 'Administrator role'),
('user', 'Regular user role'),
('tester', 'Tester role');

-- Inserting permissions
INSERT INTO T_PERMISSION (permission_name, description) VALUES
('create_user', 'Permission to create'),
('read_user', 'Permission to read'),
('update_user', 'Permission to update'),
('create_permission', 'Permission to create'),
('read_permission', 'Permission to read'),
('update_permission', 'Permission to update'),
('create_role', 'Permission to create'),
('read_role', 'Permission to read'),
('update_role', 'Permission to update'),
('ROLE_ADMIN', 'Administrator role wildcard'), -- By default, Spring Security expects roles to be prefixed with "ROLE_". For example, if you check for hasRole("ADMIN"), Spring actually looks for an authority ROLE_ADMIN.
('ROLE_USER', 'User role wildcard'), -- By default, Spring Security expects roles to be prefixed with "ROLE_". For example, if you check for hasRole("USER"), Spring actually looks for an authority ROLE_USER.
('ROLE_TESTER', 'Tester role wildcard');

-- Assigning roles to users
INSERT INTO T_USER_ROLE (user_id, role_id) VALUES
(1, 1), -- admin_user gets admin role
(2, 2), -- app_user gets user role
(3, 3); -- app_tester gets tester role

-- Assigning permissions to roles
-- admin permissions
INSERT INTO T_ROLE_PERMISSION (role_id, permission_id) VALUES
(1, 1), -- admin has create_user
(1, 2), -- admin has read_user
(1, 3), -- admin has update_user
(1, 4), -- admin has create_permission
(1, 5), -- admin has read_permission
(1, 6), -- admin has update_permission
(1, 7), -- admin has create_role
(1, 8), -- admin has read_role
(1, 9), -- admin has update_role
(1, 10); -- admin_user has wildcard ROLE_ADMIN


-- user permissions
INSERT INTO T_ROLE_PERMISSION (role_id, permission_id) VALUES
(2, 1), -- user has create_user
(2, 2), -- user has read_user
(2, 3); -- user has update_user

-- tester permissions
INSERT INTO T_ROLE_PERMISSION (role_id, permission_id) VALUES
(3, 2), -- tester has read_user
(3, 5), -- tester has read_permission
(3, 8); -- tester has read_role

