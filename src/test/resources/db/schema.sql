
    create table family_ids (
        family_id varchar(255),
        instance_id varchar(255) not null
    ) engine=InnoDB;

    create table open_e_instance (
        connect_timeout integer,
        read_timeout integer,
        base_url varchar(255),
        id varchar(255) not null,
        municipality_id varchar(255),
        password varchar(255),
        username varchar(255),
        instance_type enum ('EXTERNAL','INTERNAL'),
        integration_type enum ('REST','SOAP'),
        primary key (id)
    ) engine=InnoDB;

    create index idx_municipality_id 
       on open_e_instance (municipality_id);

    create index idx_municipality_id_id 
       on open_e_instance (municipality_id, id);

    create index idx_integration_type 
       on open_e_instance (integration_type);

    create index idx_id_municipality_id 
       on open_e_instance (id, municipality_id);

    alter table if exists family_ids 
       add constraint fk_family_ids_instance_id 
       foreign key (instance_id) 
       references open_e_instance (id);
