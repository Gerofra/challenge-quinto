INSERT INTO roles(id, name)
SELECT 1,
       'ROLE_USER'
        WHERE NOT EXISTS (SELECT * FROM roles WHERE id=1);
        
INSERT INTO roles(id, name)
SELECT 2,
       'ROLE_STUDENT'
        WHERE NOT EXISTS (SELECT * FROM roles WHERE id=2);
        
INSERT INTO roles(id, name)
SELECT 3,
       'ROLE_PROFESSOR'
        WHERE NOT EXISTS (SELECT * FROM roles WHERE id=3);
        
INSERT INTO roles(id, name)
SELECT 4,
       'ROLE_ADMIN'
        WHERE NOT EXISTS (SELECT * FROM roles WHERE id=4);    
        
INSERT INTO roles(id, name)
SELECT 4,
       'ROLE_ADMIN'
        WHERE NOT EXISTS (SELECT * FROM roles WHERE id=4); 
        
INSERT INTO users(id, date, email, lastname, name, password, roles)
SELECT '1',
       '2022-12-31 21:00:00.000000',
       'admin@admin.com',
       ':)',
       'MyAdmin',
       '$2a$10$sj/BCAKDBrjt.B0t5FSWDufXcdRZf9mtl5Fp5pJrR0xoDwcpRxggi',
       '4'
        WHERE NOT EXISTS (SELECT * FROM users WHERE id='1'); 
     
       