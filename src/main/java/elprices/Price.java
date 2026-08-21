package elprices;

// setting the price model  price for hourly electricity prices (0-23 h, price in Öre)
public record Price(int hour, double orePerkwh) {

    public Price{
        if(hour <= 0 || hour > 24){
            throw new IllegalArgumentException("Hour must be between 0 and 23.59" + hour );
        }
    }
}
