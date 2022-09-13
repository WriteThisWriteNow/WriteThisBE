DO
$do$
    BEGIN
        IF
            EXISTS(SELECT FROM pg_database WHERE datname = 'wsdb') THEN
            RAISE NOTICE 'Database already exists';
        ELSE
            PERFORM dblink_exec('dbname=' || current_database()
                , 'CREATE DATABASE wsdb');
        END IF;
    END
$do$;