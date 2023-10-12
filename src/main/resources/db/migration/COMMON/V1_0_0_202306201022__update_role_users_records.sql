UPDATE users
SET role = 'USER'
WHERE role IN ('MENTOR', 'MENTEE');