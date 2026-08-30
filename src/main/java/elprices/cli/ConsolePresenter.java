package elprices.cli;

import elprices.domain.ElectricityArea;
import elprices.domain.HourlyPrice;
import elprices.domain.PriceStatistics;
import elprices.services.ChargingWindow;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

// this class is only for all users' presentation, in short it is  for all ***println()*** in the console

public class ConsolePresenter {
    //Everything that writes to the console lives here, isolated from the logic.

    private static final Locale SV = Locale.forLanguageTag("sv-SE"); //important for swedish formatting convention in the string
    private static final DateTimeFormatter HOUR = DateTimeFormatter.ofPattern("HH:mm");

    // menu method
    public void menu(ElectricityArea selectedArea) {
        IO.println("""
                Elpriser – Analysverktyg   [Valt område: %s]
                ========================
                1. Välj elområde (SE1, SE2, SE3, SE4)
                2. Min, Max och Medelpris
                3. Sortera priser (lägst till högst)
                4. Bästa laddningstid (4h sammanhängande)
                e. Avsluta
                """.formatted(selectedArea));
    }

    // this receives the calculation fro Prestatistics and presents it here
    public void statistics(PriceStatistics stats) {
        IO.println();
        IO.println("Lägsta pris:  %s  (kl %s)".formatted(ore(stats.min()), stats.cheapestHour().label()));
        IO.println("Högsta pris:  %s  (kl %s)".formatted(ore(stats.max()), stats.priciestHour().label()));
        IO.println("Medelpris:    %s".formatted(ore(stats.average())));
    }

    // presenting the sorted prices from the PriceAnalyzer.
        public void sorted (List < HourlyPrice > sortedPrices) {
            IO.println();
            IO.println("Priser sorterade lägst → högst:");
            var rank = 1;
            for (var price : sortedPrices) {
                IO.println("%2d. kl %s   %s".formatted(rank++, price.label(), ore(price.orePerKwh())));
            }
        }

        //
        public void chargingWindow (ChargingWindow window){
            IO.println();
            IO.println("Bästa laddningstid (%d h): kl %s-%s".formatted(
                    window.hours().size(), window.start().format(HOUR), window.end().format(HOUR)));
            IO.println("Snittpris under fönstret: %s".formatted(ore(window.averageOre())));
        }

        public void info (String message){
            IO.println(message);
        }

        public void error (String message){
            IO.println("-  " + message);
        }

//formatting the Öre to be consistance with the swedish decimal formating
        private String ore ( double value){
            return String.format(ConsolePresenter.SV, "%.2f öre/kWh", value);
        }
    }

