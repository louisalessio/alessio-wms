CREATE TABLE material (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL,
    description VARCHAR(255) NOT NULL,
    cost DECIMAL(10,2),
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- index on the active material codes
CREATE UNIQUE INDEX idx_material_code_active ON material(code) WHERE active = true;

CREATE TABLE site (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE inventory (
    material_id BIGINT PRIMARY KEY REFERENCES material(id) ON DELETE RESTRICT,
    quantity INTEGER NOT NULL DEFAULT 0,
    version INTEGER NOT NULL DEFAULT 0 -- mandatory for the optmistic locking
);

-- stock movements (append only)
CREATE TABLE movement (
    id BIGSERIAL PRIMARY KEY,
    material_id BIGINT NOT NULL REFERENCES material(id) ON DELETE RESTRICT,
    site_id BIGINT REFERENCES site(id) ON DELETE RESTRICT,
    type VARCHAR(20) NOT NULL, -- in/out type
    quantity INTEGER NOT NULL,
    movement_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    operator VARCHAR(100) NOT NULL
);