-- Drop existing data to ensure clean state
DO $$
BEGIN
    RAISE NOTICE 'Starting data cleanup...';
    DELETE FROM hcm.candidate;
    DELETE FROM hcm.organization;
    DELETE FROM hcm.tenant;
    RAISE NOTICE 'Data cleanup completed';
END $$;

-- Insert default tenant
DO $$
BEGIN
    RAISE NOTICE 'Checking for default tenant...';
    IF NOT EXISTS (SELECT 1 FROM hcm.tenant WHERE tenant_id = '00000000-0000-0000-0000-000000000001') THEN
        RAISE NOTICE 'Inserting default tenant...';
        INSERT INTO hcm.tenant (tenant_id, name, created_at, created_by, updated_at, updated_by)
        VALUES (
            '00000000-0000-0000-0000-000000000001',
            'Default Tenant',
            now(),
            '00000000-0000-0000-0000-000000000001',
            now(),
            '00000000-0000-0000-0000-000000000001'
        );
        RAISE NOTICE 'Default tenant inserted successfully';
    ELSE
        RAISE NOTICE 'Default tenant already exists';
    END IF;
END $$;

-- Insert default organization
DO $$
BEGIN
    RAISE NOTICE 'Checking for default organization...';
    IF NOT EXISTS (SELECT 1 FROM hcm.organization WHERE organization_id = '00000000-0000-0000-0000-000000000001') THEN
        RAISE NOTICE 'Inserting default organization...';
        INSERT INTO hcm.organization (organization_id, tenant_id, name, created_at, created_by, updated_at, updated_by)
        VALUES (
            '00000000-0000-0000-0000-000000000001',
            '00000000-0000-0000-0000-000000000001',
            'Default Organization',
            now(),
            '00000000-0000-0000-0000-000000000001',
            now(),
            '00000000-0000-0000-0000-000000000001'
        );
        RAISE NOTICE 'Default organization inserted successfully';
    ELSE
        RAISE NOTICE 'Default organization already exists';
    END IF;
END $$;

-- Verify data
DO $$
DECLARE
    tenant_count INTEGER;
    org_count INTEGER;
BEGIN
    RAISE NOTICE 'Starting data verification...';
    
    -- Check tenant
    SELECT COUNT(*) INTO tenant_count FROM hcm.tenant WHERE tenant_id = '00000000-0000-0000-0000-000000000001';
    IF tenant_count = 0 THEN
        RAISE EXCEPTION 'Verification failed: Default tenant not found';
    END IF;
    RAISE NOTICE 'Tenant verification passed: Found % tenant(s)', tenant_count;
    
    -- Check organization
    SELECT COUNT(*) INTO org_count FROM hcm.organization WHERE organization_id = '00000000-0000-0000-0000-000000000001';
    IF org_count = 0 THEN
        RAISE EXCEPTION 'Verification failed: Default organization not found';
    END IF;
    RAISE NOTICE 'Organization verification passed: Found % organization(s)', org_count;
    
    RAISE NOTICE 'All verifications completed successfully';
END $$; 