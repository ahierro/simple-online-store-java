-- This script is just for testing purposes in order to check that indexes are being used
SET enable_seqscan = off;

EXPLAIN (COSTS OFF) SELECT * FROM product WHERE id_category = '7836a937-5467-4191-9d2c-b65bba26f64e';
EXPLAIN (COSTS OFF) SELECT * FROM product WHERE id = 'f558f20e-aa01-41a3-bba0-45f0177e5344';

