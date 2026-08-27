package elprices.domain;
import java.util.List;
import java.util.Comparator;
import java.util.Optional;

public class PriceAnalyzer {

    // create a new method statistics to calculate: Lowest, highest and average price of the day, plus the cheapest/priciest hour.
    // and return object of type PriceStatistics.
    // input -> list<HourlyPrice> -> PriceAnalyzer ... Output -<PriceStatistics

    public PriceStatistics statistics(List<HourlyPrice> prices) {

        //stream lets you process the objects in the collection. 08 100 -> 09 75 -> ....  to be able to use .min, .max() .average(), .filter(), .sorted()
        // Create a Comparator that compares HourlyPrice objects using their orePerKwh value. by using method reference(lambda).equivalant to price -> price.orePerKwh()
        var cheapest = prices.stream().min(Comparator.comparingDouble(HourlyPrice::orePerKwh)).orElseThrow();

        var priciest = prices.stream().max(Comparator.comparingDouble(HourlyPrice::orePerKwh)).orElseThrow();

        var average = prices.stream().mapToDouble(HourlyPrice::orePerKwh).average().orElseThrow(); //maptodouble means take the object HourlyPrice and transfere it to a double

        return new PriceStatistics(
                cheapest.orePerKwh(),
                priciest.orePerKwh(),
                average,
                cheapest,
                priciest
        );
    }

    //the day's hours sorted from cheapest to most expensive method.
    public List<HourlyPrice> sortedByPrice(List<HourlyPrice> prices) {
        return prices.stream().sorted(Comparator.comparingDouble(HourlyPrice::orePerKwh)).toList();
    }
}
