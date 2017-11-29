package pl.konradboniecki.nbp;

import lombok.Data;

import java.math.BigDecimal;
import java.math.MathContext;

@Data
class Currency {
    private BigDecimal bPrice;
    private BigDecimal sPrice;
    
    private static MathContext mathContext = new MathContext(5);
    
    public Currency(double bPrice, double sPrice) {
        this.bPrice = BigDecimal.valueOf(bPrice);
        this.sPrice = BigDecimal.valueOf(sPrice);
    }
    
    public static BigDecimal getAvgBuyPrice(Currency[] currencies){
        BigDecimal average = new BigDecimal(0,mathContext);
        for (Currency currency : currencies){
            average = average.add(currency.getBPrice(),mathContext);
        }
        return average.divide(new BigDecimal(currencies.length,mathContext),mathContext);
    }
    public static BigDecimal getAvgSellingPrice(Currency[] currencies){
        BigDecimal average = new BigDecimal(0,mathContext);
        for (Currency currency : currencies){
            average = average.add(currency.getSPrice(),mathContext);
        }
        return average.divide(new BigDecimal(currencies.length,mathContext),mathContext);
    }
    
    public static BigDecimal standardDeviation(Currency[] currencies, BigDecimal average){
        BigDecimal divider = new BigDecimal(currencies.length,mathContext);
        BigDecimal standardDeviation = new BigDecimal(0, mathContext);
        
        for (int i=0; i< currencies.length; i++){
            BigDecimal power = new BigDecimal(currencies[i].getSPrice().subtract(average).pow(2).doubleValue(),mathContext);
            standardDeviation = standardDeviation.add(power,mathContext);
        }
        
        standardDeviation = standardDeviation.divide(divider,mathContext);
        standardDeviation = new BigDecimal(Math.sqrt(standardDeviation.doubleValue()),mathContext);
        return standardDeviation;
    }
}
