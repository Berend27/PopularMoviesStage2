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

/*
public class Drink {
    private String name;
    private String description;
    private int imageResourceId;

    // drinks is an array of Drinks
    public static final Drink[] drinks = {
            new Drink("Latte", "A couple of espresso shots with steamed milk", R.drawable.latte),
            new Drink("Cappuccino", "Espresso, hot milk, and a steamed milk foam",
                    R.drawable.cappuccino),
            new Drink("Filter", "Highest quality beans roasted and brewed fresh",
                    R.drawable.filter)
    };

    //Each Drink has a name, description, and an image resource
    private Drink(String name, String description, int imageResourceId) {
        this.name = name;
        this.description = description;
        this.imageResourceId= imageResourceId;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    // The String representation of a Drink is its name.
    public String toString() {
        return this.name;
    }
*/
