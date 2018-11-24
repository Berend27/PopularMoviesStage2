package com.udacity.popularmovies;

public class Trailer {
    private String title;
    private String keyForYouTube;

    // demo array for development
    public static final Trailer[] trailers = {
            new Trailer("Mission: Impossible - Fallout (2018) - Official Trailer - Paramount Pictures",
                    "wb49-oV0F78"),
            new Trailer("Mission: Impossible - Fallout (2018) - Official Trailer - Paramount Pictures",
                    "XiHiW4N7-bo"),
            new Trailer("Mission: Impossible-Fallout (2018)- \"All Stunts\"- Paramount Pictures",
                    "Z_aCOQi5tm4"),
            new Trailer("Mission: Impossible - Fallout (2018) - Helicopter Stunt Behind The Scenes - Paramount Pictures",
                    "Um0aZKbpe1Y")
    };

    public Trailer(String title, String youtubeKey)
    {
        this.title = title;
        this.keyForYouTube = youtubeKey;
    }

    public String getTitle()
    {
        return title;
    }

    public String getYouTubeKey()
    {
        return keyForYouTube;
    }

    public String toString()
    {
        return this.title;
    }
}
