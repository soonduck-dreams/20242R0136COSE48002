-- Drop existing sequences if they exist
DROP SEQUENCE IF EXISTS chord_seq CASCADE;
DROP SEQUENCE IF EXISTS progression_seq CASCADE;
DROP SEQUENCE IF EXISTS chord_progression_map_seq CASCADE;

-- Create sequences
CREATE SEQUENCE chord_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE progression_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE chord_progression_map_seq START WITH 1 INCREMENT BY 50;

-- Create `chord` table
CREATE TABLE chord (
                       chord_id BIGINT NOT NULL,
                       is_public BOOLEAN NOT NULL,
                       name VARCHAR(255) NOT NULL,
                       level VARCHAR(255) NOT NULL CHECK (level IN ('B', 'C', 'D')),
                       description TEXT,
                       image_url TEXT,
                       audio_url TEXT,
                       PRIMARY KEY (chord_id)
);

-- Create `progression` table
CREATE TABLE progression (
                             progression_id BIGINT NOT NULL,
                             description TEXT,
                             audio_url TEXT,
                             sample_midi_url TEXT,
                             is_cadence BOOLEAN NOT NULL,
                             PRIMARY KEY (progression_id)
);

-- Create `chord_progression_map` table
CREATE TABLE chord_progression_map (
                                       map_id BIGINT NOT NULL,
                                       position INTEGER NOT NULL,
                                       chord_id BIGINT NOT NULL,
                                       progression_id BIGINT NOT NULL,
                                       PRIMARY KEY (map_id)
);

-- Add foreign key constraints
ALTER TABLE chord_progression_map
    ADD CONSTRAINT FK_chord_progression_map_chord FOREIGN KEY (chord_id)
        REFERENCES chord (chord_id);

ALTER TABLE chord_progression_map
    ADD CONSTRAINT FK_chord_progression_map_progression FOREIGN KEY (progression_id)
        REFERENCES progression (progression_id);
