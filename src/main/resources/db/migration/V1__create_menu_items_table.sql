-- V1__create_menu_items_table.sql
CREATE TABLE menu_items (
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name               TEXT UNIQUE NOT NULL,
    description        TEXT NOT NULL,
    price              NUMERIC(10, 2) NOT NULL,
    category           TEXT NOT NULL,
    preparation_time   BIGINT NOT NULL,
    weight             DOUBLE PRECISION NOT NULL,
    image_url          TEXT NOT NULL,
    attributes         JSONB NOT NULL,

    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для повышения производительности
CREATE INDEX idx_menu_items_category ON menu_items (category);
CREATE INDEX idx_menu_items_price ON menu_items (price);
CREATE INDEX idx_menu_items_created_at ON menu_items (created_at);
