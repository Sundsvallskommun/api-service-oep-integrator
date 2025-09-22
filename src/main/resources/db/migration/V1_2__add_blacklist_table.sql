CREATE TABLE IF NOT EXISTS black_list (
  family_id VARCHAR(255),
  id VARCHAR(255) NOT NULL,
  instance_type VARCHAR(255),
  municipality_id VARCHAR(8),
  PRIMARY KEY (id),
  KEY idx_municipality_id (municipality_id),
  KEY idx_instance_type (instance_type),
  KEY idx_family_id (family_id),
  KEY idx_municipality_id_instance_type (municipality_id, instance_type)
) ENGINE=InnoDB;
