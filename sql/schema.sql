create TYPE user_role as ENUM ('TELLER', 'MANAGER', 'ADMIN', 'AUDITOR');

create table users(
id serial primary key,
name varchar(255) not null,
email varchar(255) not null unique,
password varchar(255) not null,
role user_role
);

INSERT INTO users (name, email, password, role) VALUES
('System Administrator', 'admin@bank.com', 'admin123', 'ADMIN'),
('John Manager', 'manager@bank.com', 'manager123', 'MANAGER'),
('Alice Teller', 'alice.teller@bank.com', 'teller123', 'TELLER'),
('Bob Teller', 'bob.teller@bank.com', 'teller123', 'TELLER'),
('Sarah Auditor', 'auditor@bank.com', 'auditor123', 'AUDITOR');


