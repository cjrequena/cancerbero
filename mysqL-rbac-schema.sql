-- Multi-Application RBAC schema (Users, Clients, Apps, Roles, Permissions, Groups)
-- Curated and ready for MySQL / MariaDB (ANSI SQL with MySQL specifics)
-- Notes:
--  - Passwords and client secrets MUST be hashed (bcrypt/argon2) before inserting
--  - Adjust data types (UUID, BIGINT) if desired; this uses INT AUTO_INCREMENT for simplicity
--  - Foreign keys use ON DELETE/ON UPDATE policies you can tune for your requirements

-- ==================================================================
-- 1. Application types (normalized list of application kinds)
-- ==================================================================
CREATE TABLE IF NOT EXISTS T_APPLICATION_TYPE (
    app_type_id INT PRIMARY KEY AUTO_INCREMENT,
    type_code VARCHAR(50) NOT NULL UNIQUE, -- e.g. BACKOFFICE, CUSTOMER_MOBILE, CUSTOMER_WEB, B2B_API
    description VARCHAR(255),
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Seed the core types (idempotent)
INSERT INTO T_APPLICATION_TYPE (type_code, description)
SELECT * FROM (SELECT 'BACKOFFICE', 'Web and Mobile apps for internal employees') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM T_APPLICATION_TYPE WHERE type_code = 'BACKOFFICE');

INSERT INTO T_APPLICATION_TYPE (type_code, description)
SELECT * FROM (SELECT 'CUSTOMER_MOBILE', 'Mobile application for customers') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM T_APPLICATION_TYPE WHERE type_code = 'CUSTOMER_MOBILE');

INSERT INTO T_APPLICATION_TYPE (type_code, description)
SELECT * FROM (SELECT 'CUSTOMER_WEB', 'Web application for customers') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM T_APPLICATION_TYPE WHERE type_code = 'CUSTOMER_WEB');

INSERT INTO T_APPLICATION_TYPE (type_code, description)
SELECT * FROM (SELECT 'B2B_API', 'APIs for external third parties') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM T_APPLICATION_TYPE WHERE type_code = 'B2B_API');

