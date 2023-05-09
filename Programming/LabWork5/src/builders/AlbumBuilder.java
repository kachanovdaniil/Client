package builders;

import common.Album;

/**

 The AlbumBuilder class is a builder class for objects of the Album class.
 */
public class AlbumBuilder {
    private String name; // The name of the album
    private long tracks; // The number of tracks in the album
    private Long length; // The length of the album in milliseconds
    private Float sales; // The number of sales of the album

    /**

     A method for setting the name of the album.
     @param name - The name of the album
     @return Returns the current instance of the AlbumBuilder class
     */
    public AlbumBuilder setName(String name) {
        this.name = name;
        return this;
    }
    /**

     A method for setting the number of tracks in the album.
     @param tracks - The number of tracks in the album
     @return Returns the current instance of the AlbumBuilder class
     */
    public AlbumBuilder setTracks(long tracks) {
        this.tracks = tracks;
        return this;
    }
    /**

     A method for setting the length of the album in milliseconds.
     @param length - The length of the album in milliseconds
     @return Returns the current instance of the AlbumBuilder class
     */
    public AlbumBuilder setLength(Long length) {
        this.length = length;
        return this;
    }
    /**

     A method for setting the number of sales of the album.
     @param sales - The number of sales of the album
     @return Returns the current instance of the AlbumBuilder class
     */
    public AlbumBuilder setSales(Float sales) {
        this.sales = sales;
        return this;
    }
    /**

     A method that creates a new Album object with the set field values.
     @return Returns a new Album object with the set field values.
     */
    public Album createAlbum() {
        return new Album(name, tracks, length, sales);
    }
}