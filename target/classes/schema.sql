CREATE TABLE IF NOT EXISTS clothing_item (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    size TEXT NOT NULL,
    color TEXT NOT NULL,
    price REAL NOT NULL,
    stock INTEGER NOT NULL,
    category TEXT
);
