package elprices;

// setting the price model  price for hourly electricity prices (0-23 h, price in Öre)
public record Price(int hour, double orePerkwh) {
}
