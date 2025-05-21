-- Force insert tenant and organization
DO $$
BEGIN
    -- First, ensure we're in a transaction
    BEGIN
        -- Force insert tenant
        INSERT INTO hcm.tenant (tenant_id, name, created_at, created_by, updated_at, updated_by)
        VALUES (
            '00000000-0000-0000-0000-000000000001',
            'Default Tenant',
            now(),
            '00000000-0000-0000-0000-000000000001',
            now(),
            '00000000-0000-0000-0000-000000000001'
        )
        ON CONFLICT (tenant_id) DO UPDATE
        SET name = EXCLUDED.name,
            updated_at = now();

        -- Force insert organization
        INSERT INTO hcm.organization (organization_id, tenant_id, name, created_at, created_by, updated_at, updated_by)
        VALUES (
            '00000000-0000-0000-0000-000000000001',
            '00000000-0000-0000-0000-000000000001',
            'Default Organization',
            now(),
            '00000000-0000-0000-0000-000000000001',
            now(),
            '00000000-0000-0000-0000-000000000001'
        )
        ON CONFLICT (organization_id) DO UPDATE
        SET name = EXCLUDED.name,
            updated_at = now();

        -- Verify the data was inserted
        IF NOT EXISTS (SELECT 1 FROM hcm.tenant WHERE tenant_id = '00000000-0000-0000-0000-000000000001') THEN
            RAISE EXCEPTION 'Failed to insert tenant';
        END IF;

        IF NOT EXISTS (SELECT 1 FROM hcm.organization WHERE organization_id = '00000000-0000-0000-0000-000000000001') THEN
            RAISE EXCEPTION 'Failed to insert organization';
        END IF;

        RAISE NOTICE 'Successfully inserted tenant and organization';
    EXCEPTION
        WHEN OTHERS THEN
            RAISE EXCEPTION 'Error during data insertion: %', SQLERRM;
    END;
END $$; 