-- SEEDING AUTH SERVICE (authdb)
\c authdb;
TRUNCATE TABLE users, refresh_tokens RESTART IDENTITY CASCADE;

INSERT INTO users (first_name, last_name, email, password_hash, role, account_status, city, country, created_at) VALUES
('Ahmed', 'Ali', 'ahmed@example.com', '$2a$10$zB6BRl8no3rrLPh7plqiBurlVJy8Ed8XZ0ZlLuqzRKcDMRQo0nIiK', 'ADOPTER', 'APPROVED', 'Cairo', 'Egypt', NOW()),
('Sara', 'Maged', 'sara@example.com', '$2a$10$zB6BRl8no3rrLPh7plqiBurlVJy8Ed8XZ0ZlLuqzRKcDMRQo0nIiK', 'ADOPTER', 'APPROVED', 'Alexandria', 'Egypt', NOW()),
('John', 'Doe', 'john@example.com', '$2a$10$zB6BRl8no3rrLPh7plqiBurlVJy8Ed8XZ0ZlLuqzRKcDMRQo0nIiK', 'ADOPTER', 'APPROVED', 'New York', 'USA', NOW()),
('Emily', 'Smith', 'emily@example.com', '$2a$10$zB6BRl8no3rrLPh7plqiBurlVJy8Ed8XZ0ZlLuqzRKcDMRQo0nIiK', 'ADOPTER', 'APPROVED', 'London', 'UK', NOW()),
('Omar', 'Hassan', 'omar@example.com', '$2a$10$zB6BRl8no3rrLPh7plqiBurlVJy8Ed8XZ0ZlLuqzRKcDMRQo0nIiK', 'ADOPTER', 'APPROVED', 'Giza', 'Egypt', NOW()),
('Happy', 'Paws Shelter', 'shelter1@example.com', '$2a$10$zB6BRl8no3rrLPh7plqiBurlVJy8Ed8XZ0ZlLuqzRKcDMRQo0nIiK', 'SHELTER', 'APPROVED', 'Cairo', 'Egypt', NOW()),
('Rescue', 'Home', 'shelter2@example.com', '$2a$10$zB6BRl8no3rrLPh7plqiBurlVJy8Ed8XZ0ZlLuqzRKcDMRQo0nIiK', 'SHELTER', 'APPROVED', 'Alexandria', 'Egypt', NOW()),
('Pet', 'Haven', 'shelter3@example.com', '$2a$10$zB6BRl8no3rrLPh7plqiBurlVJy8Ed8XZ0ZlLuqzRKcDMRQo0nIiK', 'SHELTER', 'APPROVED', 'London', 'UK', NOW()),
('Admin', 'User', 'admin@petadopt.com', '$2a$10$zB6BRl8no3rrLPh7plqiBurlVJy8Ed8XZ0ZlLuqzRKcDMRQo0nIiK', 'ADMIN', 'APPROVED', 'System', 'Global', NOW()),
('Mona', 'Zaki', 'mona@example.com', '$2a$10$zB6BRl8no3rrLPh7plqiBurlVJy8Ed8XZ0ZlLuqzRKcDMRQo0nIiK', 'ADOPTER', 'APPROVED', 'Cairo', 'Egypt', NOW());

-- SEEDING PET SERVICE (petdb)
\c petdb;
TRUNCATE TABLE pets RESTART IDENTITY CASCADE;

