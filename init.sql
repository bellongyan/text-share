-- texts table
CREATE TABLE IF NOT EXISTS texts (
    id              VARCHAR(32) PRIMARY KEY,
    content         TEXT NOT NULL,
    ip_address      VARCHAR(45) NOT NULL,
    user_agent      VARCHAR(512),
    device_info     VARCHAR(128),
    view_count      INTEGER DEFAULT 0,
    expires_at      TIMESTAMP NOT NULL,
    created_at      TIMESTAMP DEFAULT NOW(),
    updated_at      TIMESTAMP DEFAULT NOW(),
    is_deleted      BOOLEAN DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_texts_expires_at ON texts(expires_at);
CREATE INDEX IF NOT EXISTS idx_texts_created_at ON texts(created_at);
CREATE INDEX IF NOT EXISTS idx_texts_ip_address ON texts(ip_address);

-- access_logs table
CREATE TABLE IF NOT EXISTS access_logs (
    id              BIGSERIAL PRIMARY KEY,
    text_id         VARCHAR(32) NOT NULL,
    ip_address      VARCHAR(45) NOT NULL,
    user_agent      VARCHAR(512),
    access_time     TIMESTAMP DEFAULT NOW(),
    action          VARCHAR(20) NOT NULL,
    CONSTRAINT fk_access_logs_text_id FOREIGN KEY (text_id) REFERENCES texts(id)
);

CREATE INDEX IF NOT EXISTS idx_access_logs_text_id ON access_logs(text_id);
CREATE INDEX IF NOT EXISTS idx_access_logs_access_time ON access_logs(access_time);
CREATE INDEX IF NOT EXISTS idx_access_logs_ip_address ON access_logs(ip_address);

-- tenants table
CREATE TABLE IF NOT EXISTS tenants (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code            VARCHAR(32) UNIQUE NOT NULL,
    name            VARCHAR(128) NOT NULL,
    status          VARCHAR(20) DEFAULT 'ACTIVE',
    settings        JSONB,
    created_at      TIMESTAMP DEFAULT NOW(),
    updated_at      TIMESTAMP DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_tenants_code ON tenants(code);
CREATE INDEX IF NOT EXISTS idx_tenants_status ON tenants(status);

-- default tenant
INSERT INTO tenants (code, name, status) VALUES ('default', 'Default Tenant', 'ACTIVE');
