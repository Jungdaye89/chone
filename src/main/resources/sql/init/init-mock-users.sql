-- 테스트 사용자 데이터 생성
INSERT INTO p_user (id,
                    username,
                    email,
                    password,
                    role,
                    is_available,
                    created_at,
                    created_by)
VALUES (1000, 'customer', 'customer@example.com', 'customerPass', 'CUSTOMER', true, NOW(),
        'system'),
       (2000, 'owner', 'owner@example.com', 'ownerPass', 'OWNER', true, NOW(), 'system'),
       (3000, 'manager', 'manager@example.com', 'managerPass', 'MANAGER', true, NOW(), 'system'),
       (4000, 'master', 'master@example.com', 'masterPass', 'MASTER', true, NOW(), 'system');