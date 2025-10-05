create TYPE user_role as ENUM ('TELLER', 'MANAGER', 'ADMIN', 'AUDITOR');

create table users
(
    id       serial primary key,
    name     varchar(255) not null,
    email    varchar(255) not null unique,
    password varchar(255) not null,
    role     user_role
);

INSERT INTO users (name, email, password, role)
VALUES ('System Administrator', 'admin@bank.com', 'admin123', 'ADMIN'),
       ('John Manager', 'manager@bank.com', 'manager123', 'MANAGER'),
       ('Alice Teller', 'alice.teller@bank.com', 'teller123', 'TELLER'),
       ('Bob Teller', 'bob.teller@bank.com', 'teller123', 'TELLER'),
       ('Sarah Auditor', 'auditor@bank.com', 'auditor123', 'AUDITOR');

-- create table clients
create table clients
(
    id           uuid primary key,
    first_name   varchar(225)       not null,
    last_name    varchar(225)       not null,
    cin          varchar(50) unique not null,
    phone_number varchar(50),
    address      text,
    email        varchar(225) unique,
    created_by   int references users (id)

)

-- generate id
ALTER TABLE clients
    ALTER COLUMN id SET DEFAULT gen_random_uuid();

-- create table account

CREATE TYPE account_type_enum AS ENUM ('CURRENT', 'SAVINGS', 'CREDIT');

create table accounts
(
    id             uuid primary key default gen_random_uuid(),
    account_number varchar(30) unique not null,
    balance        decimal(15, 2)   default 0.00,
    type           account_type_enum  not null,
    isActive       boolean          default true,
    client_id      uuid references clients (id) on delete cascade,
    created_by     int references users (id)
)

CREATE TYPE transaction_type_enum AS ENUM (
    'DEPOSIT',
    'WITHDRAW',
    'TRANSFER_IN',
    'TRANSFER_OUT',
    'TRANSFER_EXTERNAL',
    'FEE',
    'FEEINCOME'
    );
create table transactions
(
    id                  uuid primary key default gen_random_uuid(),
    amount              numeric(15, 2)        not null,
    type                transaction_type_enum NOT NULL,
    timestamp           TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    sender_account_id   UUID REFERENCES accounts (id),
    receiver_account_id UUID REFERENCES accounts (id)
)
ALTER TABLE transactions
    ADD COLUMN status VARCHAR(20) DEFAULT 'COMPLETED'
        CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED', 'CANCELLED'));

-- credit table
CREATE TYPE credit_status AS ENUM ('PENDING', 'ACTIVE', 'LATE', 'CLOSED', 'REJECTED');

CREATE TABLE credits
(
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id      VARCHAR(30)    NOT NULL REFERENCES accounts (account_number) ON DELETE CASCADE,
    amount          NUMERIC(18, 2) NOT NULL CHECK (amount > 0),
    interest_rate   NUMERIC(5, 2)  NOT NULL CHECK (interest_rate > 0),
    duration_months INT            NOT NULL CHECK (duration_months > 0),
    status          credit_status    DEFAULT 'PENDING',
    created_at      TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

-- create  fee-rule table
CREATE TABLE  fee_rule
(
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    modal         VARCHAR(20) NOT NULL CHECK (modal IN ('FIX','PERCENT')),
    rule_value    NUMERIC(18,4) NOT NULL CHECK (rule_value >= 0),
    operation_type VARCHAR(30) NOT NULL
    CHECK (operation_type IN ('TRANSFER_EXTERNAL','TRANSFER_INTERNAL',
           'DEPOSIT','WITHDRAW','CREDIT')),
    is_active     BOOLEAN DEFAULT TRUE
    );


INSERT INTO fee_rules (modal, rule_value, operation_type, is_active)
VALUES ('FIX', 5.00, 'TRANSFER_EXTERNAL', true);

INSERT INTO fee_rules (modal, rule_value, operation_type, is_active)
VALUES ('PERCENT', 1.00, 'WITHDRAW', true);

INSERT INTO fee_rules (modal, rule_value, operation_type, is_active)
VALUES ('PERCENT', 0.50, 'TRANSFER_IN', true);

INSERT INTO fee_rules (modal, rule_value, operation_type, is_active)
VALUES ('FIX', 0.00, 'DEPOSIT', false);

INSERT INTO fee_rules (modal, rule_value, operation_type, is_active)
VALUES ('PERCENT', 2.00, 'CREDIT', true);