
    create table black_list (
        municipality_id varchar(8),
        family_id varchar(255),
        id varchar(255) not null,
        instance_type varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table family_ids (
        family_id varchar(255),
        instance_id varchar(255) not null
    ) engine=InnoDB;

    create table open_e_instance (
        connect_timeout integer,
        read_timeout integer,
        base_url varchar(255),
        id varchar(255) not null,
        instance_type varchar(255),
        integration_type varchar(255),
        municipality_id varchar(255),
        password varchar(255),
        username varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create index idx_municipality_id 
       on black_list (municipality_id);

    create index idx_instance_type 
       on black_list (instance_type);

    create index idx_family_id 
       on black_list (family_id);

    create index idx_municipality_id_instance_type 
       on black_list (municipality_id, instance_type);

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
