CREATE TABLE IF NOT EXISTS TYPE
(
    ID
    UUID
    NOT
    NULL,
    NAME
    VARCHAR
    NOT
    NULL,
    ADDITIONAL_PROPERTIES
    BOOLEAN
    NOT
    NULL,
    BACKEND_KEY_DEFINITIONS
    jsonb
    NOT
    NULL,
    PRIMARY
    KEY
(
    ID
)
    );