INSERT INTO pets (name, species, breed, age, gender, location, description, status, owner_id, image_urls) VALUES
('Buddy', 'DOG', 'Golden Retriever', 2, 'MALE', 'New Cairo, Egypt', 'Very friendly dog', 'APPROVED', 1, 'https://images.unsplash.com/photo-1552053831-71594a27632d?auto=format&fit=crop&q=80&w=800'),
('Mittens', 'CAT', 'Persian', 1, 'FEMALE', 'Maadi, Cairo', 'Loves to sleep', 'APPROVED', 2, 'https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?auto=format&fit=crop&q=80&w=800'),
('Rex', 'DOG', 'German Shepherd', 4, 'MALE', 'Alexandria, Egypt', 'Active and loyal', 'APPROVED', 3, 'https://images.unsplash.com/photo-1589941013453-ec89f33b5e95?auto=format&fit=crop&q=80&w=800'),
('Luna', 'CAT', 'Siamese', 3, 'FEMALE', 'Sheikh Zayed, Giza', 'Quiet and calm', 'APPROVED', 4, 'https://images.unsplash.com/photo-1513245533418-2974e99bc96c?auto=format&fit=crop&q=80&w=800'),
('Charlie', 'DOG', 'Beagle', 5, 'MALE', 'Heliopolis, Cairo', 'Good with kids', 'APPROVED', 5, 'https://images.unsplash.com/photo-1537151608828-ea2b11777ee8?auto=format&fit=crop&q=80&w=800'),
('Bella', 'DOG', 'Poodle', 2, 'FEMALE', 'Zamalek, Cairo', 'Playful and smart', 'APPROVED', 6, 'https://images.unsplash.com/photo-1516734212186-a967f81ad0d7?auto=format&fit=crop&q=80&w=800'),
('Leo', 'CAT', 'Tabby', 1, 'MALE', 'Nasr City, Cairo', 'Very energetic', 'APPROVED', 7, 'https://images.unsplash.com/photo-1573865667391-e7503487c88a?auto=format&fit=crop&q=80&w=800'),
('Daisy', 'DOG', 'Labrador', 3, 'FEMALE', 'Hurghada, Egypt', 'Loves water', 'APPROVED', 8, 'https://images.unsplash.com/photo-1591160674255-fc8b9f79dec4?auto=format&fit=crop&q=80&w=800'),
('Simba', 'CAT', 'Main Coon', 2, 'MALE', 'Mansoura, Egypt', 'Gentle giant', 'APPROVED', 9, 'https://images.unsplash.com/photo-1533738363-b7f9aef128ce?auto=format&fit=crop&q=80&w=800'),
('Molly', 'DOG', 'Bulldog', 4, 'FEMALE', 'Tanta, Egypt', 'Chill vibes', 'APPROVED', 10, 'https://images.unsplash.com/photo-1583511655857-d19b40a7a54e?auto=format&fit=crop&q=80&w=800');

-- SEEDING ADOPTION SERVICE (adoptiondb)
\c adoptiondb;
TRUNCATE TABLE adoptions RESTART IDENTITY CASCADE;

INSERT INTO adoptions (pet_id, adopter_id, owner_id, message, why_this_pet, status, created_at, requested_at) VALUES
(1, 1, 1, 'Interested in Buddy', 'I love Golden Retrievers', 'PENDING', NOW(), NOW()),
(2, 2, 2, 'Ready for Mittens', 'Love Persians', 'PENDING', NOW(), NOW()),
(3, 3, 3, 'Rex is cool', 'Need a guard dog', 'PENDING', NOW(), NOW()),
(4, 4, 4, 'Luna is sweet', 'Companion for my cat', 'PENDING', NOW(), NOW()),
(5, 5, 5, 'Charlie is cute', 'Good for my backyard', 'PENDING', NOW(), NOW());

-- SEEDING INTERACTION SERVICE (interactiondb)
\c interactiondb;
TRUNCATE TABLE favorites, reviews, notifications RESTART IDENTITY CASCADE;

INSERT INTO reviews (adopter_id, pet_id, rating, comment, created_at) VALUES
(1, 1, 5, 'Great experience!', NOW()),
(2, 2, 4, 'Very helpful shelter', NOW()),
(3, 3, 5, 'Highly recommended', NOW());

INSERT INTO favorites (user_id, pet_id, created_at) VALUES
(1, 1, NOW()), (1, 2, NOW()), (2, 3, NOW()), (3, 4, NOW()), (4, 5, NOW());
