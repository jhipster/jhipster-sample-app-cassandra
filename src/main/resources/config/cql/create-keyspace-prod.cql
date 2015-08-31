CREATE KEYSPACE IF NOT EXISTS sampleCassandra
    WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 3 }
    AND DURABLE_WRITES = true;
