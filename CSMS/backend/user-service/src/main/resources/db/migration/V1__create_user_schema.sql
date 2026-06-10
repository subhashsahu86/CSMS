CREATE TABLE user_profiles (
    id UUID PRIMARY KEY,
    auth_user_id UUID NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    user_type VARCHAR(30) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100),
    last_name VARCHAR(100),
    display_name VARCHAR(255) NOT NULL,
    gender VARCHAR(20),
    date_of_birth DATE,
    primary_email VARCHAR(255),
    primary_phone VARCHAR(30),
    profile_image_url VARCHAR(500),
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_addresses (
    id UUID PRIMARY KEY,
    user_profile_id UUID NOT NULL REFERENCES user_profiles(id) ON DELETE CASCADE,
    address_type VARCHAR(30) NOT NULL,
    address_line_1 VARCHAR(255) NOT NULL,
    address_line_2 VARCHAR(255),
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    country VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE student_profiles (
    id UUID PRIMARY KEY,
    user_profile_id UUID NOT NULL UNIQUE REFERENCES user_profiles(id) ON DELETE CASCADE,
    registration_number VARCHAR(50) UNIQUE,
    admission_number VARCHAR(50) UNIQUE,
    admission_date DATE,
    blood_group VARCHAR(10),
    category VARCHAR(50),
    previous_school VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE teacher_profiles (
    id UUID PRIMARY KEY,
    user_profile_id UUID NOT NULL UNIQUE REFERENCES user_profiles(id) ON DELETE CASCADE,
    employee_code VARCHAR(50) UNIQUE,
    qualification VARCHAR(255),
    specialization VARCHAR(255),
    joining_date DATE,
    experience_years INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE parent_profiles (
    id UUID PRIMARY KEY,
    user_profile_id UUID NOT NULL UNIQUE REFERENCES user_profiles(id) ON DELETE CASCADE,
    occupation VARCHAR(255),
    relationship_label VARCHAR(50),
    alternate_phone VARCHAR(30),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE staff_profiles (
    id UUID PRIMARY KEY,
    user_profile_id UUID NOT NULL UNIQUE REFERENCES user_profiles(id) ON DELETE CASCADE,
    employee_code VARCHAR(50) UNIQUE,
    department VARCHAR(100),
    designation VARCHAR(100),
    joining_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE parent_student_links (
    id UUID PRIMARY KEY,
    parent_user_profile_id UUID NOT NULL REFERENCES user_profiles(id) ON DELETE CASCADE,
    student_user_profile_id UUID NOT NULL REFERENCES user_profiles(id) ON DELETE CASCADE,
    relationship VARCHAR(50) NOT NULL,
    is_primary_guardian BOOLEAN NOT NULL DEFAULT FALSE,
    can_receive_notifications BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (parent_user_profile_id, student_user_profile_id, relationship)
);

CREATE INDEX idx_user_profiles_auth_user_id ON user_profiles(auth_user_id);
CREATE INDEX idx_user_profiles_username ON user_profiles(username);
CREATE INDEX idx_user_profiles_user_type ON user_profiles(user_type);
CREATE INDEX idx_user_addresses_profile_id ON user_addresses(user_profile_id);
CREATE INDEX idx_parent_student_links_parent ON parent_student_links(parent_user_profile_id);
CREATE INDEX idx_parent_student_links_student ON parent_student_links(student_user_profile_id);
