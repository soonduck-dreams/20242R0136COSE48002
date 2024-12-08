ALTER SEQUENCE chord_seq INCREMENT BY 1;
ALTER SEQUENCE progression_seq INCREMENT BY 1;
ALTER SEQUENCE chord_progression_map_seq INCREMENT BY 1;


INSERT INTO chord (chord_id, name, level, is_public, description, image_url, audio_url)
VALUES
    (nextval('chord_seq'), 'Cmaj7', 'B', true, '', '/uploads/chord/image/Imaj7.png', '/uploads/chord/audio/Imaj7.wav'),
    (nextval('chord_seq'), 'Dm7', 'B', true, '', '/uploads/chord/image/II-7.png', '/uploads/chord/audio/II-7.wav'),
    (nextval('chord_seq'), 'Em7', 'B', true, '', '/uploads/chord/image/III-7.png', '/uploads/chord/audio/III-7.wav'),
    (nextval('chord_seq'), 'Fmaj7', 'B', true, '', '/uploads/chord/image/IVmaj7.png', '/uploads/chord/audio/IVmaj7.wav'),
    (nextval('chord_seq'), 'G7', 'B', true, '', '/uploads/chord/image/V7.png', '/uploads/chord/audio/V7.wav'),
    (nextval('chord_seq'), 'Am7', 'B', true, '', '/uploads/chord/image/VI-7.png', '/uploads/chord/audio/VI-7.wav'),
    (nextval('chord_seq'), 'Bm7(♭5)', 'B', true, '', '/uploads/chord/image/VII-7(b5).png', '/uploads/chord/audio/VII-7(b5).wav'),
    (nextval('chord_seq'), 'A7', 'C', true, '', '/uploads/chord/image/V7slashII.png', '/uploads/chord/audio/V7slashII.wav'),
    (nextval('chord_seq'), 'B7', 'C', true, '', '/uploads/chord/image/V7slashIII.png', '/uploads/chord/audio/V7slashIII.wav'),
    (nextval('chord_seq'), 'C7', 'C', true, '', '/uploads/chord/image/V7slashIV.png', '/uploads/chord/audio/V7slashIV.wav'),
    (nextval('chord_seq'), 'D7', 'C', true, '', '/uploads/chord/image/V7slashV.png', '/uploads/chord/audio/V7slashV.wav'),
    (nextval('chord_seq'), 'E7', 'C', true, '', '/uploads/chord/image/V7slashVI.png', '/uploads/chord/audio/V7slashVI.wav'),
    (nextval('chord_seq'), 'F', 'B', false, '', '/uploads/chord/image/IVmaj.png', '/uploads/chord/audio/IVmaj.wav'),
    (nextval('chord_seq'), 'Cm7', 'D', true, '', '/uploads/chord/image/I-7.png', '/uploads/chord/audio/I-7.wav'),
    (nextval('chord_seq'), 'D♭maj7', 'D', true, '', '/uploads/chord/image/flatIIMajor7.png', '/uploads/chord/audio/flatIIMajor7.wav'),
    (nextval('chord_seq'), 'Dm7(♭5)', 'D', true, '', '/uploads/chord/image/II-7flat5.png', '/uploads/chord/audio/II-7flat5.wav'),
    (nextval('chord_seq'), 'E♭maj7', 'D', true, '', '/uploads/chord/image/flatIIIMajor7.png', '/uploads/chord/audio/flatIIIMajor7.wav'),
    (nextval('chord_seq'), 'Fm7', 'D', true, '', '/uploads/chord/image/IV-7.png', '/uploads/chord/audio/IV-7.wav'),
    (nextval('chord_seq'), 'F7', 'D', true, '', '/uploads/chord/image/IV7.png', '/uploads/chord/audio/IV7.wav'),
    (nextval('chord_seq'), 'F♯m7(♭5)', 'D', true, 'C major 기준으로 파#, 라, 도, 미를 누른다. 불안정하고 긴장감을 주는 색다른 소리가 난다. 주로 재즈나 블루스에서 다음 코드로 자연스럽게 넘어갈 때 쓰이며, 팝에서도 종종 쓰인다. 1도의 대리코드로도 사용된다. lydian을 modal source로 하는 코드 중 Gmaj7과 더불어 가장 많이 modal interchange chord로서 사용되는 코드. #IVm7(♭5).', '/uploads/chord/image/sharpIV-7flat5.png', '/uploads/chord/audio/sharpIV-7flat5.wav'),
    (nextval('chord_seq'), 'Gm7', 'D', true, '', '/uploads/chord/image/V-7.png', '/uploads/chord/audio/V-7.wav'),
    (nextval('chord_seq'), 'G7(♭9, ♭13)', 'D', true, '', '/uploads/chord/image/V7flat9flat13.png', '/uploads/chord/audio/V7flat9flat13.wav'),
    (nextval('chord_seq'), 'Gmaj7', 'D', true, '', '/uploads/chord/image/VMajor7.png', '/uploads/chord/audio/VMajor7.wav'),
    (nextval('chord_seq'), 'A♭maj7', 'D', true, '', '/uploads/chord/image/flatVIMajor7.png', '/uploads/chord/audio/flatVIMajor7.wav'),
    (nextval('chord_seq'), 'A♭7', 'D', true, '', '/uploads/chord/image/flatVI7.png', '/uploads/chord/audio/flatVI7.wav'),
    (nextval('chord_seq'), 'B♭maj7', 'D', true, '', '/uploads/chord/image/flatVIIMajor7.png', '/uploads/chord/audio/flatVIIMajor7.wav'),
    (nextval('chord_seq'), 'B♭7', 'D', true, '', '/uploads/chord/image/flatVII7.png', '/uploads/chord/audio/flatVII7.wav');

