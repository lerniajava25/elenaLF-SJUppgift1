package elprices.domain;

//statistics for a day of hourly prices. All prices are in öre/kWh.
// put it in a record return an object with all the results instead of returning 5 unrelated values if i would count each value seperately
// eg, (10 min, 200 max,105 average, 03-04 cheapest, 18-19 priciest (it looks like this because of the lable that i created in HourlyPrices) )
public record PriceStatistics(
        double min,
        double max,
        double average,
        HourlyPrice cheapestHour,
        HourlyPrice priciestHour
) {
}
