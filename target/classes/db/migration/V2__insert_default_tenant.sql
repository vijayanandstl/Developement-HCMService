-- Insert default tenant
INSERT INTO hcm.tenant (tenant_id, name, created_at, created_by, updated_at, updated_by)
VALUES (
    '00000000-0000-0000-0000-000000000001',
    'Default Tenant',
    now(),
    '00000000-0000-0000-0000-000000000001',
    now(),
    '00000000-0000-0000-0000-000000000001'
) ON CONFLICT (tenant_id) DO NOTHING;

-- Insert default organization
INSERT INTO hcm.organization (organization_id, tenant_id, name, created_at, created_by, updated_at, updated_by)
VALUES (
    '00000000-0000-0000-0000-000000000001',
    '00000000-0000-0000-0000-000000000001',
    'Default Organization',
    now(),
    '00000000-0000-0000-0000-000000000001',
    now(),
    '00000000-0000-0000-0000-000000000001'
) ON CONFLICT (organization_id) DO NOTHING; 