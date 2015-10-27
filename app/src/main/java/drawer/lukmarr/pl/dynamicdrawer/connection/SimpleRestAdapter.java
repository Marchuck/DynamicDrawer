package drawer.lukmarr.pl.dynamicdrawer.connection;

/**
 * Created by Lukasz Marczak on 2015-08-20.
 */
public class SimpleRestAdapter {
    /**
     * adapter for retrofit operations, example of builder pattern
     */
    private final String name;
    private final int year;
    private final boolean isHuman;
    private final double height;


    private SimpleRestAdapter(Builder builder) {
        name = builder.name;
        year = builder.year;
        isHuman = builder.isHuman;
        height = builder.height;
    }

    public static class Builder {
        private String name;
        private int year;
        private boolean isHuman;
        private double height;

        public Builder() {
            name = "Lukasz";
            year = 1993;
            isHuman = true;
            height = 178.5;
        }

        Builder withName(String newName) {
            name = newName;
            return this;
        }

        Builder withYear(int newYear) {
            year = newYear;
            return this;
        }

        Builder withHumanity(boolean humanity) {
            isHuman = humanity;
            return this;
        }

        Builder withHeight(double newHeight) {
            height = newHeight;
            return this;
        }

        public SimpleRestAdapter build() {
            return new SimpleRestAdapter(this);
        }
    }


}
