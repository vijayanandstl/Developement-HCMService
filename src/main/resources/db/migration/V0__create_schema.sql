-- Create schema if it doesn't exist
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_namespace WHERE nspname = 'hcm') THEN
        CREATE SCHEMA hcm;
        RAISE NOTICE 'Created schema hcm';
    ELSE
        RAISE NOTICE 'Schema hcm already exists';
    END IF;
END $$; 