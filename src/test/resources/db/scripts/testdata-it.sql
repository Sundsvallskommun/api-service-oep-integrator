INSERT INTO open_e_instance (connect_timeout, read_timeout, base_url, id, instance_type, integration_type,
                             municipality_id, password, username)
VALUES (5, 60, 'http://localhost:9090/external-oepMock', '0a12ddc7-535a-41b1-91df-afe60d3ce041', 'EXTERNAL', 'SOAP', '2281',
        'qO2h/Om5KsJtQ/nqDjJVHscUV61VpV2IszrYg8qSspuAfto=', 'admin'),
       (5, 60, 'http://localhost:9090/internal-oepMock', 'b97977a2-fe12-4397-bc83-be5ce61e950e', 'INTERNAL', 'SOAP', '2281',
        'qO2h/Om5KsJtQ/nqDjJVHscUV61VpV2IszrYg8qSspuAfto=', 'admin'),
       (5, 60, 'http://localhost:9090/external-oepMock', '035e448c-f836-4af4-b93b-cdcc29e54d53', 'EXTERNAL', 'REST', '2281',
        'qO2h/Om5KsJtQ/nqDjJVHscUV61VpV2IszrYg8qSspuAfto=', 'admin'),
       (5, 60, 'http://localhost:9090/internal-oepMock', '8afe77f1-4908-4595-9d15-45119b73efc2', 'INTERNAL', 'REST', '2281',
        'qO2h/Om5KsJtQ/nqDjJVHscUV61VpV2IszrYg8qSspuAfto=', 'admin');

INSERT INTO family_ids (family_id, instance_id)
VALUES ('123', '0a12ddc7-535a-41b1-91df-afe60d3ce041'),
       ('123', 'b97977a2-fe12-4397-bc83-be5ce61e950e'),
       ('123', '035e448c-f836-4af4-b93b-cdcc29e54d53'),
       ('123', '8afe77f1-4908-4595-9d15-45119b73efc2');
