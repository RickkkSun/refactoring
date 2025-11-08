package theater;

/** Constants used in this program. */
public final class Constants {
    // volume credits
    public static final int BASE_VOLUME_CREDIT_THRESHOLD = 30;
    public static final int COMEDY_EXTRA_VOLUME_FACTOR = 5;

    // tragedy pricing
    public static final int TRAGEDY_BASE_AMOUNT = 40_000;
    public static final int TRAGEDY_AUDIENCE_THRESHOLD = 30;
    public static final int TRAGEDY_OVER_BASE_CAPACITY_PER_PERSON = 1_000;

    // comedy pricing
    public static final int COMEDY_BASE_AMOUNT = 30_000;
    public static final int COMEDY_AUDIENCE_THRESHOLD = 20;
    public static final int COMEDY_OVER_BASE_CAPACITY_AMOUNT = 10_000;
    public static final int COMEDY_OVER_BASE_CAPACITY_PER_PERSON = 500;
    public static final int COMEDY_AMOUNT_PER_AUDIENCE = 300;

    private Constants() {
    }
}

