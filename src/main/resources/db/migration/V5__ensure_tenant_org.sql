-- Ensure tenant and organization exist
DO $$
DECLARE
    v_tenant_id UUID := '00000000-0000-0000-0000-000000000001';
    v_org_id UUID := '00000000-0000-0000-0000-000000000001';
    v_user_id UUID := '00000000-0000-0000-0000-000000000001';
BEGIN
    -- Start transaction
    BEGIN
        -- Check if tenant exists
        IF NOT EXISTS (SELECT 1 FROM hcm.tenant WHERE tenant_id = v_tenant_id) THEN
            -- Insert tenant
            INSERT INTO hcm.tenant (
                tenant_id,
                name,
                domain,
                created_at,
                created_by,
                updated_at,
                updated_by
            ) VALUES (
                v_tenant_id,
                'Default Tenant',
                'default.local',
                CURRENT_TIMESTAMP,
                v_user_id,
                CURRENT_TIMESTAMP,
                v_user_id
            );
            RAISE NOTICE 'Inserted new tenant';
        END IF;

        -- Check if organization exists
        IF NOT EXISTS (SELECT 1 FROM hcm.organization WHERE organization_id = v_org_id) THEN
            -- Insert organization
            INSERT INTO hcm.organization (
                organization_id,
                tenant_id,
                name,
                address,
                status_id,
                created_at,
                created_by,
                updated_at,
                updated_by
            ) VALUES (
                v_org_id,
                v_tenant_id,
                'Default Organization',
                'Default Address',
                1,
                CURRENT_TIMESTAMP,
                v_user_id,
                CURRENT_TIMESTAMP,
                v_user_id
            );
            RAISE NOTICE 'Inserted new organization';
        END IF;

        -- Verify data
        IF NOT EXISTS (SELECT 1 FROM hcm.tenant WHERE tenant_id = v_tenant_id) THEN
            RAISE EXCEPTION 'Tenant verification failed after insertion';
        END IF;

        IF NOT EXISTS (SELECT 1 FROM hcm.organization WHERE organization_id = v_org_id) THEN
            RAISE EXCEPTION 'Organization verification failed after insertion';
        END IF;

        RAISE NOTICE 'Successfully verified tenant and organization existence';
    EXCEPTION
        WHEN OTHERS THEN
            RAISE EXCEPTION 'Error in tenant/organization setup: %', SQLERRM;
    END;
END $$; 