package pl.konradboniecki.nbp;

import lombok.Cleanup;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.regex.Pattern;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 *     Dla rekrutera: zostaje wyswietlona instrukcja jak wpisać dane. Dane przechodzą prostą walidację (np czy roznica w dniach nie przekracza 93) po czym zostaje wyslane zapytanie korzystające z API NBP.
 *     Program pobiera dane w formacie JSON i przetwarza je uzyskując średnią cenę kupna i odchylenie standardowe kursu sprzedaży.Kursy są obliczane przy pomocy klasy BigDecimal
 *
 */

public class Console {
    
    private static Request request;
    
    public static void main(String[] args) {
        Console console = new Console();
        request = new Request();
        String[] typedInput;
        @Cleanup
        Scanner scanner = new Scanner(System.in);
    
        console.printSimpleTutorial();
        while(true){
            console.printMessage("Input data:");
            typedInput = scanner.nextLine().split(" ");
            typedInput[0] = typedInput[0].toUpperCase();
            if (console.isCorrectInput(typedInput)){
                request.setStartDate(extractLocalDateFromStringTable(typedInput[1]));
                request.setEndDate(extractLocalDateFromStringTable(typedInput[2]));
                request.setCurrency(typedInput[0]);
                
                Currency[] currencies = NbpApiHandler.getCurrency(request);
                BigDecimal avgBuyPrice = Currency.getAvgBuyPrice(currencies);
                BigDecimal avgSellingPrice = Currency.getAvgSellingPrice(currencies);
                
                console.printMessage("Average buy price: " + avgBuyPrice);
                console.printMessage("Standard deviation of selling price: " + Currency.standardDeviation(currencies, avgSellingPrice));
                    
            } else{
                console.printMessage("                            INVALID DATA!");
                console.printSimpleTutorial();
            }
        }
    }
    
    private void printSimpleTutorial(){
        System.out.println("+---------------------------------------------------------------------+");
        System.out.println("|Enter currency Symbol and 2 dates. Separate them with a single space.|");
        System.out.println("|Formula: CURRENCY_SIGN START_DATE END_DATE                           |");
        System.out.println("|Date Format: YYYY-MM-DD                                              |");
        System.out.println("|Example input: EUR 2017-11-20 2017-11-24                             |");
        System.out.println("+---------------------------------------------------------------------+");
        
    }
    private void printMessage(String message){
        System.out.println(message);
    }
    private boolean isCorrectInput(String[] input){
        return validateStringInput(input);
    }
    private boolean validateStringInput(String[] input){
        if (input.length == 3 &&
                Pattern.matches("[A-Z]{3}",input[0]) &&
                Pattern.matches("\\d{4}-\\d{1,2}-\\d{1,2}",input[1]) &&
                Pattern.matches("\\d{4}-\\d{1,2}-\\d{1,2}",input[2]) ){
    
            LocalDate startDate, endDate;
            try {
                startDate = extractLocalDateFromStringTable(input[1]);
                endDate = extractLocalDateFromStringTable(input[2]);
            } catch(DateTimeException e){
                return false;
            }
            
            if (startDate.until(endDate, DAYS) > 0 && startDate.until(endDate, DAYS) <= 93)
                return true;
            else
                return false;
        } else {
            return false;
        }
    }
    
    private static LocalDate extractLocalDateFromStringTable(String input) throws DateTimeException{
        //Offensive Programming. Assuming correct data.
        String[] date = input.split("-");
    
        return LocalDate.of(Integer.parseInt(date[0]),
                Integer.parseInt(date[1]),Integer.parseInt(date[2]));
    }
}
