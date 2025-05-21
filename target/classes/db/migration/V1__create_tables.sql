-- Create tenant table
CREATE TABLE IF NOT EXISTS hcm.tenant (
    tenant_id UUID PRIMARY KEY,
    name VARCHAR(100),
    domain VARCHAR(100),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by UUID NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_by UUID NOT NULL
);

-- Create organization table
CREATE TABLE IF NOT EXISTS hcm.organization (
    organization_id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES hcm.tenant(tenant_id),
    name VARCHAR(100),
    address VARCHAR(500),
    status_id INTEGER,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by UUID NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_by UUID NOT NULL
);

-- Create skill table
CREATE TABLE IF NOT EXISTS hcm.skill (
    skill_id SERIAL PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES hcm.tenant(tenant_id),
    organization_id UUID NOT NULL REFERENCES hcm.organization(organization_id),
    skill_name VARCHAR(100),
    skill_category VARCHAR(100),
    description TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by UUID NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_by UUID NOT NULL
);

-- Create candidate table
CREATE TABLE IF NOT EXISTS hcm.candidate (
    candidate_id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES hcm.tenant(tenant_id),
    organization_id UUID NOT NULL REFERENCES hcm.organization(organization_id),
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(200) NOT NULL UNIQUE,
    phone VARCHAR(20),
    address VARCHAR(500),
    date_of_birth DATE,
    gender VARCHAR(50),
    nationality VARCHAR(100),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by UUID NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_by UUID NOT NULL,
    deleted_at TIMESTAMPTZ,
    deleted_by UUID
);

-- Create candidate_skill table
CREATE TABLE IF NOT EXISTS hcm.candidate_skill (
    candidate_id UUID NOT NULL REFERENCES hcm.candidate(candidate_id),
    skill_id INTEGER NOT NULL REFERENCES hcm.skill(skill_id),
    proficiency_level VARCHAR(50),
    years_of_experience INTEGER,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by UUID NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_by UUID NOT NULL,
    PRIMARY KEY (candidate_id, skill_id)
);

-- Create candidate_work_history table
CREATE TABLE IF NOT EXISTS hcm.candidate_work_history (
    work_history_id SERIAL PRIMARY KEY,
    candidate_id UUID NOT NULL REFERENCES hcm.candidate(candidate_id),
    company_name VARCHAR(200),
    position_title VARCHAR(100),
    location VARCHAR(200),
    start_date DATE,
    end_date DATE,
    responsibilities TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by UUID NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_by UUID NOT NULL
);

-- Create candidate_education table
CREATE TABLE IF NOT EXISTS hcm.candidate_education (
    education_id SERIAL PRIMARY KEY,
    candidate_id UUID NOT NULL REFERENCES hcm.candidate(candidate_id),
    institution VARCHAR(200),
    degree VARCHAR(100),
    field_of_study VARCHAR(100),
    start_date DATE,
    end_date DATE,
    grade VARCHAR(10),
    notes TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by UUID NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_by UUID NOT NULL
);

-- Create candidate_certification table
CREATE TABLE IF NOT EXISTS hcm.candidate_certification (
    certification_id SERIAL PRIMARY KEY,
    candidate_id UUID NOT NULL REFERENCES hcm.candidate(candidate_id),
    certificate_name VARCHAR(200),
    issued_by VARCHAR(200),
    issue_date DATE,
    expiry_date DATE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by UUID NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_by UUID NOT NULL
); 