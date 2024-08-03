package ec.edu.espe.chatws.chatwebsocketserver.utils;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class ColorUtils {
    private static ColorUtils instance;
    private final List<Pair<String, String>> flatColors = List.of(
            Pair.of("#16A085", "#ffffff"),
            Pair.of("#27AE60", "#ffffff"),
            Pair.of("#2980B9", "#ffffff"),
            Pair.of("#9B59B6", "#ffffff"),
            Pair.of("#8E44AD", "#ffffff"),
            Pair.of("#34495E", "#ffffff"),
            Pair.of("#C0392B", "#ffffff"),
            Pair.of("#2980b9", "#ffffff"),
            Pair.of("#dc7633", "#ffffff"),
            Pair.of("#2e4053", "#ffffff")
    );

    public static ColorUtils getInstance() {
        if (instance == null) {
            instance = new ColorUtils();
        }

        return instance;
    }

    public Pair<String, String> getRandomFlatColor() {
        return flatColors.get((int) (Math.random() * flatColors.size()));
    }
}
