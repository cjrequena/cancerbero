-- ===========================================================
-- PostgreSQL RBAC Model for Multi-Application Architecture
-- Supports: Backoffice, WebApp, MobileApp, B2B API
-- Author: ChatGPT (GPT-5)
-- ===========================================================

-- =======================
-- 0. EXTENSIONS
-- =======================
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- =======================
-- 1. APPLICATION TYPE
-- =======================
CREATE TABLE T_APPLICATION_TYPE (
    app_type_id SERIAL PRIMARY KEY,
    type_code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Preload common application types
INSERT INTO T_APPLICATION_TYPE (type_code, description) VALUES
('BACKOFFICE', 'Internal backoffice system (Web or Mobile)'),
('MOBILE_APP', 'Mobile application for customers'),
('WEB_APP', 'Web application for customers'),
('B2B_API', 'API for external third parties');

-- =======================
-- 2. APPLICATION
-- =======================
CREATE TABLE T_APPLICATION (
    app_id SERIAL PRIMARY KEY,
    app_type_id INT NOT NULL REFERENCES T_APPLICATION_TYPE(app_type_id),
    app_name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE
);

-- =======================
-- 3. USER (Human users)
-- =======================
CREATE TABLE T_USER (
    user_id SERIAL PRIMARY KEY,
    user_name VARCHAR(50) UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE
);

-- =======================
-- 4. CLIENT (B2B or system-to-system users)
-- =======================
CREATE TABLE T_CLIENT (
    client_id SERIAL PRIMARY KEY,
    app_id INT NOT NULL REFERENCES T_APPLICATION(app_id) ON DELETE CASCADE,
    client_key VARCHAR(100) NOT NULL UNIQUE,  -- like OAuth2 client_id
    client_secret VARCHAR(255) NOT NULL,      -- securely hashed (e.g. bcrypt)
    name VARCHAR(100),
    description VARCHAR(255),
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE
);

-- =======================
-- 5. ROLE
-- =======================
CREATE TABLE T_ROLE (
    role_id SERIAL PRIMARY KEY,
    app_id INT NOT NULL REFERENCES T_APPLICATION(app_id) ON DELETE CASCADE,
    role_name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (app_id, role_name)
);

-- =======================
-- 6. PERMISSION
-- =======================
CREATE TABLE T_PERMISSION (
    permission_id SERIAL PRIMARY KEY,
    app_id INT NOT NULL REFERENCES T_APPLICATION(app_id) ON DELETE CASCADE,
    permission_name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (app_id, permission_name)
);

-- =======================
-- 7. RELATIONSHIPS (Users ↔ Roles, Clients ↔ Roles, Roles ↔ Permissions)
-- =======================

-- Many-to-many between USER and ROLE
CREATE TABLE T_USER_ROLE (
    user_id INT NOT NULL REFERENCES T_USER(user_id) ON DELETE CASCADE,
    role_id INT NOT NULL REFERENCES T_ROLE(role_id) ON DELETE CASCADE,
    app_id INT NOT NULL REFERENCES T_APPLICATION(app_id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id, app_id)
);

-- Many-to-many between CLIENT and ROLE
CREATE TABLE T_CLIENT_ROLE (
    client_id INT NOT NULL REFERENCES T_CLIENT(client_id) ON DELETE CASCADE,
    role_id INT NOT NULL REFERENCES T_ROLE(role_id) ON DELETE CASCADE,
    app_id INT NOT NULL REFERENCES T_APPLICATION(app_id) ON DELETE CASCADE,
    PRIMARY KEY (client_id, role_id, app_id)
);

-- Many-to-many between ROLE and PERMISSION
CREATE TABLE T_ROLE_PERMISSION (
    role_id INT NOT NULL REFERENCES T_ROLE(role_id) ON DELETE CASCADE,
    permission_id INT NOT NULL REFERENCES T_PERMISSION(permission_id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

-- =======================
-- 8. AUDIT TABLE (Optional)
-- =======================
CREATE TABLE T_AUDIT_LOG (
    audit_id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    entity_name VARCHAR(100) NOT NULL,
    entity_id VARCHAR(100),
    action VARCHAR(50) NOT NULL, -- INSERT, UPDATE, DELETE, LOGIN, etc.
    performed_by VARCHAR(100),
    performed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    details JSONB
);

-- =======================
-- 9. SAMPLE DATA
-- =======================

-- Applications
INSERT INTO T_APPLICATION (app_type_id, app_name, description)
VALUES
(1, 'Backoffice Admin', 'Internal management webapp'),
(2, 'Mobile Banking', 'Customer mobile application'),
(3, 'Customer Portal', 'Customer web application'),
(4, 'Partner API', 'External API for partners');

-- Users
INSERT INTO T_USER (user_name, password, email) VALUES
('admin', '$2a$12$m/YFN4bHk4qQp.v.1sFJb.BQgWbURFOt3CN.PwxBdbdUdnzOlN0Ji', 'admin@example.com'),
('app_user', '$2a$12$3dPlzcqpi5vs6Tl7rF53gOL9Uz2cvejnkKhYNfHDfOesmz/0ucM3m', 'user@example.com'),
('app_tester', '$2a$12$njskpoqucFPK79/xQlmeBej00feVG1jiEv7wlX6UVarGBpS/pmxC2', 'tester@example.com');

-- Roles
INSERT INTO T_ROLE (app_id, role_name, description) VALUES
(1, 'ADMIN', 'Administrator role for Backoffice'),
(2, 'CUSTOMER', 'Regular customer mobile role'),
(3, 'TESTER', 'QA tester for Backoffice'),
(4, 'PARTNER', 'External B2B Partner API role');

-- Permissions
INSERT INTO T_PERMISSION (app_id, permission_name, description) VALUES
(1, 'CREATE_USER', 'Permission to create users in Backoffice'),
(1, 'READ_USER', 'Permission to read user details'),
(1, 'UPDATE_USER', 'Permission to update users'),
(4, 'READ_ORDERS', 'Read orders via API'),
(4, 'PUSH_ORDERS', 'Push orders via API');

-- Role ↔ Permission
INSERT INTO T_ROLE_PERMISSION (role_id, permission_id) VALUES
(1, 1), (1, 2), (1, 3), -- ADMIN full user perms
(4, 4), (4, 5);         -- PARTNER API perms

-- User ↔ Role
INSERT INTO T_USER_ROLE (user_id, role_id, app_id) VALUES
(1, 1, 1), -- admin → ADMIN role on Backoffice
(2, 2, 2), -- app_user → CUSTOMER role on Mobile
(3, 3, 1); -- app_tester → TESTER role on Backoffice

-- B2B Clients
INSERT INTO T_CLIENT (app_id, client_key, client_secret, name, description)
VALUES
(4, 'partner_api_client', '$2a$12$2asdJjpsxHjkbD93lkjPs0QPiI.xVfVQ31k9Jb9eMbx5x/4gPtjFi', 'Partner Corp', 'API client for Partner integration');

-- Client ↔ Role
INSERT INTO T_CLIENT_ROLE (client_id, role_id, app_id) VALUES
(1, 4, 4); -- Partner API client gets PARTNER role
