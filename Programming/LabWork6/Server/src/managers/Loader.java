/**
 * The Loader class is used to read data from console or file, write data in CSV format, create Album and MusicBand objects, and convert strings to corresponding data types.
 */

package managers;

import builders.AlbumBuilder;
import builders.CoordinatesBuilder;
import builders.MusicBandBuilder;
import common.Album;
import common.Collection;
import common.MusicBand;
import common.MusicGenre;
import result.Result;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Loader {

    /**
     * Date formatting object for LocalDate class
     */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * BufferedReader object for reading from input stream
     */
    private BufferedReader reader;

    private final boolean isConsole;

    /**
     * Constructs an instance of Loader class with specified input stream
     *
     * @param reader - input stream
     */
    public Loader(BufferedReader reader, boolean isConsole) {
        this.reader = reader;
        this.isConsole = isConsole;
    }

    /**
     * Checks whether the input stream is console
     *
     * @return true, if the input stream is console, false - otherwise
     */
    public boolean isConsole() {
        try {
            return isConsole;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Sets a new input stream
     *
     * @param reader - input stream
     */

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    /**
     * Reads album data from input stream
     *
     * @return Result object containing the created Album object or error message
     */
    public Result<Album> enterAlbum() {
        try {
            if (!this.isConsole()) {
                try {
                    return Result.success(new AlbumBuilder()
                            .setName(reader.readLine())
                            .setTracks(Long.parseLong(reader.readLine().trim()))
                            .setLength(Long.parseLong(reader.readLine().trim()))
                            .setSales(Float.parseFloat(reader.readLine().trim()))
                            .build());
                } catch (Exception e) {
                    return Result.failure(e, e.getMessage());
                }

            } else {
                AlbumBuilder builder = new AlbumBuilder();
                System.out.println("Enter album name:");
                builder.setName(reader.readLine());
                System.out.println("Enter number of tracks:");
                try {
                    builder.setTracks(Long.parseLong(reader.readLine().trim()));
                } catch (Exception e) {
                    return Result.failure(e, "Number of tracks is a number more 0");
                }
                System.out.println("Enter album length:");
                try {
                    builder.setLength(Long.parseLong(reader.readLine().trim()));
                } catch (Exception e) {
                    return Result.failure(e, "Length is a number more 0");
                }
                System.out.println("Enter album sales:");
                try {
                    builder.setSales(Float.parseFloat(reader.readLine().trim()));
                } catch (Exception e) {
                    return Result.failure(e, "Sales is a number more 0");
                }
                return Result.success(builder.build());
            }
        } catch (Exception e) {
            return Result.failure(e, "Error while reading album data.");
        }
    }




    /**
     * Change object MusicBand to CSV format
     *
     * @param band - object MusicBand
     * @return object Result with CSV format or error message
     */
    public Result<String> toCSV(MusicBand band) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(band.getID()).append(",").append(band.getName()).append(",");
            sb.append(band.getCoordinates().getX()).append(",").append(band.getCoordinates().getY()).append(",");
            sb.append(band.getCreationDate().format(formatter)).append(",").append(band.getNumberOfParticipants()).append(",");
            sb.append(band.getGenre()).append(",");
            Album album = band.getBestAlbum();
            if (band.getBestAlbum() != null) {
                sb.append(album.getName()).append(",");
                sb.append(album.getTracks()).append(",");
                sb.append(album.getLength()).append(",");
                sb.append(album.getSales());
            } else {
                sb.append(",");
            }
            return Result.success(sb.toString());
        } catch (Exception e) {
            return Result.failure(e, "Error with parsing CSV format");
        }
    }

    /**
     * Change String to LocalDate
     *
     * @param formattedDate - String with date
     * @return object Result with LocalDate or error message
     */

    public Result<LocalDate> parseDate(String formattedDate) {
        try {
            return Result.success(LocalDate.parse(formattedDate, formatter));
        } catch (Exception e) {
            return Result.failure(e, "Error with date format");
        }
    }

    /**
     * Reads id from input stream
     *
     * @return object Result, containing read line or error message
     */
    public Result<Long> enterId() {
        if (this.isConsole())
            System.out.println("Enter ID: ");
        try {
            return Result.success(Long.parseLong(reader.readLine().trim()));
        } catch (Exception e) {
            return Result.failure(e, "Error with getting ID");
        }
    }

    /**
     * Read band from input stream
     *
     * @return object Result, containing read line or error message
     */
    public Result<MusicBand> enterBand() {
        try {
            Result<String> result;
            MusicBandBuilder builder = new MusicBandBuilder();

            if (this.isConsole()) {
                System.out.println("Enter band name:");
            }
            result = this.readLine();
            if (result.isSuccess()) {
                builder.setName(result.getValue().get());
            } else {
                return Result.failure(result.getError().get(), "Error while entering band name");
            }


            CoordinatesBuilder coordinates;
            if (this.isConsole()) {
                System.out.println("Enter band coordinates:");
                System.out.print("x = ");
            }
            result = this.readLine();
            if (result.isSuccess()) {
                try {
                    coordinates = new CoordinatesBuilder().setX(Long.parseLong(result.getValue().get()));
                } catch (Exception e) {
                    return Result.failure(e, "Error while entering band coordinates. It must be a number more -129.");
                }
            } else {
                return Result.failure(result.getError().get(), "Error while entering band coordinates. It must be a number more -129.");
            }

            if (this.isConsole) {
                System.out.print("y = ");
            }
            result = this.readLine();
            if (result.isSuccess()) {
                try {
                    coordinates.setY(Float.parseFloat(result.getValue().get()));
                } catch (Exception e) {
                    return Result.failure(e, "Error while entering band coordinates. It must be a number more -420.");
                }
            } else {
                return Result.failure(result.getError().get(), "Error while entering band coordinates. It must be a number more -420.");
            }
            builder.setCoordinates(coordinates.build());


            if (this.isConsole()) {
                System.out.println("Enter number of participants:");
            }
            String number = reader.readLine().trim();
            try {
                if (Long.parseLong(number) <= 0) {
                    return Result.failure(new IllegalArgumentException("Number of participants must be greater than 0"), "Error while entering number of participants, number of participants must be greater than 0");
                }
            } catch (Exception e) {
                return Result.failure(e, "Error while entering number of participants, number of participants must be number greater than 0");
            }
            builder.setNumberOfParticipants(Long.parseLong(number));


            if (this.isConsole()) {
                System.out.println("Enter date of creation with format dd-mm-yyyy:");
            }
            result = this.readLine();
            try {
                if (result.isSuccess()) {
                    LocalDate date = parseDate(result.getValue().get()).getValue().get();
                    if (date.isAfter(LocalDate.now())) {
                        return Result.failure(new IllegalArgumentException("Date of creation must be earlier than current date"), "Error while entering date of creation, date of creation must be earlier than current date");
                    }
                    builder.setCreationDate(date);
                } else {
                    return Result.failure(result.getError().get(), "Error while entering date of creation");
                }
            } catch (Exception e) {
                return Result.failure(e, "Error while entering date of creation, it must be dd-mm-yyyy and earlier than current date");
            }


            MusicGenre genre = null;
            if (this.isConsole()) {
                System.out.println("Enter genre:");
                while (genre == null) {
                    try {
                        System.out.print("Available genres:");
                        for (MusicGenre musicGenre : MusicGenre.values()) {
                            System.out.print(musicGenre.name() + " ");
                        }
                        System.out.println();
                        result = this.readLine();
                        genre = MusicGenre.valueOf(result.getValue().get().trim().toUpperCase());
                    } catch (Exception e) {
                        System.out.println("Error while entering genre");
                    }
                }
            } else {
                result = this.readLine();
            }
            try {
                genre = MusicGenre.valueOf(result.getValue().get().trim().toUpperCase());
            } catch (Exception e) {
                return Result.failure(e, "Error while entering genre");
            }
            builder.setGenre(genre);


            try {
                Result resultAlbum = this.enterAlbum();
                if (resultAlbum.isSuccess()) {
                    builder.setBestAlbum((Album) resultAlbum.getValue().get());
                } else {
                    return resultAlbum;
                }
            } catch (Exception e) {
                return Result.failure(e, "Error while entering band");
            }
            return Result.success(builder.build());
        } catch (Exception e) {
            return Result.failure(e, "Error while entering band ");
        }
    }

    /**
     * Reads line from input stream
     *
     * @return object Result, containing line or error message
     */
    public Result<String> readLine() {
        try {
            String line = reader.readLine();
            return Result.success(Objects.requireNonNullElse(line, ""));
        } catch (Exception e) {
            return Result.failure(e, "Error with reading");
        }
    }

}
