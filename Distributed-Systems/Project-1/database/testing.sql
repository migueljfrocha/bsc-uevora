
BEGIN;
INSERT INTO users (name, email, phone, operational_state, admin_state, created_at) VALUES
  ('Alice Silva',  'alice@example.com', '912000001', 'active', 'approved', CURRENT_TIMESTAMP),
  ('Bruno Costa',  'bruno@example.com', '912000002', 'active', 'approved', CURRENT_TIMESTAMP),
  ('Carla Sousa',  'carla@example.com', '912000003', 'active', 'rejected', CURRENT_TIMESTAMP);
COMMIT;

BEGIN;
INSERT INTO apartments (name, location, price_per_night, type, operational_state, admin_state, owner_id) VALUES
  ('T1 Center',       'Lisbon, Center',  45.00, 1, 'available', 'approved', 1),
  ('Marina Flat',     'Porto, Foz',      75.50, 2, 'available', 'approved', 2),
  ('Garden House',    'Coimbra, Neighborhood', 60.00, 3, 'maintenance', 'rejected', 1);
COMMIT;

BEGIN;
INSERT INTO reservations (apartment_id, renter_id, start_date, end_date, total_price, operational_state, admin_state, created_at) VALUES
  (1, 3, '2025-12-01', '2025-12-05', 4 * 45.00, 'confirmed', 'approved', CURRENT_TIMESTAMP),
  (2, 3, '2025-12-10', '2025-12-12', 2 * 75.50, 'confirmed', 'approved', CURRENT_TIMESTAMP),
  (3, 3, '2026-01-15', '2026-01-20', 5 * 60.00, 'pending',   'rejected', CURRENT_TIMESTAMP);
COMMIT;

