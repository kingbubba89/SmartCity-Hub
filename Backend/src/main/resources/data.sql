
INSERT INTO events (title, description, category, location, event_date)
SELECT
    'Community Cleanup Day',
    'Join volunteers to help clean up the community.',
    'Community Service',
    'Riverside Park',
    '2026-11-14 09:00:00'
    WHERE NOT EXISTS (
    SELECT 1 FROM events
    WHERE title = 'Community Cleanup Day'
);

INSERT INTO events (title, description, category, location, event_date)
SELECT
    'City Safety Workshop',
    'Learn about community safety and preparedness.',
    'Public Safety',
    'Community Center',
    '2026-11-18 14:00:00'
    WHERE NOT EXISTS (
    SELECT 1 FROM events
    WHERE title = 'City Safety Workshop'
);

INSERT INTO events (title, description, category, location, event_date)
SELECT
    'Fall Food Festival',
    'Enjoy food and activities from local vendors.',
    'Cultural',
    'Downtown Plaza',
    '2026-11-21 11:00:00'
    WHERE NOT EXISTS (
    SELECT 1 FROM events
    WHERE title = 'Fall Food Festival'
);
