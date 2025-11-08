package theater;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

/** Generates a plain-text statement for an invoice. */
public class StatementPrinter {
    /** Factor to convert cents to dollars. */
    private static final double CENTS_PER_DOLLAR = 100.0;

    private final Invoice invoice;
    private final Map<String, Play> plays;

    /**
     * Constructs a statement printer.
     *
     * @param invoice the invoice to render
     * @param plays   mapping from play id to play
     */
    public StatementPrinter(Invoice invoice, Map<String, Play> plays) {
        this.invoice = invoice;
        this.plays = plays;
    }

    /**
     * Produces the plain-text statement for this printer's invoice.
     *
     * @return the formatted statement
     */
    public String statement() {
        int totalAmount = 0;
        int volumeCredits = 0;

        final StringBuilder result =
                new StringBuilder("Statement for " + invoice.getCustomer() + System.lineSeparator());

        for (final Performance performance : invoice.getPerformances()) {
            final Play play = getPlay(performance);

            final int thisAmount = getAmount(performance, play);
            volumeCredits += getVolumeCredits(performance, play);

            // line per performance
            result.append(String.format("  %s: %s (%s seats)%n",
                    play.getName(), usd(thisAmount), performance.getAudience()));

            totalAmount += thisAmount;
        }

        result.append(String.format("Amount owed is %s%n", usd(totalAmount)));
        result.append(String.format("You earned %s credits%n", volumeCredits));
        return result.toString();
    }

    private Play getPlay(final Performance performance) {
        return plays.get(performance.getPlayID());
    }

    /**
     * Formats a cent value in USD.
     *
     * @param amountInCents the amount in cents
     * @return the amount formatted as a US currency string
     */
    private String usd(final int amountInCents) {
        final NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);
        return frmt.format(amountInCents / CENTS_PER_DOLLAR);
    }

    /**
     * Computes the amount owed for a single performance.
     *
     * @param performance the performance
     * @param play        the play of the performance
     * @return amount in cents
     * @throws UnknownPlayTypeException if the play type is not supported
     */
    private int getAmount(final Performance performance, final Play play) {
        final int audience = performance.getAudience();
        int amount;

        switch (play.getType()) {
            case "tragedy":
                amount = Constants.TRAGEDY_BASE_AMOUNT;
                if (audience > Constants.TRAGEDY_AUDIENCE_THRESHOLD) {
                    amount += (audience - Constants.TRAGEDY_AUDIENCE_THRESHOLD)
                            * Constants.TRAGEDY_OVER_BASE_CAPACITY_PER_PERSON;
                }
                break;

            case "comedy":
                amount = Constants.COMEDY_BASE_AMOUNT;
                if (audience > Constants.COMEDY_AUDIENCE_THRESHOLD) {
                    amount += Constants.COMEDY_OVER_BASE_CAPACITY_AMOUNT
                            + (audience - Constants.COMEDY_AUDIENCE_THRESHOLD)
                            * Constants.COMEDY_OVER_BASE_CAPACITY_PER_PERSON;
                }
                amount += Constants.COMEDY_AMOUNT_PER_AUDIENCE * audience;
                break;

            default:
                throw new UnknownPlayTypeException(play.getType());
        }
        return amount;
    }

    /**
     * Computes volume credits contributed by a single performance.
     *
     * @param performance the performance
     * @param play        the play of the performance
     * @return credits earned
     */
    private int getVolumeCredits(final Performance performance, final Play play) {
        int credits = Math.max(performance.getAudience() - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);
        if ("comedy".equals(play.getType())) {
            credits += performance.getAudience() / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
        }
        return credits;
    }
}

