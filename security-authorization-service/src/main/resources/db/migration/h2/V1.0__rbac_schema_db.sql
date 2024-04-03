-- Create T_USER table
CREATE TABLE T_USER (
    UserID INT PRIMARY KEY AUTO_INCREMENT,
    UserName VARCHAR(50) UNIQUE,
    Password VARCHAR(100),
    Email VARCHAR(100)
);

-- Create T_ROLE table
CREATE TABLE T_ROLE (
    RoleID INT PRIMARY KEY AUTO_INCREMENT,
    RoleName VARCHAR(50) UNIQUE,
    Description VARCHAR(255)
);

-- Create T_PERMISSION table
CREATE TABLE T_PERMISSION (
    PermissionID INT PRIMARY KEY AUTO_INCREMENT,
    PermissionName VARCHAR(50) UNIQUE,
    Description VARCHAR(255)
);

-- Create T_USER_ROLE table (junction table for many-to-many relationship between users and roles)
CREATE TABLE T_USER_ROLE (
    UserID INT,
    RoleID INT,
    PRIMARY KEY (UserID, RoleID),
    FOREIGN KEY (UserID) REFERENCES T_USER(UserID),
    FOREIGN KEY (RoleID) REFERENCES T_ROLE(RoleID)
);

-- Create T_ROLE_PERMISSION table (junction table for many-to-many relationship between roles and permissions)
CREATE TABLE T_ROLE_PERMISSION (
    RoleID INT,
    PermissionID INT,
    PRIMARY KEY (RoleID, PermissionID),
    FOREIGN KEY (RoleID) REFERENCES T_ROLE(RoleID),
    FOREIGN KEY (PermissionID) REFERENCES T_PERMISSION(PermissionID)
);
