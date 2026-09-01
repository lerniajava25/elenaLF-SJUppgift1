package elprices.services;

import elprices.domain.HourlyPrice;
import elprices.domain.PriceStatistics;

import java.util.List;
import java.util.Comparator;
import java.util.Optional;

public class PriceAnalyzer {

    // create a new method statistics to calculate: Lowest, highest and average price of the day, plus the cheapest/priciest hour.
    // and return object of type PriceStatistics.
    // input -> list<HourlyPrice> -> PriceAnalyzer ... Output -<PriceStatistics

    public PriceStatistics statistics(List<HourlyPrice> prices) {
        requireNonEmpty(prices);

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

    // using sliding window algorithm: Given hourly electricity prices -> find the cheapest block of consecutive 4 hours in a row
    public Optional<ChargingWindow> cheapestWindow(List<HourlyPrice> prices, int windowSize) {
        if (windowSize <= 0 || prices.size() < windowSize) {
            return Optional.empty();
        }

        // sort the hours according to the start hour
        var chronological = prices.stream().sorted(Comparator.comparing(HourlyPrice::start)).toList();

        double windowSum = 0;
        for (int i = 0; i < windowSize; i++) {
            windowSum += chronological.get(i).orePerKwh();
        }

        double bestSum = windowSum;
        int bestStart = 0;

        // the sliding window part: index: 0, 1, 2, 3, 4, 5  prices 80, 60, 40, 30, 90, 100 ->[80 60 40 30] 90 100-> 80 [60 40 30 90] 100
        // in short: When the window moves, one new price enters and one old price leaves
        for (int i = windowSize; i < chronological.size(); i++) {
            windowSum += chronological.get(i).orePerKwh()
                    - chronological.get(i - windowSize).orePerKwh();
            if (windowSum < bestSum) {
                bestSum = windowSum;
                bestStart = i - windowSize + 1;
            }
        }
        var window = chronological.subList(bestStart, bestStart + windowSize);
        var start = window.get(0).start();
        var end = window.get(window.size() - 1).start().plusHours(1);

        return Optional.of(new ChargingWindow(start, end, bestSum / windowSize, window));
    }

    private static void requireNonEmpty(List<HourlyPrice> prices) {
        if (prices == null || prices.isEmpty()) {
            throw new IllegalArgumentException("No prices to analyze.");
        }
    }
}