INSERT INTO progression (progression_id, is_cadence, description, audio_url, sample_midi_url)
VALUES
    (nextval('progression_seq'), false, 'tonic function(안정감, 곡의 시작과 끝에 주로 쓰임) → subdominant function(주로 dominant function의 앞에 와서 긴장을 예비함) → dominant function(긴장감, 해결을 하고 싶은 상태)을 따르는 정석적인 코드 진행.', '/uploads/progression/audio/1.wav', '/uploads/progression/sample_midi/1.mid'),
    (nextval('progression_seq'), true, '흔히 "투 파이브 원"으로 불리는 코드 진행. 주로 종지(cadence, 악절을 마무리하는 코드 진행)로 쓰이며 대중음악과 재즈 등에서 매우 자주 사용된다. 2도에서 5도로, 5도에서 1도로 두 번 연달아 완전5도 하행하여 강한 안정감을 준다.', '/uploads/progression/audio/2.wav', '/uploads/progression/sample_midi/2.mid'),
    (nextval('progression_seq'), false, '캐논 진행의 앞 부분을 따서 만든 코드 진행. 원조 캐논 진행은 "I - V - VIm - IIIm - IV - I - IV - V"으로, 특유의 친숙하고 감성적인 느낌 덕에 대중음악에서 널리 파생되어 쓰인다.', '/uploads/progression/audio/3.wav', '/uploads/progression/sample_midi/3.mid'),
    (nextval('progression_seq'), true, '', '/uploads/progression/audio/5.wav', '/uploads/progression/sample_midi/5.mid'),
    (nextval('progression_seq'), true, '', '/uploads/progression/audio/6.wav', '/uploads/progression/sample_midi/6.mid'),
    (nextval('progression_seq'), false, '', '/uploads/progression/audio/7.wav', '/uploads/progression/sample_midi/7.mid'),
    (nextval('progression_seq'), true, '고조되는 느낌. 영화 <마녀 배달부 키키>의 OST "바다가 보이는 마을"이 이 코드 진행으로 시작된다. 보통 이 다음으로는 G7(V7)이 와서 긴장에 이른 뒤 해결된다.', '/uploads/progression/audio/8.wav', '/uploads/progression/sample_midi/8.mid');

INSERT INTO chord_progression_map (map_id, progression_id, position, chord_id)
VALUES
    (nextval('chord_progression_map_seq'), 1, 0, 1),
    (nextval('chord_progression_map_seq'), 1, 1, 13),
    (nextval('chord_progression_map_seq'), 1, 2, 5),
    (nextval('chord_progression_map_seq'), 2, 0, 2),
    (nextval('chord_progression_map_seq'), 2, 1, 5),
    (nextval('chord_progression_map_seq'), 2, 2, 1),
    (nextval('chord_progression_map_seq'), 3, 0, 1),
    (nextval('chord_progression_map_seq'), 3, 1, 5),
    (nextval('chord_progression_map_seq'), 3, 2, 6),
    (nextval('chord_progression_map_seq'), 3, 3, 3),
    (nextval('chord_progression_map_seq'), 4, 0, 4),
    (nextval('chord_progression_map_seq'), 4, 1, 5),
    (nextval('chord_progression_map_seq'), 4, 2, 8),
    (nextval('chord_progression_map_seq'), 5, 0, 16),
    (nextval('chord_progression_map_seq'), 5, 1, 22),
    (nextval('chord_progression_map_seq'), 5, 2, 14),
    (nextval('chord_progression_map_seq'), 6, 0, 1),
    (nextval('chord_progression_map_seq'), 6, 1, 20),
    (nextval('chord_progression_map_seq'), 6, 2, 9),
    (nextval('chord_progression_map_seq'), 6, 3, 3),
    (nextval('chord_progression_map_seq'), 7, 0, 2),
    (nextval('chord_progression_map_seq'), 7, 1, 3),
    (nextval('chord_progression_map_seq'), 7, 2, 4),
    (nextval('chord_progression_map_seq'), 7, 3, 20);



ALTER SEQUENCE chord_seq INCREMENT BY 50;
ALTER SEQUENCE progression_seq INCREMENT BY 50;
ALTER SEQUENCE chord_progression_map_seq INCREMENT BY 50;