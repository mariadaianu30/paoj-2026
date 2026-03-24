package com.pao.laboratory05.playlist;

public record Song(String title, String artist, int durationSeconds)
        implements Comparable<Song> {

     public Song {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (artist == null || artist.isBlank()) {
            throw new IllegalArgumentException("Artist cannot be empty");
        }
        if (durationSeconds <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }

        title = title.trim();
        artist = artist.trim();
    }
    public String formattedDuration() {
        return durationSeconds / 60 + ":" + String.format("%02d", durationSeconds % 60);
    }


    public int compareTo(Song other) {
        return this.title.compareTo(other.title);
    }

    // ❌ FORBIDDEN — cannot add new instance fields
    // private String album;
}