-- ==================================================================
-- 2. Applications
-- ==================================================================
CREATE TABLE IF NOT EXISTS T_APPLICATION (
    app_id INT PRIMARY KEY AUTO_INCREMENT,
    app_type_id INT NOT NULL,
    app_key VARCHAR(100) NOT NULL UNIQUE, -- short programmatic identifier
    app_name VARCHAR(150) NOT NULL,
    description VARCHAR(512),
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (app_type_id) REFERENCES T_APPLICATION_TYPE(app_type_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ==================================================================
-- 3. Users (human accounts)
-- ==================================================================
CREATE TABLE IF NOT EXISTS T_USER (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    user_name VARCHAR(50) UNIQUE,
    password VARCHAR(255) NOT NULL, -- hashed
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    user_type ENUM('HUMAN','SYSTEM') DEFAULT 'HUMAN', -- optional
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB;

-- ==================================================================
-- 4. Clients (machine-to-machine for B2B APIs)
-- ==================================================================
CREATE TABLE IF NOT EXISTS T_CLIENT (
    client_id INT PRIMARY KEY AUTO_INCREMENT,
    app_id INT NOT NULL,
    client_key VARCHAR(100) NOT NULL UNIQUE, -- analogous to client_id (public)
    client_secret VARCHAR(255) NOT NULL,     -- hashed secret
    name VARCHAR(150),
    owner_email VARCHAR(255),                 -- contact for the client
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (app_id) REFERENCES T_APPLICATION(app_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ==================================================================
-- 5. Roles (application-scoped)
-- ==================================================================
CREATE TABLE IF NOT EXISTS T_ROLE (
    role_id INT PRIMARY KEY AUTO_INCREMENT,
    app_id INT NOT NULL,
    role_name VARCHAR(100) NOT NULL,
    description VARCHAR(512),
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE (app_id, role_name),
    FOREIGN KEY (app_id) REFERENCES T_APPLICATION(app_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ==================================================================
-- 6. Permissions (application-scoped)
-- ==================================================================
CREATE TABLE IF NOT EXISTS T_PERMISSION (
    permission_id INT PRIMARY KEY AUTO_INCREMENT,
    app_id INT NOT NULL,
    permission_name VARCHAR(150) NOT NULL,
    description VARCHAR(512),
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE (app_id, permission_name),
    FOREIGN KEY (app_id) REFERENCES T_APPLICATION(app_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ==================================================================
-- 7. Role ⇄ Permission mapping (many-to-many; scoped to same app)
-- ==================================================================
CREATE TABLE IF NOT EXISTS T_ROLE_PERMISSION (
    role_id INT NOT NULL,
    permission_id INT NOT NULL,
    app_id INT NOT NULL,
    PRIMARY KEY (role_id, permission_id, app_id),
    FOREIGN KEY (role_id) REFERENCES T_ROLE(role_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES T_PERMISSION(permission_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (app_id) REFERENCES T_APPLICATION(app_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    -- Ensure role and permission belong to the same app logically
    CHECK (app_id IS NOT NULL)
) ENGINE=InnoDB;

-- ==================================================================
-- 8. User ⇄ Role mapping (user can have different roles in different apps)
-- ==================================================================
CREATE TABLE IF NOT EXISTS T_USER_ROLE (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    app_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id, app_id),
    FOREIGN KEY (user_id) REFERENCES T_USER(user_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (role_id) REFERENCES T_ROLE(role_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (app_id) REFERENCES T_APPLICATION(app_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ==================================================================
-- 9. Client ⇄ Role mapping (clients can have roles per app)
-- ==================================================================
CREATE TABLE IF NOT EXISTS T_CLIENT_ROLE (
    client_id INT NOT NULL,
    role_id INT NOT NULL,
    app_id INT NOT NULL,
    PRIMARY KEY (client_id, role_id, app_id),
    FOREIGN KEY (client_id) REFERENCES T_CLIENT(client_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (role_id) REFERENCES T_ROLE(role_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (app_id) REFERENCES T_APPLICATION(app_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ==================================================================
-- 10. Optional: Groups (organizational grouping of users)
--     Groups can be given roles (so membership inherits roles)
-- ==================================================================
CREATE TABLE IF NOT EXISTS T_GROUP (
    group_id INT PRIMARY KEY AUTO_INCREMENT,
    app_id INT, -- optional scoping (you may want global groups or app-specific groups)
    group_name VARCHAR(150) NOT NULL,
    description VARCHAR(512),
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE (app_id, group_name),
    FOREIGN KEY (app_id) REFERENCES T_APPLICATION(app_id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS T_USER_GROUP (
    user_id INT NOT NULL,
    group_id INT NOT NULL,
    PRIMARY KEY (user_id, group_id),
    FOREIGN KEY (user_id) REFERENCES T_USER(user_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (group_id) REFERENCES T_GROUP(group_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS T_GROUP_ROLE (
    group_id INT NOT NULL,
    role_id INT NOT NULL,
    app_id INT NOT NULL,
    PRIMARY KEY (group_id, role_id, app_id),
    FOREIGN KEY (group_id) REFERENCES T_GROUP(group_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (role_id) REFERENCES T_ROLE(role_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (app_id) REFERENCES T_APPLICATION(app_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ==================================================================
-- 11. Optional: Audit / history tables (simple examples)
-- ==================================================================
CREATE TABLE IF NOT EXISTS T_AUDIT (
    audit_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    actor_type ENUM('USER','CLIENT') NOT NULL,
    actor_id INT NOT NULL,
    action VARCHAR(100) NOT NULL,
    resource_type VARCHAR(100),
    resource_id VARCHAR(100),
    details JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ==================================================================
-- 12. Sample seed data: Applications, Roles, Permissions, Users, Clients
-- ==================================================================
-- Applications (example)
INSERT INTO T_APPLICATION (app_type_id, app_key, app_name, description)
SELECT at.app_type_id, 'EMP_PORTAL', 'Employee Portal', 'Backoffice for employees'
FROM T_APPLICATION_TYPE at WHERE at.type_code = 'BACKOFFICE'
AND NOT EXISTS (SELECT 1 FROM T_APPLICATION WHERE app_key = 'EMP_PORTAL');

INSERT INTO T_APPLICATION (app_type_id, app_key, app_name, description)
SELECT at.app_type_id, 'MYAPP_IOS', 'MyApp iOS', 'Customer mobile app (iOS)'
FROM T_APPLICATION_TYPE at WHERE at.type_code = 'CUSTOMER_MOBILE'
AND NOT EXISTS (SELECT 1 FROM T_APPLICATION WHERE app_key = 'MYAPP_IOS');

INSERT INTO T_APPLICATION (app_type_id, app_key, app_name, description)
SELECT at.app_type_id, 'MYAPP_WEB', 'MyApp Web', 'Customer web application'
FROM T_APPLICATION_TYPE at WHERE at.type_code = 'CUSTOMER_WEB'
AND NOT EXISTS (SELECT 1 FROM T_APPLICATION WHERE app_key = 'MYAPP_WEB');

INSERT INTO T_APPLICATION (app_type_id, app_key, app_name, description)
SELECT at.app_type_id, 'PARTNER_API', 'Partner API', 'B2B Partner API'
FROM T_APPLICATION_TYPE at WHERE at.type_code = 'B2B_API'
AND NOT EXISTS (SELECT 1 FROM T_APPLICATION WHERE app_key = 'PARTNER_API');

-- Example roles for PARTNER_API
INSERT INTO T_ROLE (app_id, role_name, description)
SELECT a.app_id, 'API_PARTNER', 'Role for external partner clients'
FROM T_APPLICATION a WHERE a.app_key = 'PARTNER_API'
AND NOT EXISTS (SELECT 1 FROM T_ROLE r WHERE r.app_id = a.app_id AND r.role_name = 'API_PARTNER');

-- Example permissions for PARTNER_API
INSERT INTO T_PERMISSION (app_id, permission_name, description)
SELECT a.app_id, 'read_customer_data', 'Read customer data endpoint'
FROM T_APPLICATION a WHERE a.app_key = 'PARTNER_API'
AND NOT EXISTS (SELECT 1 FROM T_PERMISSION p WHERE p.app_id = a.app_id AND p.permission_name = 'read_customer_data');

INSERT INTO T_PERMISSION (app_id, permission_name, description)
SELECT a.app_id, 'push_order', 'Create orders on behalf of partner'
FROM T_APPLICATION a WHERE a.app_key = 'PARTNER_API'
AND NOT EXISTS (SELECT 1 FROM T_PERMISSION p WHERE p.app_id = a.app_id AND p.permission_name = 'push_order');

-- Link role -> permissions for PARTNER_API
INSERT INTO T_ROLE_PERMISSION (role_id, permission_id, app_id)
SELECT r.role_id, p.permission_id, r.app_id
FROM T_ROLE r
JOIN T_PERMISSION p ON p.app_id = r.app_id
JOIN T_APPLICATION a ON a.app_id = r.app_id
WHERE a.app_key = 'PARTNER_API'
  AND r.role_name = 'API_PARTNER'
  AND p.permission_name IN ('read_customer_data','push_order')
  AND NOT EXISTS (
      SELECT 1 FROM T_ROLE_PERMISSION rp WHERE rp.role_id = r.role_id AND rp.permission_id = p.permission_id AND rp.app_id = r.app_id
  );

-- Example human users (admin + app user)
INSERT INTO T_USER (user_name, password, email, first_name, last_name)
VALUES
('admin', '$2a$12$REPLACE_WITH_BCRYPT_HASHED_PASSWORD', 'admin@example.com','Admin','System')
ON DUPLICATE KEY UPDATE user_name = user_name; -- no-op if exists (adjust as needed)

INSERT INTO T_USER (user_name, password, email, first_name, last_name)
VALUES
('jdoe', '$2a$12$REPLACE_WITH_BCRYPT_HASHED_PASSWORD', 'jdoe@example.com','John','Doe')
ON DUPLICATE KEY UPDATE user_name = user_name;

-- Example client for PARTNER_API
INSERT INTO T_CLIENT (app_id, client_key, client_secret, name, owner_email)
SELECT a.app_id, 'partner123', '$2a$12$REPLACE_WITH_BCRYPT_HASHED_SECRET', 'Partner 123', 'partner@company.com'
FROM T_APPLICATION a WHERE a.app_key = 'PARTNER_API'
AND NOT EXISTS (SELECT 1 FROM T_CLIENT c WHERE c.client_key = 'partner123');

-- Assign client to role API_PARTNER
INSERT INTO T_CLIENT_ROLE (client_id, role_id, app_id)
SELECT c.client_id, r.role_id, r.app_id
FROM T_CLIENT c
JOIN T_APPLICATION a ON a.app_id = c.app_id
JOIN T_ROLE r ON r.app_id = a.app_id AND r.role_name = 'API_PARTNER'
WHERE c.client_key = 'partner123'
  AND NOT EXISTS (
    SELECT 1 FROM T_CLIENT_ROLE cr WHERE cr.client_id = c.client_id AND cr.role_id = r.role_id AND cr.app_id = r.app_id
  );

-- Assign admin user to Employee Portal admin role (example)
-- Ensure role exists for EMP_PORTAL
INSERT INTO T_ROLE (app_id, role_name, description)
SELECT a.app_id, 'EMP_ADMIN', 'Administrator for Employee Portal'
FROM T_APPLICATION a WHERE a.app_key = 'EMP_PORTAL'
AND NOT EXISTS (SELECT 1 FROM T_ROLE r WHERE r.app_id = a.app_id AND r.role_name = 'EMP_ADMIN');

-- Link admin user to role
INSERT INTO T_USER_ROLE (user_id, role_id, app_id)
SELECT u.user_id, r.role_id, r.app_id
FROM T_USER u
JOIN T_ROLE r ON r.role_name = 'EMP_ADMIN'
WHERE u.user_name = 'admin'
  AND NOT EXISTS (
    SELECT 1 FROM T_USER_ROLE ur WHERE ur.user_id = u.user_id AND ur.role_id = r.role_id AND ur.app_id = r.app_id
  );

-- ==================================================================
-- 13. Useful example queries
-- ==================================================================
-- 1) Get all permissions a user has for a given app (direct roles + group roles):
--    Replace :app_key and :user_name with actual values

-- direct user roles + role permissions
-- SELECT DISTINCT p.permission_name
-- FROM T_USER u
-- JOIN T_USER_ROLE ur ON ur.user_id = u.user_id
-- JOIN T_ROLE r ON r.role_id = ur.role_id AND r.app_id = ur.app_id
-- JOIN T_ROLE_PERMISSION rp ON rp.role_id = r.role_id AND rp.app_id = r.app_id
-- JOIN T_PERMISSION p ON p.permission_id = rp.permission_id AND p.app_id = r.app_id
-- JOIN T_APPLICATION a ON a.app_id = r.app_id
-- WHERE u.user_name = :user_name AND a.app_key = :app_key;

-- 2) Include group roles too (user -> group -> group_role -> permissions)
-- (see T_USER_GROUP and T_GROUP_ROLE)

-- 3) Authenticate client by client_key + client_secret (pseudo-code):
-- SELECT client_id, app_id, active FROM T_CLIENT WHERE client_key = :client_key;
-- Then verify hashed secret matches; load roles via T_CLIENT_ROLE -> T_ROLE_PERMISSION

-- ==================================================================
-- End of script
-- ==================================================================

-- ==========================================
-- EERM (Entity-Relationship Representation)
-- Below is a Mermaid diagram to visualize the ER model.
-- You can paste it into any Mermaid renderer (docs, VS Code, mermaid.live)
-- ==========================================

/*
mermaid
erDiagram
    T_APPLICATION_TYPE ||--o{ T_APPLICATION : has
    T_APPLICATION ||--o{ T_ROLE : defines
    T_APPLICATION ||--o{ T_PERMISSION : defines
    T_APPLICATION ||--o{ T_CLIENT : contains
    T_APPLICATION ||--o{ T_GROUP : contains

    T_ROLE ||--o{ T_ROLE_PERMISSION : links
    T_PERMISSION ||--o{ T_ROLE_PERMISSION : links

    T_USER ||--o{ T_USER_ROLE : assigned
    T_ROLE ||--o{ T_USER_ROLE : assigned

    T_CLIENT ||--o{ T_CLIENT_ROLE : assigned
    T_ROLE ||--o{ T_CLIENT_ROLE : assigned

    T_GROUP ||--o{ T_USER_GROUP : membership
    T_USER ||--o{ T_USER_GROUP : membership

    T_GROUP ||--o{ T_GROUP_ROLE : links
    T_ROLE ||--o{ T_GROUP_ROLE : links

    T_ROLE_PERMISSION }|..|{ T_ROLE : contains
    T_ROLE_PERMISSION }|..|{ T_PERMISSION : contains
*/
