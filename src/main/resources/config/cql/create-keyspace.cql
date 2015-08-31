CREATE KEYSPACE IF NOT EXISTS sampleCassandra
    WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 }
    AND DURABLE_WRITES = true;
