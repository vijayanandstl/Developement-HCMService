INSERT INTO hcm.candidate (candidate_id, tenant_id, organization_id, first_name, last_name, email, phone, address, date_of_birth, gender, nationality, created_at, created_by, updated_at, updated_by)
VALUES
  ('11111111-1111-1111-1111-111111111111', '22222222-2222-2222-2222-222222222222', '33333333-3333-3333-3333-333333333333', 'Alice', 'Smith', 'alice@example.com', '1234567890', '123 Main St', '1990-01-01', 'Female', 'USA', now(), '00000000-0000-0000-0000-000000000000', now(), '00000000-0000-0000-0000-000000000000'); 

-- Sample Skills
INSERT INTO hcm.skill (skill_id, skill_name, skill_category) VALUES
    (gen_random_uuid(), 'Java', 'Programming'),
    (gen_random_uuid(), 'Spring Boot', 'Framework'),
    (gen_random_uuid(), 'PostgreSQL', 'Database'),
    (gen_random_uuid(), 'Kafka', 'Message Broker'),
    (gen_random_uuid(), 'Docker', 'DevOps'),
    (gen_random_uuid(), 'Kubernetes', 'DevOps'),
    (gen_random_uuid(), 'React', 'Frontend'),
    (gen_random_uuid(), 'TypeScript', 'Programming'),
    (gen_random_uuid(), 'AWS', 'Cloud'),
    (gen_random_uuid(), 'CI/CD', 'DevOps');

-- Sample Candidate Education
INSERT INTO hcm.candidate_education (education_id, candidate_id, institution, degree, field_of_study, start_date, end_date, gpa)
SELECT 
    gen_random_uuid(),
    c.candidate_id,
    'University of Technology',
    'Bachelor of Science',
    'Computer Science',
    '2018-09-01',
    '2022-05-15',
    3.8
FROM hcm.candidate c
WHERE c.email = 'john.doe@example.com';

-- Sample Candidate Work History
INSERT INTO hcm.candidate_work_history (work_history_id, candidate_id, company, position, start_date, end_date, description)
SELECT 
    gen_random_uuid(),
    c.candidate_id,
    'Tech Solutions Inc.',
    'Software Engineer',
    '2022-06-01',
    '2023-12-31',
    'Developed and maintained microservices using Spring Boot and Kafka'
FROM hcm.candidate c
WHERE c.email = 'john.doe@example.com';

-- Sample Candidate Certification
INSERT INTO hcm.candidate_certification (certification_id, candidate_id, certification_name, issuing_organization, issue_date, expiry_date, credential_id)
SELECT 
    gen_random_uuid(),
    c.candidate_id,
    'AWS Certified Solutions Architect',
    'Amazon Web Services',
    '2023-01-15',
    '2026-01-15',
    'AWS-123456'
FROM hcm.candidate c
WHERE c.email = 'john.doe@example.com';

-- Sample Candidate Skills
INSERT INTO hcm.candidate_skill (candidate_skill_id, candidate_id, skill_id, proficiency_level)
SELECT 
    gen_random_uuid(),
    c.candidate_id,
    s.skill_id,
    'Expert'
FROM hcm.candidate c
CROSS JOIN hcm.skill s
WHERE c.email = 'john.doe@example.com'
AND s.skill_name IN ('Java', 'Spring Boot', 'PostgreSQL', 'Kafka');

-- Add more skills with different proficiency levels
INSERT INTO hcm.candidate_skill (candidate_skill_id, candidate_id, skill_id, proficiency_level)
SELECT 
    gen_random_uuid(),
    c.candidate_id,
    s.skill_id,
    'Intermediate'
FROM hcm.candidate c
CROSS JOIN hcm.skill s
WHERE c.email = 'john.doe@example.com'
AND s.skill_name IN ('Docker', 'Kubernetes', 'React'); 