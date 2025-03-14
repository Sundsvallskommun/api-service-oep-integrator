INSERT INTO open_e_instance (connect_timeout, read_timeout, base_url, id, instance_type, integration_type,
                             municipality_id, password, username)
VALUES (5, 60, 'http://localhost:9090/external-oepMock', '1', 'EXTERNAL', 'SOAP', '2281',
        'qO2h/Om5KsJtQ/nqDjJVHscUV61VpV2IszrYg8qSspuAfto=', 'admin'),
       (5, 60, 'http://localhost:9090/internal-oepMock', '2', 'INTERNAL', 'SOAP', '2281',
        'qO2h/Om5KsJtQ/nqDjJVHscUV61VpV2IszrYg8qSspuAfto=', 'admin'),
       (5, 60, 'http://localhost:9090/external-oepMock', '3', 'EXTERNAL', 'REST', '2281',
        'qO2h/Om5KsJtQ/nqDjJVHscUV61VpV2IszrYg8qSspuAfto=', 'admin'),
       (5, 60, 'http://localhost:9090/internal-oepMock', '4', 'INTERNAL', 'REST', '2281',
        'qO2h/Om5KsJtQ/nqDjJVHscUV61VpV2IszrYg8qSspuAfto=', 'admin');

INSERT INTO family_ids (family_id, instance_id)
VALUES ('123', '1'),
       ('123', '2'),
       ('123', '3'),
       ('123', '4');
