package elprices.cli;

import elprices.application.PriceService;
import elprices.application.PriceUnavailableException;
import elprices.domain.ElectricityArea;
import elprices.domain.HourlyPrice;
import elprices.domain.PriceAnalyzer;

import java.util.List;
import java.util.function.Consumer;

    //The interactive menu. Owns the loop and all user input; delegates every calculation to PriceAnalyzer and every fetch to PriceService.

    public class ConsoleApp {

        private static final int CHARGING_WINDOW_HOURS = 4;

        private final PriceService priceService;
        private final PriceAnalyzer analyzer;
        private final ConsolePresenter presenter;

        private ElectricityArea selectedArea = ElectricityArea.SE3; // sensible default (Stockholm)

        public ConsoleApp(PriceService priceService, PriceAnalyzer analyzer, ConsolePresenter presenter) {
            this.priceService = priceService;
            this.analyzer = analyzer;
            this.presenter = presenter;
        }

        // this is the the menu loop (show menu -> ask -> show menu ->ask -> ...) until the user choose e to exit
        public void run() {
            var running = true;
            while (running) {
                presenter.menu(selectedArea);
                var choice = IO.readln("Ditt val: ");
                if (choice == null) {          // Ctrl-D / end of input
                    break;
                }
                switch (choice.trim()) {
                    case "1" -> chooseArea();
                    case "2" -> withTodaysPrices(prices -> presenter.statistics(analyzer.statistics(prices)));
                    case "3" -> withTodaysPrices(prices -> presenter.sorted(analyzer.sortedByPrice(prices)));
                    case "4" -> withTodaysPrices(this::showChargingWindow);
                    case "e", "E" -> {
                        presenter.info("Hej då!");
                        running = false;
                    }
                    default -> presenter.error("Ogiltigt val. Välj 1-4 eller e.");
                }
            }
        }
            // implementation for area selection
        private void chooseArea() {
            var input = IO.readln("Ange elområde (SE1, SE2, SE3, SE4): ");
            ElectricityArea.parse(input).ifPresentOrElse(
                    area -> {
                        selectedArea = area;
                        presenter.info("Valt område: " + area + " - " + area.description());
                    },
                    () -> presenter.error("Okänt område: \""
                            + (input == null ? "" : input.trim()) + "\". Försök igen."));
        }

    ///used method reference for charging for case "4"
        private void showChargingWindow(List<HourlyPrice> prices) {
            analyzer.cheapestWindow(prices, CHARGING_WINDOW_HOURS).ifPresentOrElse(
                    presenter::chargingWindow,
                    () -> presenter.error("För få timmar för att beräkna ett "
                            + CHARGING_WINDOW_HOURS + "h-fönster."));
        }


         //Fetches today's prices for the selected area and hands them to the given action.
        //it means : Give me some operation that accepts a list of prices.
        // menu option2 -> get today's prices -> priceAnalyzer.statistics -> ConsolePresenter.statistics

        private void withTodaysPrices(Consumer<List<HourlyPrice>> action) {
            try {
                var prices = priceService.today(selectedArea);
                if (prices.isEmpty()) {
                    presenter.error("Inga priser tillgängliga för " + selectedArea + " idag.");
                    return;
                }
                action.accept(prices);
            } catch (PriceUnavailableException e) {
                presenter.error(e.getMessage());
            }
        }
    }

