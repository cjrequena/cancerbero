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
